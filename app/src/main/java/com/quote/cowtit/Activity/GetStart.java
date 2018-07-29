package com.quote.cowtit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.quote.cowtit.Common.SessionManager;
import com.quote.cowtit.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GetStart extends AppCompatActivity {

    @BindView(R.id.btn_getstart)
    Button btnGetstart;
    @BindView(R.id.txt_loginhere)
    LinearLayout txtLoginhere;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);
        ButterKnife.bind(this);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_USEREMAIl);

        // email
        String email = user.get(SessionManager.KEY_USERID);

    }

    @OnClick({R.id.btn_getstart, R.id.txt_loginhere})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getstart:
                Intent intent = new Intent(GetStart.this, Register.class);
                startActivity(intent);
                finish();
                break;
            case R.id.txt_loginhere:
                Intent intent1 = new Intent(GetStart.this, MainLogin.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                finish();
                break;
        }
    }

    /*@OnClick({R.id.btn_getstart, R.id.txt_loginhere})
    public void onViewClicked(View view) {
        switch (view.getUserId()) {
            case R.id.btn_getstart:
                Intent intent = new Intent(GetStart.this,Register.class);
                startActivity(intent);
                finish();
                break;
            case R.id.txt_loginhere:
                Intent intentLandPage = new Intent(GetStart.this,LandingPage.class);
                startActivity(intentLandPage);
                finish();
                break;

        }
    }*/


}
