package com.irvan.seblakpredator.apiclient;
import com.irvan.seblakpredator.model.LoginResponse;
import com.irvan.seblakpredator.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/login.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Headers("Content-Type: application/json")
    @POST("auth/register.php")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
