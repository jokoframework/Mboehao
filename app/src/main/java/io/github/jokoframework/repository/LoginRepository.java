package io.github.jokoframework.repository;

import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.model.DefaultResponse;
import io.github.jokoframework.model.LoginRequest;
import io.github.jokoframework.model.UserAccessResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginRepository {
    @POST(Constants.API_URL + "/login")
    Observable<UserAccessResponse> login(@Body LoginRequest loginRequest,
                                    @Header(Constants.HEADER_VERSION) String refreshToken);

    @POST(Constants.API_URL + "/logout")
    Observable<DefaultResponse> logout(@Header(Constants.HEADER_AUTH) String refreshToken);

    @POST(Constants.API_URL + "/token/user-access")
    Observable<UserAccessResponse> userAccess(@Header(Constants.HEADER_AUTH) String refreshToken);

    @POST(Constants.API_URL + "/token/user-access")
    Call<UserAccessResponse> refreshUserAccess(@Header(Constants.HEADER_AUTH) String refreshToken);

    @POST(Constants.API_URL + "/token/refresh")
    Call<UserAccessResponse> refreshToken(@Header(Constants.HEADER_AUTH) String refreshToken);
}
