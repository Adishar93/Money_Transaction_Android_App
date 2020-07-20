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
import android.widget.TextView;
import android.widget.Toast;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RequestMoneyFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

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
        //Firebase Initialization
        mDatabase= FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_request_money, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);

        final List<User> userList = new ArrayList<>();


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter and pass in our data model list

        mAdapter = new MyAdapter(userList, getContext());
        mRecyclerView.setAdapter(mAdapter);


        //Loading user List from Firebase

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());

                User temp=dataSnapshot.getValue(User.class);

                if(!temp.getUid().equals(mAuth.getUid()))
                userList.add(temp);


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
                Toast.makeText(getContext(), "Failed to load Users!",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addChildEventListener(childEventListener);




        return view;
    }











    //Inner Class Recycler View Adapter
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<User> userList;
        private Context mContext;



        // View holder class whose objects represent each list item
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView;
            public TextView emailTextView;
            private MaterialButton mMakeRequestButton;

            public MyViewHolder(@NonNull final View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.tvName);
                emailTextView = itemView.findViewById(R.id.tvEmail);
                mMakeRequestButton= itemView.findViewById(R.id.bMakeRequest);

                mMakeRequestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Load HomeTabFragment
                        assert getParentFragment() != null;
                        assert getParentFragment().getFragmentManager() != null;

                        //initializing TextViews to access User data
                        TextView mName=itemView.findViewById(R.id.tvName);
                        TextView mEmail=itemView.findViewById(R.id.tvEmail);


                        FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_placeholder, MakeRequestUserDetailFragment.newInstance(mName.getText().toString(),mEmail.getText().toString()));
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
            }

            public void bindData(User user, Context context) {
                nameTextView.setText(user.getName());
                emailTextView.setText(user.getEmail());
            }
        }


        public MyAdapter(List<User> modelList, Context context) {
            userList = modelList;
            mContext = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate out card list item

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_useraccount_item, parent, false);
            // Return a new view holder

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            // Bind data for the item at position

            holder.bindData(userList.get(position), mContext);
        }

        @Override
        public int getItemCount() {
            // Return the total number of items

            return userList.size();
        }


    }







}

