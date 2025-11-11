package com.irvan.seblakpredator.apiclient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static final String Url = "";
    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Url) // Alamat API dari pemimpin kelompok
                    .addConverterFactory(GsonConverterFactory.create()) // Supaya bisa parsing JSON otomatis
                    .build();
        }
        return retrofit;
    }
}
