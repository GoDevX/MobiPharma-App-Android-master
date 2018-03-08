package com.vamer.Pharma.pharmacyclientapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.vamer.Pharma.pharmacyclientapp.R;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

      TextView tx=findViewById(R.id.order_id);
        tx.setText(getIntent().getStringExtra("order_id"));

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(OrderSuccessActivity.this ,HomeActivity.class);
                i.putExtra("new_cart","new_cart");
                startActivity(i);
            }
        });



    }
}
