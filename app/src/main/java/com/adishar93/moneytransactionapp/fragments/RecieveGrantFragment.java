package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adishar93.moneytransactionapp.R;


public class RecieveGrantFragment extends Fragment {



    public RecieveGrantFragment() {
        // Required empty public constructor
    }


    public static RecieveGrantFragment newInstance() {
        RecieveGrantFragment fragment = new RecieveGrantFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recieve_grant, container, false);
        return view;
    }
}