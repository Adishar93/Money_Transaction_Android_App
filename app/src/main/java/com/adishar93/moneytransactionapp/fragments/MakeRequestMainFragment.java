package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adishar93.moneytransactionapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeRequestMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeRequestMainFragment extends Fragment {



    public MakeRequestMainFragment() {
        // Required empty public constructor
    }


    public static MakeRequestMainFragment newInstance() {
        MakeRequestMainFragment fragment = new MakeRequestMainFragment();

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
        View view= inflater.inflate(R.layout.fragment_make_request_main, container, false);
        return view;
    }
}