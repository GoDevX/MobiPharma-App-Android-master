package com.vamer.Pharma.pharmacyclientapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vamer.Pharma.pharmacyclientapp.AppController;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vamer.Pharma.pharmacyclientapp.model.Customer;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;
import com.vamer.Pharma.pharmacyclientapp.util.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import es.dmoral.toasty.Toasty;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;

public class CompleteProfileActivity extends AppCompatActivity {
    Button btnDone;
    EditText your_full_name;
    EditText txt_birth_date;

    DatePickerInputEditText datePickerInputEditText;
    String name, gender, birthdate;
    RadioRealButtonGroup radioButtonGender;
    Customer cr;
    PreferenceHelper pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        radioButtonGender = findViewById(R.id.radioButtonGender);
        radioButtonGender.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                if (currentPosition == 0) {
                    gender = "False";
                } else {
                    gender = "True";
                }

            }
        });

        your_full_name = findViewById(R.id.your_full_name);
       // txt_birth_date = findViewById(R.id.datePickerInputEditText);

        //your_full_name.setText();
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        btnDone = findViewById(R.id.btnDone);
        spinner.setItems("Male", "Female");

        datePickerInputEditText = (DatePickerInputEditText) findViewById(R.id.datePickerInputEditText);

        datePickerInputEditText.setManager(getSupportFragmentManager());
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateData();
                updateProfile();
              //
            }
        });
        initiateViews();

        //spinner.setSelectedIndex(Integer.parseInt(cr.getGender()));
    }

    private void initiateData() {
        name=your_full_name.getText().toString();
        birthdate= datePickerInputEditText.getText().toString();
    }

    private void updateProfile() {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(R.string.pleasewait));
            progressDialog.setCancelable(false);
            progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(PreferenceHelper.CUSTOMER_TOKEN, pr.getString(CompleteProfileActivity.this,PreferenceHelper.CUSTOMER_TOKEN,""));
        postParam.put(PreferenceHelper.CUSTOMER_NAME, name);
        postParam.put(PreferenceHelper.CUSTOMER_GENDER, gender);
        postParam.put(PreferenceHelper.CUSTOMER_BIRTH_DATE, birthdate);
        //TODO email
        postParam.put("Email", "email@gmail.com");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.5:123/api/Customer/UpdateCustomerProfile", new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                String Status = response.getString("Status");
                                //JSONObject Result= response.getJSONObject("Result");
                            if (Status .equals(AppConstants.success)) {
                                Toasty.success(CompleteProfileActivity.this, getResources().getString(R.string.data_updated_successfully), Toast.LENGTH_SHORT, true).show();
                                Intent i =new Intent(CompleteProfileActivity.this, ConfirmLocation.class);
                                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(new Intent(CompleteProfileActivity.this, CurrentLocationActivity.class));
                                  finish();
                              //  Toasty.success(ActivationCodeActivity.this, getResources().getString(R.string.customer_found)+Result.getString("Name"), Toast.LENGTH_SHORT, true).show();
                              //  pr.setString(LoginOrRegisterActivity.this,PreferenceHelper.USER_CODE_VERIFCATION,response.getString("Result"));*//*
                               // startActivity(i);
                            } else{
                                Toasty.error(CompleteProfileActivity.this, getResources().getString(R.string.data_not_updated), Toast.LENGTH_SHORT, true).show();
                            }

                            //else if (Status .equals("201")){
                               /* Toasty.success(ActivationCodeActivity.this, getResources().getString(R.string.customer_created), Toast.LENGTH_SHORT, true).show();
                                Intent i = new Intent(ActivationCodeActivity.this, CompleteProfileActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                String s=Result.getString("BirthDate");


                                pr.setCustomerData(ActivationCodeActivity.this,Result.getString("BirthDate"),phone,Result.getString("Token"),Result.getString("Gender"),Result.getString("Name"));
                                startActivity(i);*/
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




    private void initiateViews() {
        pr = PreferenceHelper.getPrefernceHelperInstace();
        cr = pr.getCustomerData(CompleteProfileActivity.this);
        if (!cr.getName().equals(null)) {
            name=cr.getName();
            your_full_name.setText(name);
        }
        birthdate=cr.getdate();
        datePickerInputEditText.setText(birthdate);
        if (cr.getGender().equals("True")) {
            gender = "True";
            radioButtonGender.setPosition(1);
        } else if (cr.getGender().equals("False")) {
            gender = "False";
            radioButtonGender.setPosition(0);
        }

    }


}
