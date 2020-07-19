package com.adishar93.moneytransactionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button mLogin;
    EditText mEmail;
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase variables
        mAuth=FirebaseAuth.getInstance();


        //View Variables
        mLogin=findViewById(R.id.bLogin);
        mEmail=findViewById(R.id.etEmail);
        mPassword=findViewById(R.id.etPassword);

        //OnClick Listeners
        mLogin.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Firebase : ", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Firebase : ", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                        // ...
                                    }

                                    // ...
                                }
                            });


                }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        updateUI(currentUser);

    }


    //Write Code
    private void updateUI(FirebaseUser user)
    {
        if(user!=null)
        {
            //Start Home Activity
            Toast.makeText(MainActivity.this, "Login Successful.",
                    Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            finish();
            startActivity(intent);
        }
        else
        {
            //Reset fields to blank
            mEmail.setText("");
            mPassword.setText("");
        }
    }
}