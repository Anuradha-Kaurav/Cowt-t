package com.quote.cowtit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.quote.cowtit.Adapter.ViewPagerAdapter;
import com.quote.cowtit.Common.SessionManager;
import com.quote.cowtit.Fragment.HomeFragment;
import com.quote.cowtit.Fragment.MoodFragment;
import com.quote.cowtit.Fragment.ProfileFragment;
import com.quote.cowtit.Fragment.SearchFragment;
import com.quote.cowtit.POJO.CowitMoodPOJO;
import com.quote.cowtit.R;




public class DashBoard extends AppCompatActivity {

    static String TAG = DashBoard.class.getCanonicalName();

    SessionManager sessionManager;
    static final String REQ_TAG = "VACTIVITY";
    RequestQueue requestQueue;
    String url = "http://uservission.com/cw/fetchCategory.php";
    CowitMoodPOJO cowitMoodPOJO;
    private Toolbar toolbar;
    ImageView icLogo;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_smile,
            R.drawable.ic_search,
            R.drawable.ic_profile
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_deshbord);

            sessionManager = new SessionManager(getApplicationContext());
            toolbar = (Toolbar) findViewById(R.id.toolbar_deshbord);
            setSupportActionBar(toolbar);
            toolbar.setTitle("");
            toolbar.setSubtitle("");

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();


        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate] :: " + e);
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_deshbord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            Log.i(TAG, "In [onOptionsItemSelected] :: sessionManager :: " + sessionManager.toString());
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.action_settings:
                    sessionManager.logoutUser();
                    Intent intent = new Intent(DashBoard.this, MainLogin.class);
                    startActivity(intent);
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in [onOptionsItemSelected] :: " + e);
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {


        try {
            Log.i(TAG, "In [setupViewPager] ::  ");

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new HomeFragment(getApplicationContext()), "");
            adapter.addFragment(new MoodFragment(getApplicationContext()), "");
            adapter.addFragment(new SearchFragment(getApplicationContext()), "");
            adapter.addFragment(new ProfileFragment(getApplicationContext()), "");

            viewPager.setAdapter(adapter);

        } catch (Exception e) {
            Log.e(TAG, "Exception in [setupViewPager] ::  " + e);
            e.printStackTrace();
        }


    }

    private void setupTabIcons() {

        try {
            Log.i(TAG, "In [setupTabIcons] ::  ");
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        } catch (Exception e) {
            Log.e(TAG, "Exception in [setupTabIcons] ::  " + e);
            e.printStackTrace();
        }


    }

}