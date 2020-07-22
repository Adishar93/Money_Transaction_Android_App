package com.adishar93.moneytransactionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.fragments.HomeTabFragment;
import com.adishar93.moneytransactionapp.fragments.PaymentFragment;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            onActivityRequestResult(requestCode, resultCode, data, PaymentFragment.class.getSimpleName());
        Log.d("Home Activity : ","OnActivityResultCalled");
    }

    private void onActivityRequestResult(int requestCode, int resultCode, Intent data, String fragmentName){
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getFragments().size() > 0) {
                for(int i=0; i<fm.getFragments().size(); i++){
                    Fragment fragment = fm.getFragments().get(i);
                    if (fragment != null && fragment.getClass().getSimpleName().equalsIgnoreCase(fragmentName)) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                        Log.d("Home Activity : ","OnActivity Result Passed to a Fragment");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}