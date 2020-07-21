package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adishar93.moneytransactionapp.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrantRequestDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrantRequestDetailFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";


    private String mName;
    private String mEmail;
    private String mAmount;
    private String mDesc;

    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mAmountTextView;
    private TextView mDescriptionTextView;


    public GrantRequestDetailFragment() {
        // Required empty public constructor
    }


    public static GrantRequestDetailFragment newInstance(String name, String email,String amount,String description) {
        GrantRequestDetailFragment fragment = new GrantRequestDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        args.putString(ARG_PARAM2, email);
        args.putString(ARG_PARAM3, amount);
        args.putString(ARG_PARAM4, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_PARAM1);
            mEmail = getArguments().getString(ARG_PARAM2);
            mAmount = getArguments().getString(ARG_PARAM3);
            mDesc = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_grant_request_detail, container, false);
        mNameTextView=view.findViewById(R.id.tvName);
        mEmailTextView=view.findViewById(R.id.tvEmail);
        mAmountTextView=view.findViewById(R.id.tvAmount);
        mDescriptionTextView=view.findViewById(R.id.tvDescription);

        mNameTextView.setText(mName);
        mEmailTextView.setText(mEmail);
        mAmountTextView.setText(mAmount);
        mDescriptionTextView.setText(mDesc);

       return view;
    }
}