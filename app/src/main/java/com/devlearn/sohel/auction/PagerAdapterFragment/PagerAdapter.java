package com.devlearn.sohel.auction.PagerAdapterFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.devlearn.sohel.auction.MyBids.MyBidsFragment;
import com.devlearn.sohel.auction.MyItems.MyItemsFragment;
import com.devlearn.sohel.auction.OngoingBids.OngoingBidsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                OngoingBidsFragment ongoingBidsFragment = new OngoingBidsFragment();
                return ongoingBidsFragment;
            case 1:
                MyBidsFragment myBidsFragment = new MyBidsFragment();
                return myBidsFragment;
            case 2:
                MyItemsFragment myItemsFragment = new MyItemsFragment();
                return myItemsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
