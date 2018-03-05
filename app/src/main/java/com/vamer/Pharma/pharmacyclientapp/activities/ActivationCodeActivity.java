package com.vamer.Pharma.pharmacyclientapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mukesh.OtpView;
import com.vamer.Pharma.pharmacyclientapp.AppController;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;
import com.vamer.Pharma.pharmacyclientapp.util.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


/**
 * Created by Ahmed.Khames on 1/24/2018.
 */

public class ActivationCodeActivity extends AppCompatActivity {
    Button btnResend;
    Button btnVerify;
    Chronometer chronometer_timer=null;
    private int mPromptCount = 0;
    long elapsedMillis;
    OtpView otpView;
    String Code;
    PreferenceHelper pr;
    String phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Button btnVerify;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirmation);
         pr=PreferenceHelper.getPrefernceHelperInstace();
        phone  = getIntent().getStringExtra("MobileNo");
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //  setupWindowAnimations();
        btnResend=findViewById(R.id.resend_button);

        otpView=findViewById(R.id.otp_view);
        btnResend.setEnabled(false);
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer_timer.setBase(SystemClock.elapsedRealtime());
                chronometer_timer.start();
               // String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
               // getIntent().getBundleExtra("MobileNo");
                verifyMyMobile(phone);

            }
        });
        chronometer_timer=findViewById(R.id.chronometer_timer);
        chronometer_timer.setBase(SystemClock.elapsedRealtime());
        chronometer_timer.start();
        chronometer_timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                calcualteElapsedTime();
            }
        });

        btnVerify=findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(otpView.getOTP().length()==4){
         if(

                 otpView.getOTP().equals(pr.getString(ActivationCodeActivity.this,PreferenceHelper.USER_CODE_VERIFCATION,""))) {

             verifyMyMobile(phone);
         }
           //  startActivity(new Intent(ActivationCodeActivity.this,CompleteProfileActivity.class));}
         else{
             Toasty.error(ActivationCodeActivity.this, "Code Is In Correct.", Toast.LENGTH_SHORT, true).show();
         }

        }
    else
        {
            Toasty.info(ActivationCodeActivity.this, "You have to enter 4 digits code.", Toast.LENGTH_SHORT, true).show();

        }


    }
});
    }

    private void calcualteElapsedTime() {
        int seconds=0;
        elapsedMillis = SystemClock.elapsedRealtime() - chronometer_timer.getBase();
         seconds = (int) (elapsedMillis / 1000) % 60 ;
        if(seconds==59) {
            btnResend.setEnabled(true);
           // mPauseButton.setVisibility(View.GONE);
            chronometer_timer.stop();
            chronometer_timer.setBase(SystemClock.elapsedRealtime());

            //timeWhenPaused = 0;
         //   mRecordingPrompt.setText(getString(R.string.record_prompt));
            // getActivity().stopService(intent);
          //  getActivity().stopService(intent);
            /*Toast.makeText(getActivity(), "Only 60 seconds Per record permitted " + elapsedMillis,
                    Toast.LENGTH_SHORT).show();*/
        }
    }

    private void setupWindowAnimations() {
        /*Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);

        getWindow().setExitTransition(slide);*/
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);
    }
    public void verifyMyMobile(String phone_number) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("MobileNo", phone_number);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.5:123/api/Customer/RegisterCustomer", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {

                            String Status = response.getString("Status");
                            JSONObject Result= response.getJSONObject("Result");
                            if(Status .equals(AppConstants.success)||Status .equals(AppConstants.CUSTOMER_FOUND)) {
                                //
                            /*if (Status .equals("411")) {
                                Toasty.success(ActivationCodeActivity.this, getResources().getString(R.string.customer_found)+Result.getString("Name"), Toast.LENGTH_SHORT, true).show();
                              //  pr.setString(LoginOrRegisterActivity.this,PreferenceHelper.USER_CODE_VERIFCATION,response.getString("Result"));*//*
                               // startActivity(i);
                            }*/ //else if (Status .equals("201")){

                                Toasty.success(ActivationCodeActivity.this, getResources().getString(R.string.customer_created), Toast.LENGTH_SHORT, true).show();
                                Intent i = new Intent(ActivationCodeActivity.this, CompleteProfileActivity.class);
                              //  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                              //  String s = Result.getString("BirthDate");

                               pr.setCustomerData(ActivationCodeActivity.this, Result.getString("BirthDate"), phone, Result.getString("CustomerToken"), Result.getString("Gender"), Result.getString("Name"));
                               pr.setUserLoggedIn(true,ActivationCodeActivity.this);
                                startActivity(i);
                                finish();
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
private void saveUserData(String Mobile,String Name, String Token,String Gender)
{
    pr.setString(ActivationCodeActivity.this,PreferenceHelper.CUSTOMER_MOBILE,Mobile);
    pr.setString(ActivationCodeActivity.this,PreferenceHelper.CUSTOMER_NAME,Name);
    pr.setString(ActivationCodeActivity.this,PreferenceHelper.CUSTOMER_TOKEN,Token);
    pr.setString(ActivationCodeActivity.this,PreferenceHelper.CUSTOMER_GENDER,Gender);

}
}
