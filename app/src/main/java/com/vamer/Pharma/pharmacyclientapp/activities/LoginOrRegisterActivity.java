package com.vamer.Pharma.pharmacyclientapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.vamer.Pharma.pharmacyclientapp.AppController;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.lamudi.phonefield.PhoneInputLayout;
import com.vamer.Pharma.pharmacyclientapp.util.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmed.Khames on 1/24/2018.
 */

public class LoginOrRegisterActivity extends AppCompatActivity {
    EditText txtPhoneNumber;
    Button btnRegister;
    String phone_number;
    PreferenceHelper pr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_login_form);
//        setupWindowAnimations();
        final PhoneInputLayout phoneInputLayout = (PhoneInputLayout) findViewById(R.id.phone_input_layout);
        // Toolbar
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pr = PreferenceHelper.getPrefernceHelperInstace();
        btnRegister = findViewById(R.id.submit_button);
        phoneInputLayout.setHint(R.string.phone_hint);
        phoneInputLayout.setDefaultCountry("EG");
        phoneInputLayout.setHint(R.string.my_number);
        final Handler handler = new Handler();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                // checks if the field is valid
                if (phoneInputLayout.isValid()) {
                    phoneInputLayout.setError(null);
                } else {
                    // set error message
                    phoneInputLayout.setError(getString(R.string.invalid_phone_number));
                    valid = false;
                }

                if (valid) {
                    phone_number = phoneInputLayout.getPhoneNumber();
                    sendVerificationCode(phone_number);
                   /*Intent i = new Intent(LoginOrRegisterActivity.this, ActivationCodeActivity.class);
                    i.putExtra(PreferenceHelper.CUSTOMER_MOBILE,phoneInputLayout.getPhoneNumber());
                    i.putExtra("MobileNo",phone_number);
                    startActivity(i);*/
                    //  startActivity(new Intent(LoginOrRegisterActivity.this,ActivationCodeActivity.class));

                } else {

                    Toasty.error(LoginOrRegisterActivity.this, "Phone Number Is Invalid", Toast.LENGTH_LONG).show();
                }

                // Return the phone number as follows
                String phoneNumber = phoneInputLayout.getPhoneNumber();
                String a = phoneNumber;
            }
        });

    }

    public void sendVerificationCode(final String phone_number) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("MobileNo", phone_number);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.5:123/api/Customer/GenerateVRFCode", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String Status = response.getString("Status");
                            //
                            if (Status.equals("1")) {
                                Toasty.success(LoginOrRegisterActivity.this, getResources().getString(R.string.verification_sent), Toast.LENGTH_SHORT, true).show();
                                Intent i = new Intent(LoginOrRegisterActivity.this, ActivationCodeActivity.class);
                                // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                pr.setString(LoginOrRegisterActivity.this, PreferenceHelper.USER_CODE_VERIFCATION, response.getString("Result"));
                                i.putExtra("MobileNo", phone_number);

                                startActivity(i);
                                finish();
                            } else {
                                Toasty.error(LoginOrRegisterActivity.this, getResources().getString(R.string.verification_code_not_sent), Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        })

        {
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
  /*  private void setupWindowAnimations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }*/


}



