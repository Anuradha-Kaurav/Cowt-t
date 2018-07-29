package com.quote.cowtit.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.quote.cowtit.Fragment.*;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static String TAG = ViewPagerAdapter.class.getCanonicalName();
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new HomeFragment(getApplicationContext());
            case 1:
                return new MoodFragment(getApplicationContext());
            case 2:
                return new SearchFragment(getApplicationContext());

            default:

                return new ProfileFragment(getApplicationContext());
        }

    }


    @Override
    public int getCount() {
        try {
            Log.i(TAG, "In [getCount] ::  ");
        } catch (Exception e) {
            Log.e(TAG, "Exception in [getCount] ::  " + e);
            e.printStackTrace();
        }
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {
            Log.i(TAG, "In [getPageTitle] ::  ");
        } catch (Exception e) {
            Log.e(TAG, "Exception in [getPageTitle] ::  " + e);
            e.printStackTrace();
        }
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        try {
            Log.i(TAG, "In [addFragment] ::  ");

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        } catch (Exception e) {
            Log.e(TAG, "Exception in [addFragment] ::  " + e);
            e.printStackTrace();
        }
    }
}
