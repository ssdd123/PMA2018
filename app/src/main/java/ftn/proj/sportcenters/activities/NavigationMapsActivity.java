package ftn.proj.sportcenters.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.directionhelpers.FetchURL;
import ftn.proj.sportcenters.directionhelpers.TaskLoadedCallback;

public class NavigationMapsActivity extends AppCompatActivity  implements OnMapReadyCallback, TaskLoadedCallback {
    LocationManager locationManager;
    private GoogleMap mMap;
    private MarkerOptions myLocation, sportCenterLocation,place1, place2;
    Button getDirection;
    private Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_maps);

        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(NavigationMapsActivity.this).execute(getUrl(myLocation.getPosition(), sportCenterLocation.getPosition(), "driving"), "driving");
            }
        });
        //27.658143,85.3199503
        //27.667491,85.3208583


        //requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, android.location.LocationListener.locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,locationListener);
      //  place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
      //  place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");
        //  myLocation = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("I am here!");
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent in = getIntent();
        float latSportCentar = getIntent().getExtras().getFloat("latSportCentar");
        float longgSportCentar = getIntent().getExtras().getFloat("longgSportCentar");
        String nameSportCentar = getIntent().getExtras().getString("nameSportCentar");

        float latMyLocation = getIntent().getExtras().getFloat("latMyLocation");
        float longgMyLocation = getIntent().getExtras().getFloat("longgMyLocation");
        String nameMyLocation = getIntent().getExtras().getString("nameMyLocation");


        LatLng locationSportCentar = new LatLng(latSportCentar, longgSportCentar);
        LatLng locationMyLocation = new LatLng(latMyLocation, longgMyLocation);


        myLocation = new MarkerOptions().position(locationMyLocation).title(nameMyLocation);
        sportCenterLocation = new MarkerOptions().position(locationSportCentar).title(nameSportCentar);

        mMap.addMarker(sportCenterLocation);
        mMap.addMarker(myLocation);


        Log.d("mylog", "Added Markers");
        // mMap.addMarker(place1);// Kada ubacis centar odkomentarisi

        // mMap.addMarker(place2);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}
