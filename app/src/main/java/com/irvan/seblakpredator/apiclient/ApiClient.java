package com.irvan.seblakpredator.apiclient;

import android.content.Context;
import android.util.Log;

import com.irvan.seblakpredator.model.TokenManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    // Ambil token dari TokenManager
                    String token = TokenManager.getToken(context).trim();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .method(original.method(), original.body())
                            .build();

                    // Debug log
                    Log.d("API_REQUEST", "URL: " + request.url());
                    Log.d("API_REQUEST", "Headers: " + request.headers());

                    Response response = chain.proceed(request);

                    Log.d("API_RESPONSE", "Code: " + response.code());
                    Log.d("API_RESPONSE", "Message: " + response.message());

                    return response;
                })
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.9/seblak-predator-web-app/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
