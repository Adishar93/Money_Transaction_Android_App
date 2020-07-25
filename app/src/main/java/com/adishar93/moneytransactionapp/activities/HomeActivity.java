package com.adishar93.moneytransactionapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.fragments.HomeTabFragment;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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




}