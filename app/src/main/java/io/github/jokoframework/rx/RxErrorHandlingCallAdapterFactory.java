package io.github.jokoframework.rx;

import android.content.Context;
import android.util.Log;

import com.facebook.login.LoginManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.github.jokoframework.exception.NoFinhealthServerConnection;
import io.github.jokoframework.mboehaolib.rx.RetrofitException;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.singleton.MboehaoApp;
import io.github.jokoframework.utilities.AppUtils;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func1;


public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private final RxJavaCallAdapterFactory original;
    private Context context;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJavaCallAdapterFactory.create();
    }

    private RxErrorHandlingCallAdapterFactory(Scheduler scheduler) {
        original = RxJavaCallAdapterFactory.createWithScheduler(scheduler);
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    public static CallAdapter.Factory createWithScheduler(Scheduler scheduler) {
        return new RxErrorHandlingCallAdapterFactory(scheduler);
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {
        private static final String LOG_TAG = RxCallAdapterWrapper.class.getName();
        private final Retrofit retrofit;
        private final CallAdapter<?> wrapped;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            final Observable observable = ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Func1<Throwable, Observable>() {
                @Override
                public Observable call(final Throwable throwable) {
                    if (throwable instanceof SocketTimeoutException || throwable instanceof UnknownHostException
                            || throwable instanceof retrofit2.adapter.rxjava.HttpException
                            || throwable instanceof ConnectException) {
                        Observable observableWithError = Observable.create(new Observable.OnSubscribe<Object>() {
                            @Override
                            public void call(Subscriber<? super Object> subscriber) {
                                final String msg = String.format("Exception en el Observable: %s", throwable);
                                Log.d(LOG_TAG, msg);
                                if (throwable instanceof HttpException) {
                                    int code = ((HttpException) throwable).code();
                                    if (code == 401) {
                                        // Una excepción con código 401 nos indica que no hay autorización
                                        // para realizar la petición. Si llegó a este punto significa
                                        // que no se pudo renovar el access token (lo más probable es que
                                        // haya iniciado sesión en otro dispositivo lo que hizo que se
                                        // revocara el refresh token que tenía anteriormente), por lo tanto hay
                                        // que volver a autenticar al usuario.
                                        Log.d(LOG_TAG, "Se detectó una petición no autorizada. Se solicita de vuelta el login.");
                                        Utils.showStickyMessage(MboehaoApp.getActivity(), "Por favor ingrese de nuevo");
                                        AppUtils.showLogin(MboehaoApp.getSingletonApplicationContext(), (HttpException) throwable);
                                        LoginManager.getInstance().logOut();
                                    } else if (code >= 500 && code <= 599) {
                                        final String message = String.format("No se puede contactar con el servidor en estos momentos: %s",
                                                throwable.getMessage());
                                        Log.e(LOG_TAG, message, throwable);
                                        MboehaoApp.setProgressMessage(message);
                                        Utils.showStickyMessage(MboehaoApp.getActivity(), message);
                                        LoginManager.getInstance().logOut();
                                    } else if (throwable instanceof SocketTimeoutException) {
                                        String message = "La conexión con el servidor es inestable, intente de vuelta en unos momentos";
                                        Utils.showStickyMessage(MboehaoApp.getActivity(), message);
                                        AppUtils.showTimeOutError(MboehaoApp.getSingletonApplicationContext(), (SocketTimeoutException) throwable);
                                    } else {
                                        showGenericError(throwable);
                                    }
                                } else {
                                    showGenericError(throwable);
                                }

                            }
                        });
                        return observableWithError;
                    } else {
                        Log.d(LOG_TAG, String.format("Exception en el ErrorHandler: %s", throwable));
                        final RetrofitException retrofitException = asRetrofitException(throwable);
                        //PP Esto hace explotar la PP si es un exception no capturado
                        return Observable.error(retrofitException).first();
                        //return Observable.empty();
                    }
                }
            });

            return observable;
        }

        private void showGenericError(Throwable throwable) {
            final String message = String.format("No se puede contactar con el servidor en estos momentos: %s",
                    throwable.getMessage());
            Log.e(LOG_TAG, message, throwable);
            MboehaoApp.setProgressMessage(message);
            Utils.showStickyMessage(MboehaoApp.getActivity(), message);
        }

        private RetrofitException asRetrofitException(Throwable throwable) {
            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                return RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit);
            } else if (throwable instanceof NoFinhealthServerConnection && AppUtils.NO_CONEXION_VISIBLE) {
                //No hacemos nada, ya se esta mostrando el error de No Conexion
                RetrofitException.networkError((IOException) throwable);
            } else {// A network error happened
                if (throwable instanceof IOException) {
                    //PP: Nadie hace catch de esto y hace explotar la APP
                    return RetrofitException.networkError((IOException) throwable);
                }
            }
            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.unexpectedError(throwable);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
