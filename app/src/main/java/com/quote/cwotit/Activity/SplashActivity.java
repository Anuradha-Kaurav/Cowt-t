package com.quote.cwotit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.quote.cwotit.Common.SessionManager;
import com.quote.cwotit.R;

public class SplashActivity extends AppCompatActivity {

    public static String TAG = SplashActivity.class.getCanonicalName();
    // Session Manager Class
    SessionManager session;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Intent intent = null;
                if(session.isLoggedIn()){
                     intent = new Intent(SplashActivity.this, DashBoard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }else{
                    intent = new Intent(SplashActivity.this, GetStart.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
                startActivity(intent);
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

            // Session class instance
            session = new SessionManager(getApplicationContext());

            handler.postDelayed(runnable, 2000);

        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate] :: " + e);
            e.printStackTrace();
        }

    }
}
