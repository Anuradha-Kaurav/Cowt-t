package com.quote.cwotit.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.quote.cwotit.Common.SessionManager;
import com.quote.cwotit.Fragment.SubFragment.AllFragment;
import com.quote.cwotit.Fragment.SubFragment.MoodSubFragment;
import com.quote.cwotit.Fragment.SubFragment.WriterFragment;
import com.quote.cwotit.R;

public class SearchFragment extends Fragment {

    private static String TAG = SearchFragment.class.getCanonicalName();
    Button update;
    SessionManager sessionManager;
    String useriD;
    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";

    public SearchFragment() {
        // Required empty public constructor
    }

    Context context;
    ProgressDialog progressdialog;
    @SuppressLint("ValidFragment")
    public SearchFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.container_main);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        return view;

    }
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AllFragment(context);
                case 1:
                    return new MoodSubFragment(context);
                default:
                    return new WriterFragment(context);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Writer";
                default:
                    return "Mood";
            }
        }
    }

}
