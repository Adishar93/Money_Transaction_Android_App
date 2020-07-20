package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adishar93.moneytransactionapp.R;


public class MakeRequestUserDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mName;
    private String mEmail;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_make_request_user_detail, container, false);
        //initializing TextViews
        TextView mNameTextView=view.findViewById(R.id.tvName);
        TextView mEmailTextView=view.findViewById(R.id.tvEmail);
        mNameTextView.setText(mName);
        mEmailTextView.setText(mEmail);
        return view;
    }
}