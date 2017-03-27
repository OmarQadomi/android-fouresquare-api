package com.example.myplacesapp.myplacesapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myplacesapp.myplacesapp.Adapters.VenuesAdapter;
import com.example.myplacesapp.myplacesapp.Api.ApiClient;
import com.example.myplacesapp.myplacesapp.Api.ApiInterface;
import com.example.myplacesapp.myplacesapp.DataModels.Venue;
import com.example.myplacesapp.myplacesapp.DataModels.VenuesRespons;
import com.example.myplacesapp.myplacesapp.R;
import com.example.myplacesapp.myplacesapp.SharedClasses.AlertDialogManager;
import com.example.myplacesapp.myplacesapp.SharedClasses.ControllerConstants;
import com.example.myplacesapp.myplacesapp.SharedClasses.ControllerFunctions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyPlacesActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    final int PERMISSION_REQUEST = 100;
    MenuItem searchMenuItem;
    SearchView searchView;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    Location currentLocation;
    boolean isGPSEnabled, isNetworkEnabled;
    RecyclerView recyclerView;
    ArrayList<Venue> venuesList;
    String searchText="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        recyclerView = (RecyclerView) findViewById(R.id.nearByRecycler);
        Button btnShowOnMap=(Button)findViewById(R.id.btn_show_map) ;
        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent=new Intent(NearbyPlacesActivity.this,MapsActivity.class);
                mapIntent.putExtra("MyLatitude", currentLocation.getLatitude());
                mapIntent.putExtra("MyLongitude", currentLocation.getLongitude());
                mapIntent.putParcelableArrayListExtra("venueList",  venuesList);
                startActivity(mapIntent);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions,  ActivityCompat.requestPermissions(MainActivity.this,
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST);
            return;

        }
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                enableAutoManage(this, 0, this).
                addApi(LocationServices.API).
                build();
        mLocationRequest = LocationRequest.create().
                setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).
                setInterval(10 * 1000).
                setFastestInterval(1 * 1000);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

    }

    void FillList(List<Venue> venueList) {
        VenuesAdapter mAdapter = new VenuesAdapter(venueList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /** Create an option menu from res/menu/items.xml */
        getMenuInflater().inflate(R.menu.search, menu);
        searchMenuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchText=s;
                getNearbyVenues(s);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onResume() {
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            new AlertDialogManager() {
                @Override
                public void onPositiveButtonClickListener() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

                @Override
                public void onNegativeButtonClickListener() {

                }
            }.showConfirmationDialog(this, getString(R.string.GPS_settings), getString(R.string.do_you_want_enable_gps), getString(R.string.settings), getString(R.string.cancel));
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        getNearbyVenues("");
        mGoogleApiClient.connect();
        super.onResume();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocation = location;
        getNearbyVenues(searchText);
    }

    private void getNearbyVenues(String s) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<VenuesRespons> call = apiService.getNearbyVenues(currentLocation.getLatitude() + "," + currentLocation.getLongitude(), ControllerFunctions.getPreferences(this, ControllerConstants.TOKEN), ControllerFunctions.getCurrentDate());
        call.enqueue(new Callback<VenuesRespons>() {
            @Override
            public void onResponse(Call<VenuesRespons> call, Response<VenuesRespons> response) {
                venuesList = (ArrayList<Venue>) response.body().getResponse().getVenues();
                FillList(venuesList);
            }

            @Override
            public void onFailure(Call<VenuesRespons> call, Throwable t) {
                t.getLocalizedMessage();
                Snackbar.make(recyclerView, t.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
                // Log error here since request failed
            }
        });
    }
}

