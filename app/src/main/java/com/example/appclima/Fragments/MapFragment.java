package com.example.appclima.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appclima.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, AlertDialog.OnClickListener, LocationListener, GoogleMap.OnMarkerDragListener {

    private View rootView;
    private LatLng latitude_longitude;
    private TextView textLocation;
    private MapView mapView;
    private FloatingActionButton floatingActionButton;
    private GoogleMap map;
    private LocationManager locationManager;
    private Location currentLocation;
    private Marker marker;
    private CameraPosition cameraPosition;
    private Geocoder geocoder;
    private List<Address> address;
    private Button buttonOk;

    private DataListener callback;

    public MapFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        textLocation = rootView.findViewById(R.id.textViewLocation);
        buttonOk = (Button) rootView.findViewById(R.id.botonListo);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude_longitude != null)
                {
                    callback.sendData(latitude_longitude);
                }else{
                    callback.sendData(new LatLng(-89.21, 18.32));
                }

            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mapView = (MapView) view.findViewById(R.id.mapview);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            callback = (DataListener) context;
        }catch (Exception e){
            throw new ClassCastException(context.toString() + "debe de implementar DataListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //map.setMyLocationEnabled(true);
        //map.getUiSettings().setMyLocationButtonEnabled(false);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

        map.setOnMarkerDragListener(this);

        geocoder = new Geocoder(getContext(), Locale.getDefault());
    }

    private boolean isGpsEnabled() {
        try {
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);

            if (gpsSignal == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlertInfo() {
        new AlertDialog.Builder(getContext()).setTitle("Señal gps")
                .setMessage("No tienes activado el gps, porfavor, activalo")
                .setPositiveButton("Activar", this)
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    public void onClick(View v) {
        if (!this.isGpsEnabled()) {
            showAlertInfo();
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //PERMISOS  POR SEGURIDAD DE ACCESO AL GPS
                Toast.makeText(getContext(), "Primero debes habilitar los permisos de ubicaicon", Toast.LENGTH_LONG).show();
                Intent intentPermisos = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +getActivity().getPackageName()));
                intentPermisos.addCategory(Intent.CATEGORY_DEFAULT);
                //intentPermisos.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentPermisos);
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location == null)
            {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            currentLocation = location;

            if(currentLocation != null)
            {
                createOrUpdateMarkerByLocation(location);
                zoomToLocation(location);
            }else{
                Toast.makeText(getContext(), "No se ha activado los permisos de ubicaion", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which)
        {
            case DialogInterface.BUTTON_POSITIVE:
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(getContext(), "Sin activar", Toast.LENGTH_LONG).show();
                break;

                default:
                    Toast.makeText(getContext(), "Lorem", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location){}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private void createOrUpdateMarkerByLocation(Location location)
    {
        if(marker == null)
        {
            marker = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(true));

        }else{
            marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void zoomToLocation(Location location)
    {
        cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                .tilt(30).zoom(10).bearing(0).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        textLocation.setText("Ubicacion...");
    }

    @Override
    public void onMarkerDrag(Marker marker) {}

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;

        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String direccion = address.get(0).getAddressLine(0);
        String postal = address.get(0).getPostalCode();

        String cadena;

        if (direccion != null) {
            if (postal == null) {
                cadena = direccion;
            } else {
                cadena = direccion + "," + postal;
            }
        } else {
            cadena = "Ubicacion no valida";
        }

        textLocation.setText(cadena);

        latitude_longitude = marker.getPosition();
    }

    public interface DataListener{
        void sendData(LatLng coordinates);
    }


}
