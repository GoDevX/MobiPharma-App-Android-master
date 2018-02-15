package com.example.ahmedkhames.pharmacyclientapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      /*  if (position ==0) {
         //   return new AddOrderFragment();
        } else if (position == 1) {
        //    return new OrdersFragment();
        } else
            return null;
    }*/
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
}
