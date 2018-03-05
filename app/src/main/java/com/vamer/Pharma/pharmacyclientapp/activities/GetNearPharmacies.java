package com.vamer.Pharma.pharmacyclientapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vamer.Pharma.pharmacyclientapp.AppController;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.adapter.PharmacyAdapter;
import com.vamer.Pharma.pharmacyclientapp.model.Pharmacy;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.rmiri.skeleton.Master.IsCanSetAdapterListener;


public class GetNearPharmacies extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PharmacyAdapter pharmacyAdapter;
    private ArrayList<Pharmacy> dataObjects = new ArrayList<>();
    String GPS_Latitude, GPS_Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);
        GPS_Latitude = getIntent().getStringExtra("GPS_Latitude");
        GPS_Longitude = getIntent().getStringExtra("GPS_Longitude");

        // Toolbar
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initial recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Set adapter in recyclerView
        pharmacyAdapter = new PharmacyAdapter(getApplicationContext(), dataObjects, recyclerView, new IsCanSetAdapterListener() {
            @Override
            public void isCanSet() {
                recyclerView.setAdapter(pharmacyAdapter);
            }
        });


        /*// After 5 second get data fake
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataObjects = new GeneratesDataFake().generateDataFake();
                pharmacyAdapter.addMoreDataAndSkeletonFinish(dataObjects);
            }
        }, 5000);*/
        getAvailablePharmacies();
    }

    public void getAvailablePharmacies() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("GPS_Latitude", GPS_Latitude);
        postParam.put("GPS_Longitude", GPS_Longitude);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "Pharmacy/GetNearPharmacies", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String Status = response.getString("Status");
                            if (Status.equals(AppConstants.success)) {
                                JSONArray jsonArray = response.getJSONArray("Result");
                                dataObjects.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Pharmacy pharmacy = new Pharmacy();
                                    pharmacy.setPharmacyName(jsonObject.getString("PharmacyName"));
                                    pharmacy.setBranchID(jsonObject.getString("BranchID"));
                                    pharmacy.setPharmacyDesc(jsonObject.getString("PharmacyDesc"));
                                    pharmacy.setPharmacyLogo(jsonObject.getString("PharmacyLogo"));
                                    pharmacy.setPharmacyRate(jsonObject.getString("PharmacyRate"));
                                    pharmacy.setDeliveryDuration(jsonObject.getString("DeliveryDuration"));
                                    pharmacy.setDistance(jsonObject.getString("Distance"));
                                    pharmacy.setDeliveryZone_KiloMeter(jsonObject.getString("DeliveryZone_KiloMeter"));
                                    pharmacy.setDeliveryWorkinghrsFrom(jsonObject.getString("DeliveryWorkinghrsFrom"));
                                    pharmacy.setDeliveryWorkinghrsTo(jsonObject.getString("DeliveryWorkinghrsTo"));
                                    dataObjects.add(pharmacy);
                                }
                                pharmacyAdapter.addMoreDataAndSkeletonFinish(dataObjects);

                             //   locationAdapter.notifyDataSetChanged();
                                //  LocationsRecylcerview.setAdapter(locationAdapter);

                            } else {
                                Toast.makeText(GetNearPharmacies.this, "There is an error try again later ", Toast.LENGTH_SHORT).show();
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


}
