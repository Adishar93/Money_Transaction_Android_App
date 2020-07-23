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
import android.widget.TextView;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.Utilities.GPayUtil;
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

    private View mGooglePayButton;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_payment, container, false);
        initializeGPayButton(view);
        mAmountTextView=view.findViewById(R.id.tvAmount);

        mAmountTextView.setText(mAmountTextView.getText().toString()+" "+mPrice);

        paymentsClient = GPayUtil.createPaymentsClient(getActivity());
        possiblyShowGooglePayButton();
        return view;
    }

    private void initializeGPayButton(View view) {



        // The Google Pay button is a layout file – take the root view
        mGooglePayButton = view.findViewById(R.id.llgpay_button);
        mGooglePayButton.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        //Snackbar.make(getView(), "Google Pay Button Pressed!", Snackbar.LENGTH_SHORT).show();
                        Log.d("GPayButton : ","Pressed");





                        //Temporary operation performed here for testing, this code will be shifted to onActivityResult later
                        Transaction to=new Transaction(mRequest,String.valueOf(System.currentTimeMillis()));
                        mCurrToDatabase.push().setValue(to);

                        Transaction from=new Transaction(mUser.getUid(),mUser.getName(),mUser.getEmail(),mRequest.getAmount(),mRequest.getDescription(),String.valueOf(System.currentTimeMillis()));
                        mReceiverFromDatabase.push().setValue(from);

                        mRequestsDatabase.child(mRequest.getUid()).removeValue();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Snackbar.make(getView(), "Payment Successful!", Snackbar.LENGTH_SHORT).show();

                        //Temporarily commented
                        //requestPayment(view);
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        mGooglePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        Log.d("requestPayment","Called");

            mPrice="20";
            Optional<JSONObject> paymentDataRequestJson = GPayUtil.getPaymentDataRequest(Long.parseLong(mPrice));
            if (!paymentDataRequestJson.isPresent()) {
                Log.d("payentDataRequestJson","NotPresent");
                return;
            }

            PaymentDataRequest request =
                    PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                Log.d("GPay request","Not Null"+request.toJson());
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        Objects.requireNonNull(getActivity()), LOAD_PAYMENT_DATA_REQUEST_CODE);
            }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = GPayUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            Log.w("isReadyToPayJson", ".ispresent() failed");
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(getActivity(),
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                            Log.w("isReadyToPay succeeded", task.getResult().toString());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            mGooglePayButton.setVisibility(View.VISIBLE);
        } else {
            Snackbar.make(getView(), "Sorry! Google Pay Not Available!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("PaymentFragment : ","OnActivity Result Called");
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                Log.d("PaymentFragment : ","OnActivity Result code LOAD_PAYMENT_DATA_REQUEST_CODE");

                switch (resultCode) {

                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        Log.d("Payment Fragment : ","RESULT_OK Payment Data:"+paymentData.toJson());
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        Log.d("Payment Fragment : ","RESULT_CANCELED ");
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        //handleError(status.getStatusCode());
                        Log.d("Payment Fragment : ","RESULT_ERROR"+status.getStatusCode()+" "+status.getStatusMessage());
                        break;
                }

                // Re-enables the Google Pay payment button.
                mGooglePayButton.setClickable(true);
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String tokenizationType = tokenizationData.getString("type");
            final String token = tokenizationData.getString("token");

            if ("PAYMENT_GATEWAY".equals(tokenizationType) && "examplePaymentMethodToken".equals(token)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning")
                        .setMessage("Gateway replace name example")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }

            final JSONObject info = paymentMethodData.getJSONObject("info");

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("Payment Data Parsing Error");
        }
    }
}