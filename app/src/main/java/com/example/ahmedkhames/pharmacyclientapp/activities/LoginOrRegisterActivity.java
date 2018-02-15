package com.example.ahmedkhames.pharmacyclientapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.ahmedkhames.pharmacyclientapp.AppController;
import com.example.ahmedkhames.pharmacyclientapp.R;
import com.lamudi.phonefield.PhoneInputLayout;

import java.util.Map;

/**
 * Created by Ahmed.Khames on 1/24/2018.
 */

public class LoginOrRegisterActivity extends AppCompatActivity {
    EditText txtPhoneNumber;
Button btnRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_login_form);
//        setupWindowAnimations();
        final PhoneInputLayout phoneInputLayout = (PhoneInputLayout) findViewById(R.id.phone_input_layout);
        btnRegister=findViewById(R.id.submit_button);
        phoneInputLayout.setHint(R.string.phone_hint);
        phoneInputLayout.setDefaultCountry("EG");
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
                    startActivity(new Intent(LoginOrRegisterActivity.this,ActivationCodeActivity.class));

                } else {
                    Toast.makeText(LoginOrRegisterActivity.this, R.string.invalid_phone_number, Toast.LENGTH_LONG).show();
                }

                // Return the phone number as follows
                String phoneNumber = phoneInputLayout.getPhoneNumber();
                String a=phoneNumber;
            }
        });

    }

    public void getComments() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                /*Constants.BASE_URL +*/ "wall/post/comments", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               /* progressDialog.dismiss();
                String userName = null;
                Log.e("CommentData-->",response);
                String content = null;
                try {
                    JSONObject ob = new JSONObject(response);
                    JSONArray array = ob.getJSONArray("comments");
                    int count = 0;
                    while (count < array.length()) {
                        CommentResponseModel commentResponseModel= new Gson().fromJson(response.toString(),CommentResponseModel.class);
                        model.add(commentResponseModel);
                        count++;
                    }
                    commentsListView.setAdapter(adapter);*/

               /* } catch (JSONException e) {
                    e.printStackTrace();*/
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                /*Map<String, String> params = new HashMap<String, String>();
                params.put("secret", new SQLiteHandler(getApplicationContext()).getUserDetails().get("secret"));
                params.put("token", new SQLiteHandler(getApplicationContext()).getUserDetails().get("token"));
                params.put("api_key", Constants.API_KEY);
                params.put("post_id", getIntent().getStringExtra("pId"));*/
                /*return params;*/
                return null;
            }

        };
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq, "tag");
    }
  /*  private void setupWindowAnimations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }*/
}



