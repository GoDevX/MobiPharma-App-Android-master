package com.example.ahmedkhames.pharmacyclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ahmedkhames.pharmacyclientapp.R;


public class ConfirmLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_location);
    }
    public void goToCurrentLocation(View v){
startActivity(new Intent(ConfirmLocation.this,CurrentLocationActivity.class));
    }
    public void goToSavedLocation(View v){
        startActivity(new Intent(ConfirmLocation.this,SavedLocations.class));

    }
}
