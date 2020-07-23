package com.adishar93.moneytransactionapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.Request;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ReceiveGrantFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressView;
    private TextView mNoGrantRequestTextView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    public ReceiveGrantFragment() {
        // Required empty public constructor
    }


    public static ReceiveGrantFragment newInstance() {
        ReceiveGrantFragment fragment = new ReceiveGrantFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("Requests").child(mAuth.getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_receive_grant, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mProgressView=view.findViewById(R.id.pbProgress);
        mNoGrantRequestTextView=view.findViewById(R.id.tvNoGrantRequest);

        final List<Request> requestList = new ArrayList<>();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter and pass in our data model list

        mAdapter = new ReceiveGrantFragment.MyAdapter(requestList, getContext());
        mRecyclerView.setAdapter(mAdapter);


        //Loading Request List from Firebase

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());

                Request temp=dataSnapshot.getValue(Request.class);


                    requestList.add(temp);

                    mProgressView.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Firebase : ", "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.


                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Firebase : ", "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.


                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Firebase", "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase : ", "postComments:onCancelled", databaseError.toException());
                mProgressView.setVisibility(View.INVISIBLE);
                Snackbar.make(getView(), "Failed to load Users!",
                        Snackbar.LENGTH_SHORT).show();
            }
        };

        mDatabase.addChildEventListener(childEventListener);

        //Check if database is empty
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    // The child doesn't exist
                    mProgressView.setVisibility(View.INVISIBLE);
                    mNoGrantRequestTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }



    //Inner Class Recycler View Adapter
    class MyAdapter extends RecyclerView.Adapter<ReceiveGrantFragment.MyAdapter.MyViewHolder> {

        private List<Request> requestList;
        private Context mContext;



        // View holder class whose objects represent each list item
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView;
            public TextView emailTextView;
            private MaterialButton viewRequestButton;

            public MyViewHolder(@NonNull final View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.tvName);
                emailTextView = itemView.findViewById(R.id.tvEmail);

                viewRequestButton= itemView.findViewById(R.id.bViewRequest);


            }

            public void bindData(final Request request, Context context) {
                nameTextView.setText(request.getName());
                emailTextView.setText(request.getEmail());


                viewRequestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Load HomeTabFragment
                        assert getParentFragment() != null;
                        assert getParentFragment().getFragmentManager() != null;


                        FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
                        ft.replace(R.id.fragment_placeholder, GrantRequestDetailFragment.newInstance(request));
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });

            }
        }


        public MyAdapter(List<Request> modelList, Context context) {
            requestList = modelList;
            mContext = context;
        }

        @NonNull
        @Override
        public ReceiveGrantFragment.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate out card list item

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_useraccount_grant_item, parent, false);
            // Return a new view holder

            return new ReceiveGrantFragment.MyAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReceiveGrantFragment.MyAdapter.MyViewHolder holder, int position) {
            // Bind data for the item at position

            holder.bindData(requestList.get(position), mContext);
        }

        @Override
        public int getItemCount() {
            // Return the total number of items

            return requestList.size();
        }


    }


}