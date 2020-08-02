package com.adishar93.moneytransactionapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static android.app.Activity.RESULT_OK;


public class PaymentFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int UPI_PAYMENT = 991;


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




    public void initializePayButton(View view)
    {
        mPayButton=view.findViewById(R.id.bPay);

        mPayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(isConnectionAvailable(getActivity())) {
                    //Launch UPI Apps
                    Uri uri =
                            Uri.parse("upi://pay").buildUpon()
                                    .appendQueryParameter("pa", "kamaltenet@oksbi")       // Personal hardcoded UPI ID for testing
                                    .appendQueryParameter("pn", "Kamal Sharma")          // name
                                    .appendQueryParameter("tn", mRequest.getDescription())       //  note about payment
                                    .appendQueryParameter("am", "5")           // Test amount for minimum loss
                                    .appendQueryParameter("cu", "INR")                         // currency
                                    .build();


                    Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                    upiPayIntent.setData(uri);

                    // will always show a dialog to user to choose an app
                    Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

                    // check if intent resolves
                    if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
                        startActivityForResult(chooser, UPI_PAYMENT);
                    } else {
                        Snackbar.make(getView(), "No UPI app found, please install one to continue", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Snackbar.make(getView(),"No Internet Connection! Pleaase Try Again. ",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Handle payment transaction
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {

            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Snackbar.make(getView(), "Payment successful!", Snackbar.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);

                //After Successful transaction, store transaction data into history and delete request.
                        Transaction to=new Transaction(mRequest,String.valueOf(System.currentTimeMillis()));
                        mCurrToDatabase.push().setValue(to);

                        Transaction from=new Transaction(mUser.getUid(),mUser.getName(),mUser.getEmail(),mRequest.getAmount(),mRequest.getDescription(),String.valueOf(System.currentTimeMillis()));
                        mReceiverFromDatabase.push().setValue(from);

                        mRequestsDatabase.child(mRequest.getUid()).removeValue();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Snackbar.make(getView(), "Payment Successful!", Snackbar.LENGTH_SHORT).show();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Snackbar.make(getView(), "Payment cancelled by user.", Snackbar.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Snackbar.make(getView(), "Transaction failed.Please try again", Snackbar.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }

    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


}