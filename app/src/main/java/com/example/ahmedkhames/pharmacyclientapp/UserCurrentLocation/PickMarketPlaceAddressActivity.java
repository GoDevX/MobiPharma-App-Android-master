package com.example.ahmedkhames.pharmacyclientapp.UserCurrentLocation;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ahmedkhames.pharmacyclientapp.AppController;
import com.example.ahmedkhames.pharmacyclientapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class PickMarketPlaceAddressActivity extends AppCompatActivity implements   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener {


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
String address;
    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 1; // 10 meters

    MapView mMapView;
    private GoogleMap googleMap;
    private LatLng mPosition;
    double piclat;
    double piclang;
    private TextView tv_current_location;
    FancyButton btn_get_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_market_place_address);
        tv_current_location= (TextView) findViewById(R.id.tv_current_location);
        mMapView = (MapView)findViewById(R.id.mapView);
        btn_get_address=findViewById(R.id.btn_get_address);
        btn_get_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putExtra("address", address);
                setResult(RESULT_OK, output);
                finish();
            }
        });
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        if (checkPlayServices())
        {
            buildGoogleApiClient();
            createLocationRequest();
        }
        try
        {
            MapsInitializer.initialize(getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap mMap)
            {
                googleMap = mMap;

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // For showing a move to my location button
                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
                {
                    @Override
                    public void onCameraMove()
                    {

                    }
                });
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {

                        mPosition = googleMap.getCameraPosition().target;

                        GetAddress(mPosition.latitude,mPosition.longitude);

                    }
                });
                googleMap.setMyLocationEnabled(true);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.i("ASITRACK", "Connection failed, ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0)
    {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Once connected with google api, get the location
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            displayLocation();
        else
            showGPSDisabledAlertToUser();



    }



    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }




    /**
     * get address from  Lat and lng using google map api using
     *
     * @param lat,lng
     */
    private void GetAddress(double lat, final double lng)
    {
//TODO language preferencces
        String lang="en";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&sensor=true&language=" + lang, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Locale.setDefault(new Locale("en"));

                List<Address> res = new ArrayList<Address>();
                try
                {
                    double lon;
                    double lat ;
                    String name = "";
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = (JSONArray) jsonObject.get("results");
                    lon = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    lat = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    name = array.getJSONObject(0).getString("formatted_address");
                    Log.e("FOR MATED ADDRES IS ==", name);
                    Address addr = new Address(Locale.getDefault());
                        address=name;
                        tv_current_location.setText(name);
                        piclat =lat;
                        piclang = lon;



                }
                catch (JSONException e)
                {
                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {


            }
        });
        // Adding request to request queue
        int socketTimeout = 50000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq, "tag");
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(PickMarketPlaceAddressActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, PickMarketPlaceAddressActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(PickMarketPlaceAddressActivity.this,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                PickMarketPlaceAddressActivity.this.finish();
            }
            return false;
        }
        return true;
    }
    private void displayLocation()
    {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
            // For dropping a marker at a point on the Map
        if(mLastLocation!=null) {
            LatLng sydney = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_currentlocation));


            //For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }

    }


    @Override
    public void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
        checkPlayServices();


    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void chooseThisAddress(View view) {





      






       /* Intent intent=new Intent(PickMarketPlaceAddressActivity.this,AddMarcketPlaceActivity.class)*/;
       /* intent.putExtra("address",tv_current_location.getText().toString());*/
       /* intent.putExtra("lat",String.valueOf(piclat));*/
       /* intent.putExtra("lng",String.valueOf(piclang));*/
       /* startActivity(intent);*/




    }
}
