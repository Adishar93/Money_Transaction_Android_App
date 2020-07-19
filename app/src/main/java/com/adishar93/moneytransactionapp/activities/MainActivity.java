package com.adishar93.moneytransactionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.adishar93.moneytransactionapp.fragments.LoginFragment;
import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.fragments.SignupFragment;
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


    public void openHome(FirebaseUser user)
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

    public void openSignup()
    {

        //Set Signup Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
        ft.replace(R.id.fragment_placeholder, SignupFragment.newInstance());
        ft.addToBackStack(null);
        ft.commit();


    }
}