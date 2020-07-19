package com.adishar93.moneytransactionapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.adishar93.moneytransactionapp.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //Set Transition
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // Apply activity transition
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//
//            getWindow().setEnterTransition(new Fade());
//        }

        setContentView(R.layout.activity_home);
    }
}