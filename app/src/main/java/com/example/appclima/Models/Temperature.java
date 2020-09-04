package com.example.appclima.Models;

import com.google.gson.annotations.SerializedName;

public class Temperature {

    @SerializedName("temp")
    private float temperature;
    private float pressaure;
    private float humity;
    @SerializedName("temp_min")
    private float minTemperature;
    @SerializedName("temp_max")
    private float maxTemperature;

    public Temperature(){}

    public Temperature(float temperature, float pressaure, float humity, float minTemperature, float maxTemperature){
        this.temperature = temperature;
        this.pressaure = pressaure;
        this.humity = humity;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressaure() {
        return pressaure;
    }

    public void setPressaure(float pressaure) {
        this.pressaure = pressaure;
    }

    public float getHumity() {
        return humity;
    }

    public void setHumity(float humity) {
        this.humity = humity;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
}
