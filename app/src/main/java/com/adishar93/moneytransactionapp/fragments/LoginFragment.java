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
import com.adishar93.moneytransactionapp.activities.AuthenticationActivity;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mPhoneAuthenticationCallbacks;

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
        mUserDatabase= FirebaseDatabase.getInstance().getReference("Users");

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

                                                //Login successful. Fetch user data
                                                ValueEventListener userListener = new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        //After obtaining user data (with phone number) time for user phone verification
                                                        User currentUser=dataSnapshot.getValue(User.class);
                                                        mAuth.signOut();

                                                        phoneVerification(currentUser.getPhone());

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        // Getting Post failed, log a message
                                                        Log.w("Firebase : ", "loadPost:onCancelled", databaseError.toException());
                                                        // ...
                                                    }
                                                };

                                                mUserDatabase.child(mAuth.getUid()).addListenerForSingleValueEvent(userListener);


                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("Firebase : ", "signInWithEmail:failure", task.getException());
                                                //Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                                Snackbar.make(getView(), "Authentication failed.", Snackbar.LENGTH_SHORT)
                                                        .show();
                                                resetPassField();
                                                // ...
                                            }

                                            // ...
                                        }
                                    });
                        }
                        else
                        {
                            Snackbar.make(getView(), "You cannot leave email or password empty !", Snackbar.LENGTH_SHORT)
                                    .show();
                        }


                    }

                });

        //Signup Onclick
        mSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((AuthenticationActivity)getActivity()).openSignup();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        ((AuthenticationActivity)getActivity()).openHome(currentUser);
    }

    private void resetPassField()
    {
        mPassword.setText("");
    }

    public void phoneVerification(String phoneNumber)
    {
        mPhoneAuthenticationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("Firebase : ", "onPhoneVerificationCompleted:" + credential);
                Snackbar.make(getView(),"Phone verification Successful!",Snackbar.LENGTH_SHORT).show();


                //Login with the phone
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Firebase : ", "signInWithCredential:success");
                                        Snackbar.make(getView(),"Phone Verification SignIn successful",Snackbar.LENGTH_SHORT).show();
                                        ((AuthenticationActivity) getActivity()).openHome(mAuth.getCurrentUser());

                                    } else {
                                        // Sign in failed, display a message and update the UI
                                        Log.w("Firebase : ", "signInWithCredential:failure", task.getException());
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            // The verification code entered was invalid
                                        }
                                    }
                                }
                            });

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("Firebase : ", "onPhoneVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded

                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("Firebase", "onCodeSent:" + verificationId);
                Snackbar.make(getView(),"Verification Code sent to your phone!",Snackbar.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                //mVerificationId = verificationId;
                //mResendToken = token;

            }
        };

        //Phone sign in while passing callback
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mPhoneAuthenticationCallbacks);         // OnVerificationStateChangedCallbacks

    }

}