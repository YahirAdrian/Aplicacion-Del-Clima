package com.example.appclima.Api.Deserializers;

import com.example.appclima.Models.City;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Deserializer implements JsonDeserializer<City> {
    //humedad, sensacion termica, presion
    public static float temperature;
    public static float wind;
    public static float realfeel;
    public static int humidity;
    public static int pressaure;
    public static String icon, description;

    @Override
    public City deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int id = json.getAsJsonObject().get("id").getAsInt();
        String name = json.getAsJsonObject().get("name").getAsString();

        //Parar un objeto dentro de otro
        String country = json.getAsJsonObject().get("sys").getAsJsonObject().get("country").getAsString();
        temperature = json.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsFloat();
        realfeel = json.getAsJsonObject().get("main").getAsJsonObject().get("feels_like").getAsFloat();
        wind = json.getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsFloat();
        pressaure = json.getAsJsonObject().get("main").getAsJsonObject().get("pressure").getAsInt();
        description = json.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();
        icon = json.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
        humidity = json.getAsJsonObject().get("main").getAsJsonObject().get("humidity").getAsInt();
        City city = new City(id, name, country, new LatLng(23.44, 64.22));

        return city;
    }
}
