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
import android.widget.Toast;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseDatabase mdatabase;
    DatabaseReference mUserRef;


    EditText mName;
    EditText mEmail;
    EditText mPhone;
    EditText mPassword;
    EditText mConfirmPassword;

    Button mSignup;

    public SignupFragment() {
        // Required empty public constructor
    }


    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        mUserRef = mdatabase.getReference("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signup, container, false);

        //Initializing Views
        mName=view.findViewById(R.id.etName);
        mEmail=view.findViewById(R.id.etEmail);
        mPhone=view.findViewById(R.id.etPhone);
        mPassword=view.findViewById(R.id.etPassword);
        mConfirmPassword=view.findViewById(R.id.etConfirmPassword);

        mSignup=view.findViewById(R.id.bSignup);

        //Setting onClick Listeners
        mSignup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name=mName.getText().toString();
                String email=mEmail.getText().toString();
                String phone=mPhone.getText().toString();
                String password=mPassword.getText().toString();
                String confirmPassword=mConfirmPassword.getText().toString();

                final User user=new User(name,email,phone);


                if(name.equals("")||email.equals("")||phone.equals("")||password.equals("")||confirmPassword.equals(""))
                {
                    //Toast.makeText(getActivity(),"Fields cannot be left empty!",Toast.LENGTH_SHORT).show();
                    Snackbar.make(getView(), "Fields cannot be left empty!", Snackbar.LENGTH_SHORT)
                            .show();
                }
                else if(!password.equals(confirmPassword))
                {
                   // Toast.makeText(getActivity(),"Entered Passwords do not match!",Toast.LENGTH_SHORT).show();
                    Snackbar.make(getView(), "Entered Passwords do not match!", Snackbar.LENGTH_SHORT)
                            .show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign ip success
                                        Log.d("Firebase : ", "createUserWithEmail:success");
                                        //Toast.makeText(getContext(), "Account Created!",Toast.LENGTH_SHORT).show();
                                        Snackbar.make(getView(), "Account Created!", Snackbar.LENGTH_SHORT)
                                                .show();

                                        //Update Database with user data
                                        user.setUid(mAuth.getUid());
                                        mUserRef.child(user.getUid()).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(getContext(), "Data Written to Database!",Toast.LENGTH_SHORT).show();
                                                //Snackbar.make(getView(), "Data Written to Database!", Snackbar.LENGTH_SHORT)
                                                 //       .show();
                                                Log.d("Firebase : ", "Signup Data Written to Database");

                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Toast.makeText(getContext(), "Failed to Write Data to Database!",Toast.LENGTH_SHORT).show();
                                                        //Snackbar.make(getView(), "Failed to Write Data to Database!", Snackbar.LENGTH_SHORT)
                                                          //      .show();
                                                        Log.d("Firebase : ", "Failed to write Signup Data to Database");
                                                    }
                                                });

                                        //update UI to the login page
                                        mAuth.signOut();
                                        openLogin();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Firebase : ", "createUserWithEmail:failure", task.getException());
                                        //Toast.makeText(getContext(), "Create User failed.",Toast.LENGTH_SHORT).show();
                                        Snackbar.make(getView(), "Create User failed.", Snackbar.LENGTH_SHORT)
                                                .show();

                                    }

                                    // ...
                                }
                            });
                }
            }
        });



        return view;
    }

    private void openLogin()
    {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}

