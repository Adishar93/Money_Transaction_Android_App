package com.adishar93.moneytransactionapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import com.adishar93.moneytransactionapp.R;
import com.adishar93.moneytransactionapp.fragments.RecieveGrantFragment;
import com.adishar93.moneytransactionapp.fragments.RequestMoneyFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    ViewPager2 mViewPager;
    TabLayout mTabLayout;

    FirebaseAuth mAuth;

    String[] mTabNames={"Request","Grant"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initializing views
        mViewPager=findViewById(R.id.view_pager);
        mTabLayout=findViewById(R.id.tab_layout);

        //Setting  up ViewPager and Tab Layout
        initTabViewPager();

        //Firebase Variables
        mAuth=FirebaseAuth.getInstance();

        //Check Login Or Not
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent=new Intent(this,AuthenticationActivity.class);
            finish();
            startActivity(intent);
        }
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
        public ViewPagerFragmentAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return RequestMoneyFragment.newInstance();
                case 1:
                    return RecieveGrantFragment.newInstance();
            }
            return RequestMoneyFragment.newInstance();

        }

        @Override
        public int getItemCount() {
            return mTabNames.length;
        }
    }
}