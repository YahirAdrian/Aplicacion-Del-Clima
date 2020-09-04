package com.example.appclima.Api.ApiServices;

import com.example.appclima.Models.City;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;

public interface WeatherService {

    //http://api.openweathermap.org/data/2.5/  weather?q=Seville,es&appid=aa816796c4fd226469679434101f7b56
    //http://api.openweathermap.org/data/2.5/weather?lat=-20.48302&lon=86.952386&appid=e9d90ac695e1febbafd6407d9a71ac76

    @GET("weather")
    Call<City> getCity(@Query("q") String city, @Query("appid") String key);

    @GET("weather")
    Call<City> getCityCelcius(@Query("q") String city, @Query("appid") String key, @Query("units") String value);

    @GET("weather")
    Call<City> getCityUbication(@Query("lat") double latitude, @Query("lon") double longitude, @Query("appid") String key, @Query("units") String units, @Query("lang") String language);
}
