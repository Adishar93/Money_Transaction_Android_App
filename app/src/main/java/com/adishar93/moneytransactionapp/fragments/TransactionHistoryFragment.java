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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.pojo.Request;
import com.adishar93.moneytransactionapp.pojo.Transaction;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransactionHistoryFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressView;

    private DatabaseReference  mDatabaseFrom;
    private DatabaseReference mDatabaseTo;

    public TransactionHistoryFragment() {
        // Required empty public constructor
    }


    public static TransactionHistoryFragment newInstance() {
        TransactionHistoryFragment fragment = new TransactionHistoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String currentUid=FirebaseAuth.getInstance().getUid();
        mDatabaseTo= FirebaseDatabase.getInstance().getReference("TransactionTo").child(currentUid);
        mDatabaseFrom= FirebaseDatabase.getInstance().getReference("TransactionFrom").child(currentUid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_transaction_history, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mProgressView=view.findViewById(R.id.pbProgress);

        final List<Transaction> transactionList = new ArrayList<>();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter and pass in our data model list

        mAdapter = new TransactionHistoryFragment.MyAdapter(transactionList, getContext());
        mRecyclerView.setAdapter(mAdapter);

        //Setting Up Database Listener for To Database
        ChildEventListener childEventListenerTo = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());

                Transaction temp=dataSnapshot.getValue(Transaction.class);

                temp.to=true;
                transactionList.add(temp);


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

        mDatabaseTo.addChildEventListener(childEventListenerTo);


        //Setting up Database Listener for From Database

        ChildEventListener childEventListenerFrom = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());
                Log.d("Firebase : ", "onChildAdded:" + dataSnapshot.getKey());

                Transaction temp=dataSnapshot.getValue(Transaction.class);

                temp.to=false;
                transactionList.add(temp);


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

        mDatabaseFrom.addChildEventListener(childEventListenerFrom);




        return view;
    }




    //Inner Class Recycler View Adapter
    class MyAdapter extends RecyclerView.Adapter<TransactionHistoryFragment.MyAdapter.MyViewHolder> {

        private List<Transaction> transactionList;
        private Context mContext;



        // View holder class whose objects represent each list item
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView fromNameTextView;
            public TextView toNameTextView;
            public TextView dateTextView;
            public TextView timeTextView;
            public TextView amountTextView;
            private ImageView arrowRightImageView;
            private ImageView arrowLeftImageView;



            public MyViewHolder(@NonNull final View itemView) {
                super(itemView);
                fromNameTextView = itemView.findViewById(R.id.tvFromName);
                toNameTextView = itemView.findViewById(R.id.tvToName);
                dateTextView=itemView.findViewById(R.id.tvDate);
                timeTextView=itemView.findViewById(R.id.tvTime);
                amountTextView=itemView.findViewById(R.id.tvAmount);
                arrowRightImageView=itemView.findViewById(R.id.ivTransactionRightArrow);
                arrowLeftImageView=itemView.findViewById(R.id.ivTransactionLeftArrow);



            }

            public void bindData(final Transaction transaction, Context context) {

                //Parsing Date and Time
                Long dateNTime = Long.parseLong(transaction.getDateNTime());
                Date dateObject = new Date(dateNTime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                String time = timeFormat.format(dateObject);
                timeTextView.setText(time);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = dateFormat.format(dateObject);
                dateTextView.setText(date);

                amountTextView.setText(getString(R.string.amounttv_label)+" "+transaction.getAmount());
                fromNameTextView.setText("You");
                toNameTextView.setText(transaction.getName());

                if(transaction.to)
                {
                   arrowRightImageView.setVisibility(View.VISIBLE);
                   arrowLeftImageView.setVisibility(View.GONE);

                }
                else
                {
                    arrowRightImageView.setVisibility(View.GONE);
                    arrowLeftImageView.setVisibility(View.VISIBLE);
                }
//                nameTextView.setText(transaction.getName());
//                emailTextView.setText(transaction.getEmail());


//                viewRequestButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Load HomeTabFragment
//                        assert getParentFragment() != null;
//                        assert getParentFragment().getFragmentManager() != null;
//
//
//                        FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
//                        ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
//                        ft.replace(R.id.fragment_placeholder, GrantRequestDetailFragment.newInstance(transaction));
//                        ft.addToBackStack(null);
//                        ft.commit();
//                    }
//                });

            }
        }


        public MyAdapter(List<Transaction> modelList, Context context) {
            transactionList = modelList;
            mContext = context;
        }

        @NonNull
        @Override
        public TransactionHistoryFragment.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate out card list item

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_useraccount_transaction_item, parent, false);
            // Return a new view holder

            return new TransactionHistoryFragment.MyAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionHistoryFragment.MyAdapter.MyViewHolder holder, int position) {
            // Bind data for the item at position

            holder.bindData(transactionList.get(position), mContext);
        }

        @Override
        public int getItemCount() {
            // Return the total number of items

            return transactionList.size();
        }


    }



}