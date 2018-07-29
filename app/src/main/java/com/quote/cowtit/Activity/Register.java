package com.quote.cowtit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quote.cowtit.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {

    String TAG = Register.class.getCanonicalName();
    @BindView(R.id.reg_email)
    EditText regEmail;
    @BindView(R.id.reg_name)
    EditText regName;
    @BindView(R.id.reg_username)
    EditText regUsername;
    @BindView(R.id.reg_phone)
    EditText regPhone;
    @BindView(R.id.reg_password)
    TextInputEditText regPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_reg_cencl)
    Button btnRegCencl;

    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            ButterKnife.bind(this);


        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate] :: " + e);
            e.printStackTrace();
        }

    }


    @OnClick({R.id.btn_register, R.id.btn_reg_cencl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (validation()) {
                    register();
                }
                break;
            case R.id.btn_reg_cencl:
                Intent intent = new Intent(Register.this, MainLogin.class);
                startActivity(intent);
                break;
        }
    }

    public boolean validation() {
        String str_email = regEmail.getText().toString();
        String str_name = regName.getText().toString();
        String str_username = regUsername.getText().toString();
        String str_city = regPhone.getText().toString();
        String str_password = regPassword.getText().toString();

        if (str_email == null) {
            regEmail.setError("Please enter first name");
            regEmail.setFocusable(true);
            return false;
        } else if (str_name == null) {
            regName.setError("Please enter first name");
            regName.setFocusable(true);
            return false;
        } else if (str_username == null) {
            regUsername.setError("Please enter first name");
            regUsername.setFocusable(true);
            return false;
        } else if (str_city == null) {
            regPhone.setError("Please enter first name");
            regPhone.setFocusable(true);
            return false;
        } else if (str_password == null) {
            regPassword.setError("Please enter first name");
            regPassword.setFocusable(true);
            return false;
        }
        return true;
    }


    public void register() {
        JSONObject json = new JSONObject();
        try {
            json.put("emailId", regEmail.getText().toString());
            json.put("userName", regUsername.getText().toString());
            json.put("firstName", regName.getText().toString());
            json.put("mobileNumber", regPhone.getText().toString());
            json.put("password", regPassword.getText().toString());
            json.put("userType", "Email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://uservission.com/cw/userRegister.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            String status = (String) response.get("status");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(Register.this, "Register sucessful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, MainLogin.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, response.get("message").toString(), Toast.LENGTH_LONG).show();
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
