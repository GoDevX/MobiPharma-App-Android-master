package com.godevx.dawadoz.main.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.godevx.dawadoz.R;
import com.godevx.dawadoz.main.AppController;
import com.godevx.dawadoz.main.activities.HomeActivity;
import com.godevx.dawadoz.main.activities.PickMarketPlaceAddressActivity;
import com.godevx.dawadoz.main.adapter.MyLocationsAdapter;
import com.godevx.dawadoz.main.model.Location;
import com.godevx.dawadoz.main.util.AppConstants;
import com.godevx.dawadoz.main.util.GPSTracker;
import com.godevx.dawadoz.main.util.PreferenceHelper;
import com.godevx.dawadoz.main.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyLocationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyLocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Location> locationslist = new ArrayList<>();
    MyLocationsAdapter myLocationsAdapter;
    PreferenceHelper pr;
    RecyclerView my_locations_recycler_view;
    View v;
    String Address, Building_no, Floor, Appartment, LocationName;
    String GPS_Latitude;
    String GPS_Longitude;
    Button btnaddLocation;
    EditText textViewlocation_name;
    EditText textViewAddress;
    EditText textViewBuilding_no;
    EditText textViewFloor;
    EditText textViewAppartment;

    Button btnSaveLocation;
    Location location;
    GPSTracker gpsTracker;
    AlertDialog alertDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MyLocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLocationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLocationsFragment newInstance(String param1, String param2) {
        MyLocationsFragment fragment = new MyLocationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_locations, container, false);
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            GetAddress(getActivity(), latitude, longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();

        }
        final Toolbar toolbar = (Toolbar) v.findViewById(R.id.anim_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnaddLocation = v.findViewById(R.id.btnAdd);
        btnaddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        AddLocationFragment.newInstance(null),
                        ((HomeActivity) (getContext())), Utils.ADD_LOCATION_FRAGMENT,
                        Utils.AnimationType.SLIDE_LEFT);


            }
        });
        pr = PreferenceHelper.getPrefernceHelperInstace();

        //final Toolbar toolbar = (Toolbar) v.findViewById(R.id.anim_toolbar);

       /* ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        location = new Location("", "", "", "", "", "", "");

        my_locations_recycler_view = v.findViewById(R.id.my_locations_recycler_view);

        my_locations_recycler_view.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        my_locations_recycler_view.setLayoutManager(linearLayoutManager);

        myLocationsAdapter = new MyLocationsAdapter(getActivity(), locationslist, new MyLocationsAdapter.MyAdapterListener() {
            @Override
            public void editLocation(View v, int position) {
                //editMyLocation(locationslist.get(position));
                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        AddLocationFragment.newInstance(locationslist.get(position)),
                        ((HomeActivity) (getContext())), Utils.ADD_LOCATION_FRAGMENT,
                        Utils.AnimationType.SLIDE_LEFT);

                // editMyLocation(locationslist.get(position));
            }

            @Override
            public void deleteLocation(View v, final int position) {
                new MaterialStyledDialog.Builder(getActivity())
                        .setTitle("ohhhh!")
                        .setDescription("Are you sure to delete location?")
                        .withDialogAnimation(true)
                        .setPositiveText(R.string.dialog_action_ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteMyLocation(locationslist.get(position));
                                dialog.dismiss();

                            }
                        })
                        .setNegativeText(R.string.dialog_action_cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();

                            }
                        })

                        //.withDialogAnimation(true, Duration.SLOW)
                        .show();


            }


        });
        my_locations_recycler_view.setAdapter(myLocationsAdapter);

        //  getMyLocations();

        return v;
    }

    private void editMyLocation(Location location) {
        final String TAG = "ASI";
        final Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("CustomerToken", pr.getString(getActivity(), PreferenceHelper.CUSTOMER_TOKEN, ""));
        postParam.put("LocationID", location.getLocationID());

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
                                getMyLocations();

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

    private void deleteMyLocation(Location location) {
        final String TAG = "ASI";
        final Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("CustomerToken", pr.getString(getActivity(), PreferenceHelper.CUSTOMER_TOKEN, ""));
        postParam.put("LocationID", location.getLocationID());

        Log.e("Posts Data ", postParam.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConstants.API_BASE_URL + "CustomerLocations/DeleteLocation/", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                        try {

                            String flag = response.getString("Status");
                            if (flag.equals(AppConstants.success)) {
                                Toasty.success(getActivity(), getResources().getString(R.string.location_deleted), Toast.LENGTH_SHORT, true).show();
                                getMyLocations();

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

    public void getMyLocations() {
        final String TAG = "ASI";
        final Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("CustomerToken", pr.getString(getActivity(), PreferenceHelper.CUSTOMER_TOKEN, ""));
        Log.e("Posts Data ", postParam.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "CustomerLocations/GetMyLocations/", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                        try {
                            String flag = response.getString("Status");
                            if (flag.equals(AppConstants.success)) {
                                locationslist.clear();
                                JSONArray jsonArray = response.getJSONArray("Result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Location LocationModel = new Location(jsonObject.getString("Location_ID"), jsonObject.getString("LocationName"), jsonObject.getString("Address"), jsonObject.getString("BuildingNo"), jsonObject.getString("FloorNo"), jsonObject.getString("AppartmentNo"), jsonObject.getString("GPS_Latitude"), jsonObject.getString("GPS_Longitude"));
                                    locationslist.add(LocationModel);
                                }
                                myLocationsAdapter.notifyDataSetChanged();

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

    @Override
    public void onResume() {
        super.onResume();

        getMyLocations();
    }

    public void GetAddress(Activity activity, double lat, final double lng) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&sensor=true&language=en", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                List<Address> res = new ArrayList<Address>();
                try {
                    double lon;
                    double lat;

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = (JSONArray) jsonObject.get("results");
                    lon = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    GPS_Longitude = String.valueOf(lon);
                    lat = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    GPS_Latitude = String.valueOf(lat);

                    Address = array.getJSONObject(0).getString("formatted_address");


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        // Adding request to request queue
        int socketTimeout = 50000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "tag");
    }

    private void show_add_dialog_location() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_location, null);
        dialogBuilder.setView(dialogView);
        textViewAddress = dialogView.findViewById(R.id.address);
        textViewlocation_name = dialogView.findViewById(R.id.location_name);
        textViewBuilding_no = dialogView.findViewById(R.id.building_no);
        textViewFloor = dialogView.findViewById(R.id.Floor);
        textViewAppartment = dialogView.findViewById(R.id.Appartment);
        btnSaveLocation = dialogView.findViewById(R.id.btnSaveLocation);
        textViewAddress.setText(Address);
        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    initializeLocationFromInputEditTexts();
                    AddMyLocation(location);
                }

            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();

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
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "Customer/AddLocation", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String Status = response.getString("Status");
                            if (Status.equals(AppConstants.success)) {
                                alertDialog.dismiss();
                                Toasty.success(getActivity(), getResources().getString(R.string.location_added), Toast.LENGTH_SHORT, true).show();
                                getMyLocations();


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

    public void getLocationFromMap() {

        Intent intent = new Intent(getActivity(), PickMarketPlaceAddressActivity.class);
        startActivityForResult(intent, 1);
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
        if (textViewAppartment.getText().toString().trim().length() == 0) {
            textViewAppartment.setError(getResources().getString(R.string.Apartment_error));
            return false;
        } else
            return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Address = data.getStringExtra("address");

                GPS_Latitude = String.valueOf(data.getDoubleExtra("piclat", 0));

                GPS_Longitude = String.valueOf(data.getDoubleExtra("piclang", 0));

                textViewAddress.setText(Address);
            }
        }
    }

    private void initializeLocationFromInputEditTexts() {

        Address = textViewAddress.getText().toString();
        Building_no = textViewBuilding_no.getText().toString();
        LocationName = textViewlocation_name.getText().toString();
        Floor = textViewFloor.getText().toString();
        Appartment = textViewAppartment.getText().toString();
        location = new Location(LocationName, Address, Building_no, Floor, Appartment, GPS_Latitude, GPS_Longitude);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
