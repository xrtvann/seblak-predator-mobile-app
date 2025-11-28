package com.irvan.seblakpredator.apiclient;
import com.irvan.seblakpredator.model.ForgetPassRequest;
import com.irvan.seblakpredator.model.ForgetPassResponse;
import com.irvan.seblakpredator.model.LoginRequest;
import com.irvan.seblakpredator.model.LoginResponse;
import com.irvan.seblakpredator.model.ProfileRequest;
import com.irvan.seblakpredator.model.ProfileResponse;
import com.irvan.seblakpredator.model.RegisterRequest;
import com.irvan.seblakpredator.model.RegisterResponse;
import com.irvan.seblakpredator.model.SecondMenuResponse;
import com.irvan.seblakpredator.model.UpdateProfileRequest;
import com.irvan.seblakpredator.model.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/login.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Headers("Content-Type: application/json")
    @POST("auth/register.php")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @Headers("Content-Type: application/json")
    @POST("auth/forgot-password.php")
    Call<ForgetPassResponse> forgetpassword(@Body ForgetPassRequest forgetPassRequest);

    @POST("auth/updateprofilemobile.php")
    Call<UpdateProfileResponse> updateProfile(
            @Header("Authorization") String token,
            @Body UpdateProfileRequest request
    );
    @GET("auth/profilemobile.php")
    Call<ProfileResponse> getProfile(
            @Header("Authorization") String token
    );

    @GET("menu/toppings.php")
    Call<SecondMenuResponse> getToppings();
}
