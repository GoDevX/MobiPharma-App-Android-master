package com.example.ahmedkhames.pharmacyclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;

import com.example.ahmedkhames.pharmacyclientapp.R;


/**
 * Created by Ahmed.Khames on 1/24/2018.
 */

public class ActivationCodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Button btnVerify;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirmation);
      //  setupWindowAnimations();

        btnVerify=findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(ActivationCodeActivity.this,CompleteProfileActivity.class));
    }
});
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
}
