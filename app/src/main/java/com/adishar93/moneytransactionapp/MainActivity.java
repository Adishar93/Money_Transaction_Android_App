package com.adishar93.moneytransactionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //Set Transition
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // Apply activity transition
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//
//            getWindow().setExitTransition(new Fade());
//        }

        setContentView(R.layout.activity_main);


        //First Load Login Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, LoginFragment.newInstance());
        ft.commit();


    }


    protected void openHome(FirebaseUser user)
    {
        if(user!=null)
        {
            //Start Home Activity
            Toast.makeText(MainActivity.this, "Login Successful.",
                    Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,HomeActivity.class);
            finish();
            startActivity(intent);
            //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }

    }

    protected void openSignup()
    {

        //Set Signup Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, SignupFragment.newInstance());
        ft.addToBackStack(null);
        ft.commit();


    }
}