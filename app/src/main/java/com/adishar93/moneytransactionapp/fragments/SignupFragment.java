package com.adishar93.moneytransactionapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseDatabase mdatabase;
    DatabaseReference mUserRef;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mPhoneAuthenticationCallbacks;

    User user=null;

    EditText mName;
    EditText mEmail;
    EditText mPhone;
    EditText mPassword;
    EditText mConfirmPassword;
    CheckBox mTwoStepVerificationCheckBox;
    AlertDialog mOTPDialog;

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
        mTwoStepVerificationCheckBox=view.findViewById(R.id.cbTwoStepVerification);

        mSignup=view.findViewById(R.id.bSignup);

        //Setting onClick Listeners
        mSignup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String name=mName.getText().toString();
                final String email=mEmail.getText().toString();
                final String phone=mPhone.getText().toString();
                final String password=mPassword.getText().toString();
                final String confirmPassword=mConfirmPassword.getText().toString();

                user=new User(name,email,phone);


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


                                        //After successful creation of email account, phone verification is performed and linked
                                        if(mTwoStepVerificationCheckBox.isChecked()) {
                                            phoneVerification(phone);
                                            user.setTwoStepVerification(true);
                                        }

                                           addUserToDatabase(!mTwoStepVerificationCheckBox.isChecked());



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

                if(mOTPDialog!=null)
                {
                    mOTPDialog.cancel();
                }

                //Link phone account to email account
                mAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Firebase : ", "linkWithCredential:success");

                                    //update UI to the login page
                                    mAuth.signOut();
                                    openLogin();
                                } else {
                                    Log.w("Firebase : ", "linkWithCredential:failure", task.getException());
                                    mAuth.signOut();

                                }

                                // ...
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

              showOTPDialog(verificationId);

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

    public void addUserToDatabase(final boolean handleLaunch)
    {
        //Update Database with user data

        user.setUid(mAuth.getUid());

        if(handleLaunch) {
            mAuth.signOut();
            openLogin();
        }

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
    }

    public void showOTPDialog(final String verificationId)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Phone Verification");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout_otp, null);
        final TextInputEditText input=dialogView.findViewById(R.id.tietOTP);
        builder.setView(dialogView);
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Create credential for the user using OTP entered
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, input.getText().toString());

                //Link phone account to email account
                mAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Firebase : ", "linkWithCredential:success");
                                    Snackbar.make(getView(),"Phone verification Successful!",Snackbar.LENGTH_SHORT).show();
                                    //update UI to the login page
                                    mAuth.signOut();
                                    openLogin();
                                } else {
                                    Log.w("Firebase : ", "linkWithCredential:failure", task.getException());
                                    Snackbar.make(getView(),"Something went wrong with phone verification!",Snackbar.LENGTH_SHORT).show();
                                    mAuth.signOut();

                                }

                                // ...
                            }
                        });
            }
        });
        AlertDialog otpDialog=builder.create();
        otpDialog.show();
        mOTPDialog=otpDialog;

    }
}

