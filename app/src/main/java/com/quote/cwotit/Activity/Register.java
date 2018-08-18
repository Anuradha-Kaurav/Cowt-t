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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.quote.cwotit.Common.RequestQueueSingleton;
import com.quote.cwotit.Common.Urls;
import com.quote.cwotit.Common.Utils;
import com.quote.cwotit.R;

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

    KProgressHUD hud;

    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            ButterKnife.bind(this);

            requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

            hud = KProgressHUD.create(Register.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);


        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate] :: " + e);
            e.printStackTrace();
        }

    }

    @OnClick({R.id.btn_register, R.id.btn_reg_cencl})
    public void onViewClicked(View view) {
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        switch (view.getId()) {
            case R.id.btn_register:
                if (validation()) {
                    register();
                }
                break;
            case R.id.btn_reg_cencl:
                Intent intent = new Intent(Register.this, MainLogin.class);
                startActivity(intent);
                finish();
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
            regEmail.setError("Please enter email");
            regEmail.setFocusable(true);
            return false;
        } else if (str_name == null) {
            regName.setError("Please enter name");
            regName.setFocusable(true);
            return false;
        } else if (str_username == null) {
            regUsername.setError("Please enter user name");
            regUsername.setFocusable(true);
            return false;
        } else if (str_city == null) {
            regPhone.setError("Please enter city");
            regPhone.setFocusable(true);
            return false;
        } else if (str_password == null) {
            regPassword.setError("Please enter password");
            regPassword.setFocusable(true);
            return false;
        }
        return true;
    }

    public void register() {
        hud.show();
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
        String url = Urls.userRegistration;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hud.dismiss();
                        System.out.println(response.toString());
                        try {
                            String status = (String) response.get("status");
                            if (status.equalsIgnoreCase("success")) {

                                //Utils.snackBarWithAction(Register.this, "Please check inbox for email verification" );
                                Intent intent = new Intent(Register.this, MainLogin.class);
                                intent.putExtra("msg","Please check inbox for email verification" );
                                startActivity(intent);
                                finish();

                            } else {
                                if(response.getString("message").equalsIgnoreCase("Email id 34 already registered.")){
                                    Utils.simpleSnackBar(Register.this, "Email id is already registered");
                                }else if(response.getString("message").equalsIgnoreCase("User Name not available.")){
                                    Utils.simpleSnackBar(Register.this, "This user name is not available");
                                }else{
                                    Utils.simpleSnackBar(Register.this, response.getString("message"));
                                }
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
