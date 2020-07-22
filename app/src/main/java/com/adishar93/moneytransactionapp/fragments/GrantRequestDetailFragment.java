package com.adishar93.moneytransactionapp.fragments;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button mGrantFullButton;
    private Button mGrantPartialButton;


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
        mGrantFullButton=view.findViewById(R.id.bGrantFullRequest);
        mGrantPartialButton=view.findViewById(R.id.bGrantPartialRequest);

        mNameTextView.setText(mName);
        mEmailTextView.setText(mEmail);
        mAmountTextView.setText(mAmount);
        mDescriptionTextView.setText(mDesc);

        mGrantFullButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
                ft.replace(R.id.fragment_placeholder, PaymentFragment.newInstance(mAmountTextView.getText().toString(),null));
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        mGrantPartialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Partial Payment");

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_layout_partial_grant, null);
                    final TextInputEditText input=dialogView.findViewById(R.id.tietAmount);
                    builder.setView(dialogView);
                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
                            ft.replace(R.id.fragment_placeholder, PaymentFragment.newInstance(input.getText().toString(),null));
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    });
                    builder.show();

            }
        });

       return view;
    }
}