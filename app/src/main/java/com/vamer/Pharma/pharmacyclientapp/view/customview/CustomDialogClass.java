package com.vamer.Pharma.pharmacyclientapp.view.customview;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.activities.CurrentLocationActivity;
import com.vamer.Pharma.pharmacyclientapp.model.Location;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
       Location l;
    public CustomDialogClass(Activity a, Location l) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.l=l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_save_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:

                c.finish();
                break;
            case R.id.btn_no:

                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}