package io.github.jokoframework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseUser;

import org.json.JSONArray;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.ParseUtils;
import io.github.jokoframework.mboehaolib.util.Utils;


public class CronService extends Service {
    /**
     * Log tag for this service.
     */
    private static final String LOG_TAG = "CRON";

    private IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        Utils.showToast(getBaseContext(), String.format("Periodic REST"));
        super.onCreate();
        handlerRESTServiceExecution(); // doPeriodicTasks();
    }

    public void handlerRESTServiceExecution() {
        Utils.showToast(getBaseContext(), String.format("REST Service Execution"));
        handler.postDelayed(runRESTService, Constants.FIRST_TIME);
    }

    @Override
    public IBinder onBind(final Intent intent) {
        Utils.showToast(getBaseContext(), String.format("REST onBind"));
        Log.i(LOG_TAG, "Entering onBind Method in CronService");
        return mBinder;
    }

    private final Handler handler = new Handler();
    Runnable runRESTService = new Runnable() {
        @Override
        public void run() {
            Utils.showToast(getBaseContext(), String.format("Checkeo de API"));
            checkAPI();
            handler.postDelayed(this, Constants.ONE_MINUTE);
        }

        private void checkAPI() {
            // Instanciar el RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            String url = getString(R.string.rest_URL);
            Utils.showToast(getBaseContext(), String.format("URL is: " + url));

            // Solicitar un JSON Array como respuesta de la URL.
            JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response)
                {
                    // Mostrar el response (DEBUG)
                    Utils.showToast(getBaseContext(), String.format("Response is: "+ response.toString()));

                    // Verificar response
                    //if not null -> ROOM
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Utils.showToast(getBaseContext(), String.format("ERROR: "+ error.toString()));
                    //Log.d(LOG_TAG, String.format("Respuesta invalida: "+ error.toString());
                }
            }
            );

            // Add the request to the RequestQueue.
            postRequest.setShouldCache(false);
            queue.add(postRequest);

            //legacy
            /*
            String appNews = ParseUtils.getParameterValue(getBaseContext(), Constants.APP_NEWS);
            if (StringUtils.isNotBlank(appNews)) {
                Log.d(LOG_TAG, String.format("Agregando noticia de la app %s", appNews));
                Utils.addPrefs(getBaseContext(), Constants.USER_PREFERENCE_APP_NEWS, appNews);
                //Cada 12 horas, que corre el Cron reiniciamos sí o sí la fecha de noticias mostradas
                Utils.addPrefs(getBaseContext(), Constants.USER_PREFERENCE_APP_NEWS_SHOWED_TIME, 0L);
            } else {
                Log.d(LOG_TAG, String.format("Eliminanda noticia de la app"));
            }*/
        }
    };

    public class MyBinder extends Binder {
        public CronService getService() {
            return CronService.this;
        }
    }

}
