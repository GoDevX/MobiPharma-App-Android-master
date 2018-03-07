package com.vamer.Pharma.pharmacyclientapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vamer.Pharma.pharmacyclientapp.AppController;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.adapter.PharmacyAdapter;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Location;
import com.vamer.Pharma.pharmacyclientapp.model.Pharmacy;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;
import com.vamer.Pharma.pharmacyclientapp.util.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.rmiri.skeleton.Master.IsCanSetAdapterListener;


public class GetNearPharmacies extends AppCompatActivity {
    double sumofProducts;
    private RecyclerView recyclerView;
    private PharmacyAdapter pharmacyAdapter;
    private ArrayList<Pharmacy> dataObjects = new ArrayList<>();
    private List<Product> productArrayList = new ArrayList<>();

    String GPS_Latitude, GPS_Longitude, BuildingNo, AppartmentNo, Address, FloorNo, LocationName;
    String NumberOfItems, TotalPrice, PharmacyID;
    String OrderItemType, ProductID, ItemQuantity, FileData;
    PreferenceHelper pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);
        pr = PreferenceHelper.getPrefernceHelperInstace();
        productArrayList = CenterRepository.getCenterRepository().getListOfProductsInShoppingList();

        ItemQuantity = String.valueOf(productArrayList.size());

        GPS_Latitude = getIntent().getStringExtra("GPS_Latitude");
        GPS_Longitude = getIntent().getStringExtra("GPS_Longitude");
        AppartmentNo = getIntent().getStringExtra("AppartmentNo");
        BuildingNo = getIntent().getStringExtra("BuildingNo");
        Address = getIntent().getStringExtra("Address");
        FloorNo = getIntent().getStringExtra("FloorNo");
        LocationName = getIntent().getStringExtra("LocationName");
        // Toolbar
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sumofProducts = 0;
        for (Product product : productArrayList) {
            if (!product.getQuantity().equals(""))
                sumofProducts += Double.parseDouble(product.getSellMRP()) * Double.parseDouble(product.getQuantity());

        }
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
        }, new PharmacyAdapter.MyAdapterListener() {
            @Override
            public void btnOrderOnClick(View v, int position) {
               /* Pharmacy c = dataObjects.get(position);
                Toast.makeText(GetNearPharmacies.this,c.getPharmacyName(),Toast.LENGTH_SHORT).show();*/
                PharmacyID = dataObjects.get(position).getBranchID();
                if (PharmacyID != null && !PharmacyID.isEmpty()) {
                    try {
                        submitOrder();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

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

        /*try {
            submitOrder();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

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

    public void submitOrder() throws JSONException {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();

        JSONArray OrderItemsArray = new JSONArray();
        for (Product product : productArrayList) {
            JSONObject OrderItemJSON = new JSONObject();
            OrderItemJSON.put("OrderItemType", product.getOrderItemType());
            OrderItemJSON.put("ProductID", product.getProductId());
            OrderItemJSON.put("ItemQuantity", product.getQuantity());
            OrderItemJSON.put("TextItem", product.getItemShortDesc());

            if (!product.getOrderItemType().equals("1") || !product.getOrderItemType().equals("4")) {
                OrderItemJSON.put("FileData", encodeFile(product.getFilePath()));

            } else
                OrderItemJSON.put("FileData", "");
            OrderItemsArray.put(OrderItemJSON);
        }

        JSONObject SubmitOrder = new JSONObject();
        SubmitOrder.put(PreferenceHelper.CUSTOMER_TOKEN, pr.getString(GetNearPharmacies.this, PreferenceHelper.CUSTOMER_TOKEN, ""));
        SubmitOrder.put("PharmacyID", PharmacyID);
        SubmitOrder.put("NumberOfItems", ItemQuantity);
        SubmitOrder.put("TotalPrice", String.valueOf(sumofProducts));
        JSONObject Location = new JSONObject();
        Location.put("LocationName", LocationName);
        Location.put("Address", Address);
        Location.put("BuildingNo", BuildingNo);
        Location.put("FloorNo", FloorNo);
        Location.put("AppartmentNo", AppartmentNo);
        Location.put("GPS_Latitude", GPS_Latitude);
        Location.put("GPS_Longitude", GPS_Longitude);
        SubmitOrder.put("LocationModel", Location);
        SubmitOrder.put("OrderItems", OrderItemsArray);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "Order/SubmitOrder", SubmitOrder,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String Status = response.getString("Status");
                            if (Status.equals(AppConstants.success)) {
                                Toasty.success(GetNearPharmacies.this, getResources().getString(R.string.order_submitted), Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(GetNearPharmacies.this, getResources().getString(R.string.order_not_submitted), Toast.LENGTH_SHORT, true).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "tag");
    }

    private String encodeFile(String selectedPath) {
        byte[] audioBytes;
        try {

            // Just to check file size.. Its is correct i-e; Not Zero
            File audioFile = new File(selectedPath);
            long fileSize = audioFile.length();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(selectedPath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            // Here goes the Base64 string
            String FileBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);
            return FileBase64;

        } catch (Exception e) {
            // DiagnosticHelper.writeException(e);
            return null;
        }

    }


}
