package com.adishar93.moneytransactionapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.Request;
import com.adishar93.moneytransactionapp.pojo.Transaction;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Optional;


public class PaymentFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;


    private String mPrice;
    private Request mRequest;

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    private Button mPayButton;
    private TextView mAmountTextView;

    //Two places in database will be written
    DatabaseReference mCurrToDatabase;
    DatabaseReference mReceiverFromDatabase;
    //Fetch Data for this user
    DatabaseReference mCurrUser;

    //Also Delete Request after payment
    DatabaseReference mRequestsDatabase;

    private User mUser;


    public PaymentFragment() {
        // Required empty public constructor
    }


    public static PaymentFragment newInstance(String price, Request request) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, price);
        args.putSerializable(ARG_PARAM2,request);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPrice = getArguments().getString(ARG_PARAM1);
            mRequest = (Request)getArguments().getSerializable(ARG_PARAM2);
            mRequest.setAmount(mPrice);
        }

        String currUid=FirebaseAuth.getInstance().getUid();
        mCurrToDatabase= FirebaseDatabase.getInstance().getReference("TransactionTo").child(currUid);
        mReceiverFromDatabase=FirebaseDatabase.getInstance().getReference("TransactionFrom").child(mRequest.getUid());
        mCurrUser=FirebaseDatabase.getInstance().getReference("Users").child(currUid);
        mRequestsDatabase=FirebaseDatabase.getInstance().getReference("Requests").child(currUid);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
               mUser=dataSnapshot.getValue(User.class);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mCurrUser.addListenerForSingleValueEvent(userListener);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_payment, container, false);

        mAmountTextView=view.findViewById(R.id.tvAmount);
        mAmountTextView.setText(mAmountTextView.getText().toString()+" "+mPrice);

        initializePayButton(view);

        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       //Handle payment transaction
    }

    public void initializePayButton(View view)
    {
        mPayButton=view.findViewById(R.id.bPay);

        mPayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Temporary operation performed here for testing, this code will be shifted to onActivityResult later and be replaced by UPI Intent code
                        Transaction to=new Transaction(mRequest,String.valueOf(System.currentTimeMillis()));
                        mCurrToDatabase.push().setValue(to);

                        Transaction from=new Transaction(mUser.getUid(),mUser.getName(),mUser.getEmail(),mRequest.getAmount(),mRequest.getDescription(),String.valueOf(System.currentTimeMillis()));
                        mReceiverFromDatabase.push().setValue(from);

                        mRequestsDatabase.child(mRequest.getUid()).removeValue();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Snackbar.make(getView(), "Payment Successful!", Snackbar.LENGTH_SHORT).show();


            }
        });
    }

}