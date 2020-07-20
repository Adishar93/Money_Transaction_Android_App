package com.adishar93.moneytransactionapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.fragments.HomeTabFragment;
import com.adishar93.moneytransactionapp.fragments.LoginFragment;
import com.adishar93.moneytransactionapp.fragments.RecieveGrantFragment;
import com.adishar93.moneytransactionapp.fragments.RequestMoneyFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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