package com.adishar93.moneytransactionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.fragments.HomeTabFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //Firebase Variables
        mAuth=FirebaseAuth.getInstance();

        //Check Login Or Not
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent=new Intent(this,AuthenticationActivity.class);
            finish();
            startActivity(intent);
        }

        //Load HomeTabFragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, HomeTabFragment.newInstance());
        ft.commit();
    }




}