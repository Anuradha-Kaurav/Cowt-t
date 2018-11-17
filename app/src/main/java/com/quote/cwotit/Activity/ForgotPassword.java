package com.quote.cwotit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quote.cwotit.Common.RequestQueueSingleton;
import com.quote.cwotit.Common.Urls;
import com.quote.cwotit.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.btn_otp)
    Button btnOtp;
    @BindView(R.id.edt_otp)
    EditText edtOtp;
    @BindView(R.id.edt_newpassword)
    TextInputEditText edtNewpassword;
    @BindView(R.id.btn_updatepass)
    Button btnUpdatepass;
    @BindView(R.id.btn_cancel)
    Button btnCencal;
    private static String TAG = ForgotPassword.class.getSimpleName();
    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";
    static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forgot_password);

            ButterKnife.bind(this);
            requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate]:: " + e);
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_otp, R.id.btn_updatepass, R.id.btn_cancel})
    public void onViewClicked(View view) {
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        switch (view.getId()) {
            case R.id.btn_otp:
                if (validationotp()) {
                    sendotp();
                }
                break;
            case R.id.btn_updatepass:
                if (validatioupdate()) {
                    updatePassword();
                }
                break;
            case R.id.btn_cancel:
                Intent intent = new Intent(ForgotPassword.this, MainLogin.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public boolean validationotp() {
        try {
            String str_email = edtEmail.getText().toString();
            if (str_email == null) {
                edtEmail.setError("Please enter first name");
                edtEmail.setFocusable(true);
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate]:: " + e);
            e.printStackTrace();
        }
        return true;
    }

    public boolean validatioupdate() {
        try {
            String str_otp = edtOtp.getText().toString();
            String str_newpass = edtNewpassword.getText().toString();
            if (str_otp == null) {
                edtOtp.setError("Please enter otp");
                edtOtp.setFocusable(true);
                return false;
            } else if (str_newpass == null) {
                edtNewpassword.setError("Please enter new password");
                edtNewpassword.setFocusable(true);
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate]:: " + e);
            e.printStackTrace();
        }
        return true;
    }

    public void updatePassword() {
        JSONObject json = new JSONObject();
        try {
            json.put("emailId", email);
            ;
            json.put("password", edtNewpassword.getText().toString());
            json.put("OTP", edtOtp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Urls.validateOTP;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            String status = (String) response.get("status");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(ForgotPassword.this, "Check your Email", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgotPassword.this, MainLogin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ForgotPassword.this, response.get("message").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getting response");
            }
        });
        jsonObjectRequest.setTag(REQ_TAG);
        requestQueue.add(jsonObjectRequest);
    }

    public void sendotp() {
        JSONObject json = new JSONObject();
        try {
            email = edtEmail.getText().toString();
            json.put("emailId", email);
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Urls.generateOTP;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            String status = (String) response.get("status");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(ForgotPassword.this, "Check your Email", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ForgotPassword.this, response.get("message").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getting response");
            }
        });
        jsonObjectRequest.setTag(REQ_TAG);
        requestQueue.add(jsonObjectRequest);
    }


}
