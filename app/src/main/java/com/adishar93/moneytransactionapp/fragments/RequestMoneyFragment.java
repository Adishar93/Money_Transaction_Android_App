package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adishar93.moneytransactionapp.R;


public class RequestMoneyFragment extends Fragment {



    public RequestMoneyFragment() {
        // Required empty public constructor
    }


    public static RequestMoneyFragment newInstance() {
        RequestMoneyFragment fragment = new RequestMoneyFragment();
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
        View view= inflater.inflate(R.layout.fragment_request_money, container, false);
        return view;
    }
}