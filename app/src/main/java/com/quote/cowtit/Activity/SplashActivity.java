package com.quote.cowtit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.quote.cowtit.R;

public class SplashActivity extends AppCompatActivity {


    public static String TAG = SplashActivity.class.getCanonicalName();
    private Toolbar toolbar;
    ImageView icLogo;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {

                Intent intent1 = new Intent(SplashActivity.this, GetStart.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                finish();

            } catch (Exception e) {
                Log.e("TAG", "Exception in [SplashActivity] :: [run] :: " + e);
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.i(TAG, "In [onCreate] :: ");
            setContentView(R.layout.activity_main);
            /*toolbar = (Toolbar) findViewById(R.id.toolbar);
            icLogo = (ImageView) toolbar.findViewById(R.id.iv_logo);
            setSupportActionBar(toolbar);*/

            handler.postDelayed(runnable, 2000);

        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate] :: " + e);
            e.printStackTrace();
        }

    }
}
