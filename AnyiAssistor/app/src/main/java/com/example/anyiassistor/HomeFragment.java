package com.example.anyiassistor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private ViewPager mViewPager;

    private TabLayout mTabLayout;
    private  List<Fragment> mFragments = new ArrayList<>();

    private List<OrderBean> mPendingList, mProcessList;

    public HomeFragment(){};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homeindex_layout,null);
        initview();
        return  view;
    }

    private void initview() {
        mViewPager = view.findViewById(R.id.home_viewpager);
        mTabLayout = view.findViewById(R.id.home_tabLayout);
        PendingFragment pendingFragment = new PendingFragment(mPendingList);
        ProcessingFragment processingFragment = new ProcessingFragment(mProcessList);
        mFragments.add(pendingFragment);
        mFragments.add(processingFragment);
        HomeFragmentAdapter myHomeFragmentAdapter = new HomeFragmentAdapter(getChildFragmentManager(),mFragments);
        mViewPager.setAdapter(myHomeFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText("待处理");
        mTabLayout.getTabAt(1).setText("进行中");
    }

    public HomeFragment(List<OrderBean> list1, List<OrderBean> list2) {
            mPendingList = list1;
            mProcessList = list2;
    }



    private class HomeFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        public HomeFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return  mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
