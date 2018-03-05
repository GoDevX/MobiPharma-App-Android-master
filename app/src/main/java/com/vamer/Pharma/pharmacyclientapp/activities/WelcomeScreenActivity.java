package com.vamer.Pharma.pharmacyclientapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.vamer.Pharma.pharmacyclientapp.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeScreenActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Title", "Description", R.drawable.ic_image);
        ahoyOnboarderCard1.setBackgroundColor(R.color.button_brown);
        ahoyOnboarderCard1.setTitleColor(R.color.white);
        ahoyOnboarderCard1.setDescriptionColor(R.color.grey_200);
        ahoyOnboarderCard1.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard1.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard1.setIconLayoutParams(100, 100, 15, 15, 15, 15);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Title", "Description", R.drawable.ic_image);
        ahoyOnboarderCard2.setBackgroundColor(R.color.button_brown);
        ahoyOnboarderCard2.setTitleColor(R.color.white);
        ahoyOnboarderCard2.setDescriptionColor(R.color.grey_200);
        ahoyOnboarderCard2.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard2.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard2.setIconLayoutParams(100, 100, 15, 15, 15, 15);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Title", "Description", R.drawable.ic_image);
        ahoyOnboarderCard3.setBackgroundColor(R.color.button_brown);
        ahoyOnboarderCard3.setTitleColor(R.color.white);
        ahoyOnboarderCard3.setDescriptionColor(R.color.grey_200);
        ahoyOnboarderCard3.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard3.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard3.setIconLayoutParams(100, 100, 15, 15, 15, 15);
        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        setOnboardPages(pages);

    }

    @Override
    public void onFinishButtonPressed() {
        Intent i=new Intent(this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
