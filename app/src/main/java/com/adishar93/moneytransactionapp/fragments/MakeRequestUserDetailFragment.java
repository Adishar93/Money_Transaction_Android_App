package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.Request;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MakeRequestUserDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


    private User currentUser;
    private String mName;
    private String mEmail;
    private String mUid;


    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextInputEditText mAmountEditText;
    private TextInputEditText mDescriptionEditText;
    private Button mSendRequestButton;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseStore;
    private DatabaseReference mDatabaseRetrieve;


    public MakeRequestUserDetailFragment() {
        // Required empty public constructor
    }


    public static MakeRequestUserDetailFragment newInstance(String name,String email,String uid) {
        MakeRequestUserDetailFragment fragment = new MakeRequestUserDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        args.putString(ARG_PARAM2, email);
        args.putString(ARG_PARAM3, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_PARAM1);
            mEmail = getArguments().getString(ARG_PARAM2);
            mUid=getArguments().getString(ARG_PARAM3);
        }


        mAuth=FirebaseAuth.getInstance();

        mDatabaseStore=FirebaseDatabase.getInstance().getReference("Requests").child(mUid).child(mAuth.getUid());

        mDatabaseRetrieve=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                currentUser = dataSnapshot.getValue(User.class);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase : ", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabaseRetrieve.addValueEventListener(postListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_make_request_user_detail, container, false);
        //initializing TextViews
        mNameTextView=view.findViewById(R.id.tvName);
        mEmailTextView=view.findViewById(R.id.tvEmail);
        mSendRequestButton=view.findViewById(R.id.bSendRequest);
        mAmountEditText=view.findViewById(R.id.tietAmount);
        mDescriptionEditText=view.findViewById(R.id.tietDescription);

        mSendRequestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String amount=mAmountEditText.getText().toString();
                String description=mDescriptionEditText.getText().toString();

                if(!amount.equals("")&&!description.equals("")) {


                    Request userRequest = new Request(currentUser.getUid(), currentUser.getName(), currentUser.getEmail(), amount, description);
                    mDatabaseStore.setValue(userRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           // Toast.makeText(getContext(), "Request made Successfully!", Toast.LENGTH_SHORT).show();
                            Snackbar.make(getView(), "Request made Successfully!", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(getContext(), "Failed to Make Request! Try Again", Toast.LENGTH_SHORT).show();
                                    Snackbar.make(getView(), "Failed to Make Request! Try Again", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            });
                }
                else
                {
                    //Toast.makeText(getContext(), "You cannot leave amount or description empty!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getView(), R.string.warncl_amount_desc_empty, Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });



    //Setting Argument passed Values
        mNameTextView.setText(mName);
        mEmailTextView.setText(mEmail);
        return view;
    }
}