package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MakeRequestUserDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mName;
    private String mEmail;

    private Button mSendRequestButton;
    private TextInputEditText mAmountEditText;
    private TextInputEditText mDescriptionEditText;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseStore;
    private DatabaseReference mDatabaseRetrieve;


    public MakeRequestUserDetailFragment() {
        // Required empty public constructor
    }


    public static MakeRequestUserDetailFragment newInstance(String name,String email) {
        MakeRequestUserDetailFragment fragment = new MakeRequestUserDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        args.putString(ARG_PARAM2, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_PARAM1);
            mEmail = getArguments().getString(ARG_PARAM2);
        }

        mAuth=FirebaseAuth.getInstance();

        mDatabaseStore=FirebaseDatabase.getInstance().getReference("Requests");
        mDatabaseRetrieve=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_make_request_user_detail, container, false);
        //initializing TextViews
        TextView mNameTextView=view.findViewById(R.id.tvName);
        TextView mEmailTextView=view.findViewById(R.id.tvEmail);
        mSendRequestButton=view.findViewById(R.id.bSendRequest);
        mAmountEditText=view.findViewById(R.id.tietAmount);
        mDescriptionEditText=view.findViewById(R.id.tietDescription);

        mSendRequestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String amount=mAmountEditText.getText().toString();
                String description=mAmountEditText.getText().toString();


//                ValueEventListener postListener = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get Post object and use the values to update the UI
//
//                        User currentUser=dataSnapshot.getValue(User.class);
//
//                        // ...
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Getting Post failed, log a message
//                        Log.w("Firebase : ", "loadPost:onCancelled", databaseError.toException());
//                        // ...
//                    }
//                };
//                mDatabaseRetrieve.addValueEventListener(postListener);

            }
        });



    //Setting Argument passed Values
        mNameTextView.setText(mName);
        mEmailTextView.setText(mEmail);
        return view;
    }
}