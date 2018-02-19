package com.vamer.Pharma.pharmacyclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.vamer.Pharma.pharmacyclientapp.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;

public class CompleteProfileActivity extends AppCompatActivity {
    DatePickerInputEditText datePickerInputEditText;
Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        btnDone=findViewById(R.id.btnDone);
        spinner.setItems("Male", "Female");
        datePickerInputEditText = (DatePickerInputEditText) findViewById(R.id.datePickerInputEditText);
        datePickerInputEditText.setManager(getSupportFragmentManager());
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompleteProfileActivity.this, ConfirmLocation.class));
            }
        });



    }
}
