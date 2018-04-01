package com.godevx.dawadoz.main.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.godevx.dawadoz.R;
import com.godevx.dawadoz.main.AppController;
import com.godevx.dawadoz.main.activities.HomeActivity;
import com.godevx.dawadoz.main.activities.PickMarketPlaceAddressActivity;
import com.godevx.dawadoz.main.model.Location;
import com.godevx.dawadoz.main.util.AppConstants;
import com.godevx.dawadoz.main.util.PreferenceHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class AddLocationFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View v;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PreferenceHelper pr;
    MarkerOptions markerOptions;
    Location location;
    MapView mapView;
    GoogleMap map;
    String GPS_Latitude = "0";
    boolean edit_location = false;
    String GPS_Longitude = "0";
    EditText textViewlocation_name;
    EditText textViewAddress;
    EditText textViewBuilding_no;
    EditText textViewFloor;
    Button btnSaveLocation;
    Button btnEditLocationOnMap;
    EditText textViewAppartment;
    private String locationAddress = "";
    private String locationID = "";
    private String locationName = "";

    private String locationBuildingNumbe = "";
    private String locationFloorNumber = "";
    private String locationAppartmentNumber = "";

    GoogleMap googleMap;

    public AddLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddLocationFragment newInstance(Location location) {

        AddLocationFragment fragment = new AddLocationFragment();
        if (location != null) {
            Bundle args = new Bundle();
            args.putString("locationAddress", location.getLocationAddress());
            args.putString("locationID", location.getLocationID());
            args.putString("locationName", location.getLocationName());
            args.putString("locationBuildingNumbe", location.getLocationBuildingNumber());
            args.putString("locationFloorNumber", location.getLocationFloorNumber());
            args.putString("locationAppartmentNumber", location.getLocationAppartmentNumber());
            args.putString("locationAppartmentNumber", location.getLocationAppartmentNumber());
            args.putString("GPS_Latitude", location.getGPS_Latitude());
            args.putString("GPS_Longitude", location.getGPS_Longitude());
            fragment.setArguments(args);

        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            locationAddress = getArguments().getString("locationAddress");
            locationID = getArguments().getString("locationID");
            locationName = getArguments().getString("locationName");
            locationBuildingNumbe = getArguments().getString("locationBuildingNumbe");
            locationFloorNumber = getArguments().getString("locationFloorNumber");
            locationAppartmentNumber = getArguments().getString("locationAppartmentNumber");
            GPS_Latitude = getArguments().getString("GPS_Latitude");
            GPS_Longitude = getArguments().getString("GPS_Longitude");
            edit_location = true;
        }

    }

    private void initializeEditTextFromArguments() {
        textViewlocation_name.setText(locationName);
        textViewAddress.setText(locationAddress);
        textViewBuilding_no.setText(locationBuildingNumbe);
        textViewFloor.setText(locationFloorNumber);
        textViewAppartment.setText(locationAppartmentNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_location, container, false);
        pr = PreferenceHelper.getPrefernceHelperInstace();
        btnEditLocationOnMap = v.findViewById(R.id.btnEditLocation);

        btnEditLocationOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocationFromMap();

            }
        });
        final Toolbar toolbar = (Toolbar) v.findViewById(R.id.anim_toolbar);

        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }

        });

        mapView = v.findViewById(R.id.mapfragment);

        mapView.onCreate(savedInstanceState);

        mapView.setClickable(false);

        mapView.getMapAsync(this);
        textViewAddress = v.findViewById(R.id.address);
        textViewlocation_name = v.findViewById(R.id.location_name);
        textViewBuilding_no = v.findViewById(R.id.building_no);
        textViewFloor = v.findViewById(R.id.Floor);
        textViewAppartment = v.findViewById(R.id.Appartment);
        btnSaveLocation = v.findViewById(R.id.btnSaveLocation);

        initializeEditTextFromArguments();

        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    initializeLocationFromInputEditTexts();
                    if (edit_location) {
                        editMyLocation(locationID);
                    } else {
                        AddMyLocation(location);
                    }
                }
            }
        });
        //textViewAddress.setText(Address);
        return v;
    }

    public void setUpMap() {
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
       /*
       //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
       */


        // Updates the location and zoom of the MapView
        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);*/
        LatLng location = new LatLng(Double.valueOf(GPS_Latitude), Double.valueOf(GPS_Longitude));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title("location");
        map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        setUpMap();

    }

    @Override
    public void onResume() {
        mapView.onResume();


        super.onResume();


    }

    private boolean validateInputs() {
        if (textViewAddress.getText().toString().trim().length() == 0) {
            textViewAddress.setError(getResources().getString(R.string.address_error));
            return false;
        }
        if (textViewBuilding_no.getText().toString().trim().length() == 0) {
            textViewBuilding_no.setError(getResources().getString(R.string.Building_number_error));
            return false;
        }
        if (textViewlocation_name.getText().toString().trim().length() == 0) {
            textViewlocation_name.setError(getResources().getString(R.string.location_name_error));
            return false;
        }
        if (textViewFloor.getText().toString().trim().length() == 0) {
            textViewFloor.setError(getResources().getString(R.string.Floor_error));
            return false;
        }
        if (GPS_Latitude.trim().equals("0") && GPS_Longitude.trim().equals("0")) {
            Toast.makeText(getActivity(), "you have to set  your location on the map first", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (textViewAppartment.getText().toString().trim().length() == 0) {
            textViewAppartment.setError(getResources().getString(R.string.Apartment_error));
            return false;
        } else
            return true;
    }

    private void editMyLocation(String location) {
        final String TAG = "ASI";
        final Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("CustomerToken", pr.getString(getActivity(), PreferenceHelper.CUSTOMER_TOKEN, ""));
        postParam.put("LocationID", location);
        postParam.put("LocationName", textViewlocation_name.getText().toString());
        postParam.put("Address", textViewAddress.getText().toString());
        postParam.put("BuildingNo", textViewBuilding_no.getText().toString());
        postParam.put("FloorNo", textViewFloor.getText().toString());
        postParam.put("AppartmentNo", textViewAppartment.getText().toString());
        postParam.put("GPS_Latitude", GPS_Latitude);
        postParam.put("GPS_Longitude", GPS_Longitude);

        Log.e("Posts Data ", postParam.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConstants.API_BASE_URL + "CustomerLocations/UpdateLocation/", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                        try {

                            String flag = response.getString("Status");
                            if (flag.equals(AppConstants.success)) {
                                Toasty.success(getActivity(), getResources().getString(R.string.location_deleted), Toast.LENGTH_SHORT, true).show();
                                getActivity().onBackPressed();

                            } else {
                                Toast.makeText(getActivity(), "There is an error try again later ", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                // progressDialog.dismiss();

            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic YWhtZWQ6YWhtZWQ=");
                return headers;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "TAG");
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    public void getLocationFromMap() {

        Intent intent = new Intent(getActivity(), PickMarketPlaceAddressActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


                GPS_Latitude = String.valueOf(data.getDoubleExtra("piclat", 0));

                GPS_Longitude = String.valueOf(data.getDoubleExtra("piclang", 0));

                textViewAddress.setText(data.getStringExtra(""));
                map.clear();
                setUpMap();
                // textViewAddress.setText(Address);
            }
        }
    }

    protected void RefreshMap() {

    }

    public void AddMyLocation(final Location myLocation) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(PreferenceHelper.CUSTOMER_TOKEN, pr.getString(getActivity(), PreferenceHelper.CUSTOMER_TOKEN, ""));
        postParam.put("LocationName", myLocation.getLocationName());
        postParam.put("Address", myLocation.getLocationAddress());
        postParam.put("BuildingNo", myLocation.getLocationBuildingNumber());
        postParam.put("FloorNo", myLocation.getLocationFloorNumber());
        postParam.put("AppartmentNo", myLocation.getLocationAppartmentNumber());
        postParam.put("GPS_Latitude", myLocation.getGPS_Latitude());
        postParam.put("GPS_Longitude", myLocation.getGPS_Longitude());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "CustomerLocations/AddLocation", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String Status = response.getString("Status");
                            if (Status.equals(AppConstants.success)) {
                                //alertDialog.dismiss();
                                Toasty.success(getActivity(), getResources().getString(R.string.location_added), Toast.LENGTH_SHORT, true).show();
                                getActivity().onBackPressed();
                                //  getMyLocations();


                            } else {
                                Toasty.error(getActivity(), getResources().getString(R.string.location_not_added), Toast.LENGTH_SHORT, true).show();
                            }
                            //Todo
                            // saveUserData(Result.getString("MobNo"),Result.getString("Name"),Result.getString("Token"),Result.getString("Gender"));

                            // Toasty.error(LoginOrRegisterActivity.this,getResources().getString(R.string.verification_code_not_sent) , Toast.LENGTH_SHORT, true).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic YWhtZWQ6YWhtZWQ=");
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "tag");
    }

    private void initializeLocationFromInputEditTexts() {


        location = new Location(textViewlocation_name.getText().toString(), textViewAddress.getText().toString(), textViewBuilding_no.getText().toString(), textViewFloor.getText().toString(), textViewAppartment.getText().toString(), GPS_Latitude, GPS_Longitude);

    }

}

