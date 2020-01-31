package ftn.proj.sportcenters.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.directionhelpers.TaskLoadedCallback;
import ftn.proj.sportcenters.directionhelpers.FetchURL;



public class CurrentLocationActivity extends AppCompatActivity implements LocationListener{
    public static String tvLongi;
    public static String tvLati;
    TextView tvLatitude;
    TextView tvLongitude;
    LocationManager locationManager;
    private GoogleMap mMap;
    private MarkerOptions myLocation, sportCenterLocation,place1, place2;
    Button getDirection;
    private Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        CheckPermission();

    }


//FIND MY LOCATION
            /* Request updates at startup */
            @Override
            public void onResume() {
                super.onResume();
                getLocation();
            }

            /* Remove the locationlistener updates when Activity is paused */
            @Override
            protected void onPause() {
                super.onPause();
                locationManager.removeUpdates(this);
            }

            public void getLocation() {
                try {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            public void CheckPermission() {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
            }

            @Override
            public void onLocationChanged(Location location) {
              try{
                myLocation = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("I am here!");
                tvLongi = String.valueOf(location.getLongitude());
                tvLati = String.valueOf(location.getLatitude());

                // Stele komentar
                // Getting reference to TextView tv_longitude
                tvLongitude = (TextView) findViewById(R.id.tv_longitude);
                // Getting reference to TextView tv_latitude
                tvLatitude = (TextView) findViewById(R.id.tv_latitude);


                // Setting Current Longitude
                tvLongitude.setText("Longitude:" + tvLongi);
                // Setting Current Latitude
                tvLatitude.setText("Latitude:" + tvLati);



                Intent in = getIntent();
                float lat = getIntent().getExtras().getFloat("lat");
                float longg = getIntent().getExtras().getFloat("longg");
                String name = getIntent().getExtras().getString("name");

                LatLng locationSport = new LatLng(lat, longg);
                LatLng myLocationSSS = new LatLng(lat+5, longg+3);

                myLocation = new MarkerOptions().position(myLocationSSS).title(name);
                sportCenterLocation = new MarkerOptions().position(locationSport).title(name);

                Intent intentSend = new Intent(CurrentLocationActivity.this, NavigationMapsActivity.class);

                intentSend.putExtra("latSportCentar", lat);
                intentSend.putExtra("longgSportCentar", longg);
                intentSend.putExtra("nameSportCentar", name);
                float  lo=  Float.valueOf(tvLongi);
                float  la=  Float.valueOf(tvLati);
                intentSend.putExtra("latMyLocation", la);
                intentSend.putExtra("longgMyLocation", lo);
                intentSend.putExtra("nameMyLocation", "I am here!");
                finish();
                startActivity(intentSend);

              }catch (Exception e){
                  e.printStackTrace();
              }
            }


            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(CurrentLocationActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(this, "Enabled new provider! " + provider,
                        Toast.LENGTH_SHORT).show();
            }


//GET DIRECTION

    }

