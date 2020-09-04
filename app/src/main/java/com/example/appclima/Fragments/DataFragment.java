package com.example.appclima.Fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appclima.Activities.MainActivity;
import com.example.appclima.Api.API;
import com.example.appclima.Api.ApiServices.WeatherService;
import com.example.appclima.Api.Deserializers.Deserializer;
import com.example.appclima.Models.City;
import com.example.appclima.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFragment extends Fragment {

    private TextView textUbication, textDegrees, textDescription, textData;
    private ImageView weatherIcon;
    WeatherService service;
    private final String BASE_ICON_URL = "http://openweathermap.org/img/w/";

    private final String APIKEY = "aa816796c4fd226469679434101f7b56";
    private Context contexto;

    public DataFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        textUbication = v.findViewById(R.id.textViewUbicationName);
        textDegrees = v.findViewById(R.id.textViewDegrees);
        textData = v.findViewById(R.id.textViewWeatherData);
        weatherIcon = (ImageView) v.findViewById(R.id.imageViewWeatherIcon);
        textDescription = v.findViewById(R.id.textViewWeatherDescription);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        service = API.getApi().create(WeatherService.class);
        contexto = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void reciveCoordinates(LatLng coordinates)
    {
        double latitude = coordinates.latitude;
        double longitude = coordinates.longitude;
        //Aqui nos llegan las coordenadas de el mapa
        //Call<City> cityCall = service.getCityCelcius("Cozumel,Mx", APIKEY, "metric");
        Call<City> cityCall = service.getCityUbication(latitude, longitude, APIKEY, "metric", "es");
        //humedad, sensacion termica, presion
        cityCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                String data = "Humedad: " + Deserializer.humidity + "% \n Sensacion termica:" + Deserializer.realfeel + "ºC \n Presion: "
                        + Deserializer.pressaure + "mb\n Viento: " + Deserializer.wind + "km/h\n" ;
                City city = response.body();
                textDegrees.setText("" + Deserializer.temperature + "ºC");
                textUbication.setText("" + city.getName() + ", " + city.getCountry());
                textDescription.setText(Deserializer.description);
                textData.setText(data);
                Picasso.with(contexto).load("http://openweathermap.org/img/w/" + Deserializer.icon + ".png").error(R.mipmap.ic_launcher).into(weatherIcon);


            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
