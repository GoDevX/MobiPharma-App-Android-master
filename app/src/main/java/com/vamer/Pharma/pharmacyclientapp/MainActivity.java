package com.vamer.Pharma.pharmacyclientapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private String[] pageTitle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /*try {
            addMarketPlace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        pageTitle = new String[]{this.getResources().getString(R.string.add_order), getResources().getString(R.string.my_orders), "Fragment 3"};
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 2; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.MODE_FIXED);

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fr1) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.fr2) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.go) {
            Intent intent = new Intent(this, DesActivity.class);
            intent.putExtra("string", "Go to other Activity by NavigationView item cliked!");
            startActivity(intent);
        } else if (id == R.id.close) {
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void addMarketPlace() throws JSONException {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String TAG = "ASI";
        final Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Mobile", "123454");

        Log.e("ASITRACKAddMarketParams", postParam.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                 "http://192.168.1.5:123/api/Customer/RegisterCustomer", getParams(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                        try {
                            String flag = response.getString("status");
                            if (flag.equals("approval_required")) {
                                String Url = response.getString("approval_link");
                             //Intent i = new Intent(AddMarcketPlaceActivity.this, PaymentMarketPlaceWebView.class);
                             //i.putExtra("Url", Url);
                             //startActivityForResult(i, 1);
                                finish();
                                // Toasty.success(AddMarcketPlaceActivity.this, "Place added successfully", Toast.LENGTH_LONG).show();

                            } else if (flag.equals("402")) {

                            //    Toasty.info(AddMarcketPlaceActivity.this, "wallet isn't have enough money", Toast.LENGTH_LONG).show();

                            } else {
                               // Toast.makeText(AddMarcketPlaceActivity.this, "There is an error try again later", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                String statusCode = String.valueOf(error.networkResponse.statusCode);

                Log.d(statusCode, ": "+statusCode);
                //get response body and parse with appropriate encoding
                if(error.networkResponse.data!=null) {
                    try {
                        String body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }
        })



        {


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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
      AppController.getInstance().addToRequestQueue(jsonObjReq, "TAG");
    }

    private JSONObject getParams() throws JSONException{
        JSONObject postParam = new JSONObject();
        postParam.put("Mobile", "123544");
        return   postParam;

    }


    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
