package com.example.ahmedkhames.pharmacyclientapp.pharmacylisting.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ahmedkhames.pharmacyclientapp.R;
import com.example.ahmedkhames.pharmacyclientapp.pharmacylisting.Adapter.AdapterSample2;
import com.example.ahmedkhames.pharmacyclientapp.pharmacylisting.Data.DataObject;
import com.example.ahmedkhames.pharmacyclientapp.pharmacylisting.Data.GeneratesDataFake;

import java.util.ArrayList;

import io.rmiri.skeleton.Master.IsCanSetAdapterListener;


public class Sample2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterSample2 adapterSample2;
    private ArrayList<DataObject> dataObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_2);


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
        adapterSample2 = new AdapterSample2(getApplicationContext(), dataObjects,recyclerView, new IsCanSetAdapterListener() {
            @Override
            public void isCanSet() {
                recyclerView.setAdapter(adapterSample2);
            }
        });


        // After 5 second get data fake
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataObjects = new GeneratesDataFake().generateDataFake();
                adapterSample2.addMoreDataAndSkeletonFinish(dataObjects);
            }
        }, 5000);
    }


}
