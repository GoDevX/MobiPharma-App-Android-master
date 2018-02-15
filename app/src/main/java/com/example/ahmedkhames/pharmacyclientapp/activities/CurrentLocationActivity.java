package com.example.ahmedkhames.pharmacyclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.ahmedkhames.pharmacyclientapp.R;
import com.example.ahmedkhames.pharmacyclientapp.UserCurrentLocation.PickMarketPlaceAddressActivity;
import com.example.ahmedkhames.pharmacyclientapp.pharmacylisting.Activity.Sample1Activity;


public class CurrentLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
    }
    public void goToAvailablePharmacies(View v){

        startActivity(new Intent(CurrentLocationActivity.this,Sample1Activity.class));
    }
    public void getLocationFromMap(View v){
        Intent intent = new Intent(this, PickMarketPlaceAddressActivity.class);
        startActivityForResult(intent, 1);
      //  startActivity(new Intent(CurrentLocationActivity.this,PickMarketPlaceAddressActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check that it is the SecondActivity with an OK result
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra("address");
                EditText textViewAddress =  findViewById(R.id.address);
                textViewAddress.setText(returnString);
            }
        }
    }
}
