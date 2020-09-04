package com.example.appclima.Api;

import com.example.appclima.Api.Deserializers.Deserializer;
import com.example.appclima.Models.City;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

public class API {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit = null;

    public static Retrofit getApi()
    {
        if (retrofit == null)
        {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(City.class, new Deserializer());

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create(builder.create()))
                    .build();
        }

        return retrofit;
    }
}
