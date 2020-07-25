package com.adishar93.moneytransactionapp.activities;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseInstanceId mInstanceId;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //Firebase Variables
        mAuth=FirebaseAuth.getInstance();
        mInstanceId=FirebaseInstanceId.getInstance();


        //Check Login Or Not
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent=new Intent(this,AuthenticationActivity.class);
            finish();
            startActivity(intent);
        }
        //If Logged in, get the FirebaseMessagingToken and Store in database for the user
        else
        {
            mDatabase= FirebaseDatabase.getInstance().getReference("DeviceTokens").child(mAuth.getUid());
            mInstanceId.getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("Firebase : ", "getInstanceId for device token failed", task.getException());

                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    mDatabase.setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.w("Firebase : ", "device token saved to database successfully!");

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Firebase : ", "failed to save device token to database", e);

                                }
                            });

                }
            });
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