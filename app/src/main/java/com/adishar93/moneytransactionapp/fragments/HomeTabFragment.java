package com.adishar93.moneytransactionapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adishar93.moneytransactionapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomeTabFragment extends Fragment {

    ViewPager2 mViewPager;
    TabLayout mTabLayout;



    String[] mTabNames={"Request","Grant"};

    public HomeTabFragment() {
        // Required empty public constructor
    }


    public static HomeTabFragment newInstance() {
        HomeTabFragment fragment = new HomeTabFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_home_tab, container, false);

        //Initializing views
        mViewPager=view.findViewById(R.id.view_pager);
        mTabLayout=view.findViewById(R.id.tab_layout);

        //Setting  up ViewPager and Tab Layout
        initTabViewPager();

       return view;
    }

    private void initTabViewPager()
    {
        mViewPager.setAdapter(new ViewPagerFragmentAdapter(this));

        TabLayoutMediator.TabConfigurationStrategy tcs=new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTabNames[position]);
            }
        };

        new TabLayoutMediator(mTabLayout,mViewPager,tcs).attach();
    }

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter
    {
        public ViewPagerFragmentAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return RequestMoneyFragment.newInstance();
                case 1:
                    return ReceiveGrantFragment.newInstance();
            }
            return RequestMoneyFragment.newInstance();

        }

        @Override
        public int getItemCount() {
            return mTabNames.length;
        }
    }
}