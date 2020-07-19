package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {


    private FirebaseAuth mAuth;
    Button mLogin;
    EditText mEmail;
    EditText mPassword;
    TextView mSignup;


    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firebase variables
        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_login, container, false);

        //View Variables
        mLogin=view.findViewById(R.id.bLogin);
        mEmail=view.findViewById(R.id.etEmail);
        mPassword=view.findViewById(R.id.etPassword);
        mSignup=view.findViewById(R.id.tvSignup);

        //OnClick Listeners

        //Login Onclick
        mLogin.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        String email=mEmail.getText().toString();
                        String password=mPassword.getText().toString();

                        if(!email.equals("")&&!password.equals(""))
                        {
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("Firebase : ", "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                ((MainActivity) getActivity()).openHome(user);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("Firebase : ", "signInWithEmail:failure", task.getException());
                                                Toast.makeText(getActivity(), "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                resetPassField();
                                                // ...
                                            }

                                            // ...
                                        }
                                    });
                        }


                    }

                });

        //Signup Onclick
        mSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openSignup();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        ((MainActivity)getActivity()).openHome(currentUser);
    }

    private void resetPassField()
    {
        mPassword.setText("");
    }
}