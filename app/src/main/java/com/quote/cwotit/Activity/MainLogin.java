package com.quote.cwotit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.quote.cwotit.Common.RequestQueueSingleton;
import com.quote.cwotit.Common.SessionManager;
import com.quote.cwotit.Common.Urls;
import com.quote.cwotit.Common.Utils;
import com.quote.cwotit.POJO.ProfilePOJO;
import com.quote.cwotit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainLogin extends AppCompatActivity {

    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txt_forgotpass)
    TextView txtForgotpass;
    @BindView(R.id.txt_signuphere)
    LinearLayout txtSignuphere;
    private static String TAG = MainLogin.class.getSimpleName();
    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton loginButton;
    KProgressHUD hud;

    JSONObject fbres;
    //ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main_login);

            ButterKnife.bind(this);
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
            callbackManager = CallbackManager.Factory.create();

            getKeyHash(MainLogin.this);

            Intent i = getIntent();
            String msg = i.getStringExtra("msg");
            if(msg!= null){
                Utils.snackBarWithAction(MainLogin.this, msg);
            }

//            progressdialog = ProgressDialog.show(MainLogin.this, null, null, true);
//            progressdialog.setContentView(R.layout.elemento_progress_splash);
//            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            progressdialog.dismiss();

            hud = KProgressHUD.create(MainLogin.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);

            loginButton = findViewById(R.id.fb_login_btn );
            loginButton.setReadPermissions(Arrays.asList(EMAIL));
            callbackManager = CallbackManager.Factory.create();

//            LoginButton loginButton = findViewById(R.id.login_button);
//            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    Log.i("FB", "loginResult : "+loginResult);
                    getUserDetails(loginResult);
                }

                @Override
                public void onCancel() {
                    // App code
                    Utils.simpleSnackBar(MainLogin.this, "Request cancelled");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Utils.simpleSnackBar(MainLogin.this, exception.getMessage());
                }
            });
            // If you are using in a fragment, call loginButton.setFragment(this);
            requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();


        } catch (Exception e) {
            Log.e("TAG", "Exception in [MainLogin] :: [onCreate] :: " + e);
            e.printStackTrace();
            Utils.simpleSnackBar(MainLogin.this, e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_login, R.id.txt_forgotpass, R.id.txt_signuphere})
    public void onViewClicked(View view) {

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        switch (view.getId()) {
            case R.id.btn_login:
                //Crashlytics.getInstance().crash();
                if(Utils.isValidEmail(edtUsername.getText().toString())){
                    if(!edtPassword.getText().toString().isEmpty()){
                        login();
                    }else{
                        edtPassword.setError("Enter password");
                    }
                }else{
                    edtUsername.setError("Enter valid email id");
                }
                break;
            case R.id.txt_forgotpass:
                Intent intentf = new Intent(MainLogin.this, ForgotPassword.class);
                startActivity(intentf);
                break;
            case R.id.txt_signuphere:
                Intent i = new Intent(MainLogin.this, Register.class);
                startActivity(i);
                break;
        }
    }

    public void login() {

        if(Utils.isNetworkAvailable(getApplicationContext())) {
            hud.show();
            JSONObject json = new JSONObject();
            try {
                json.put("emailId", edtUsername.getText().toString());
                json.put("password", edtPassword.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = Urls.userLogin;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try {
                                String id = null, emailId = null;
                                hud.dismiss();
                                String status = (String) response.get("status");
                                if (status.equalsIgnoreCase("success")) {
                                    JSONObject jsonObjectprofile = (JSONObject) response.get("profile");
                                    id = (String) jsonObjectprofile.get("id");
                                    emailId = (String) jsonObjectprofile.get("emailId");

                                    userLogin(id, emailId);

                                } else {
                                    Utils.simpleSnackBar(MainLogin.this, response.get("message").toString());
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hud.dismiss();
                    System.out.println("Error getting response");
                }
            });
            jsonObjectRequest.setTag(REQ_TAG);
            requestQueue.add(jsonObjectRequest);
        }else{
            Utils.simpleSnackBar(MainLogin.this, "Please network connection");
        }
    }

    protected void getUserDetails(LoginResult loginResult) {
        try {
            GraphRequest data_request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject json_object, GraphResponse response) {
                            Log.i("FB", "getUserDetails : "+json_object);
                            registerfb(json_object);
                        }

                    });
            Bundle permission_param = new Bundle();
            permission_param.putString("fields", "id,name,email,picture.width(500).height(500)");
            data_request.setParameters(permission_param);
            data_request.executeAsync();
        } catch (Exception e) {
            Log.e("TAG", "Exception in [MainLogin] :: [getUserDetails] :: " + e);
            e.printStackTrace();
        }

    }

    public void registerfb(JSONObject json_object) {

        if(Utils.isNetworkAvailable(getApplicationContext())) {
            try {
                fbres = json_object;
                JSONObject json = new JSONObject();
                try {
                    json.put("emailId", fbres.get("email"));
                    json.put("firstName", fbres.get("name"));
                    json.put("facebookUserId", fbres.get("id"));
                    json.put("emailConfirm", "1");
                    json.put("userType", "Facebook");
                    Log.d("REG", "Post data : " + json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = Urls.userRegistration;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("REG", " resp : " + response.toString());
                                ProfilePOJO profilePOJO = new ProfilePOJO();
                                try {
                                    String id, emailId;
                                    String status = (String) response.get("status");
                                    String message = (String) response.get("message");

                                    if (message.equalsIgnoreCase("User Registered") || message.contains("already registered")) {
                                        id = (String) response.get("userId");
                                        emailId = (String) response.get("emailId");
                                        //login with fb
                                        userLogin(id, emailId); // hit login service and save the session
                                    } else {
                                        Utils.simpleSnackBar(MainLogin.this, "Something went wrong");
                                    }


//                                if (status.equalsIgnoreCase("success")) {
//                                    JSONObject profile = (JSONObject) response.get("profile");
//                                    String id = (String) profile.get("userId");
//                                    String emailId = (String) profile.get("emailId");
//                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                    sessionManager.createLoginSession(emailId, id);
//                                    Intent intent = new Intent(MainLogin.this, DashBoard.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                } else if (status.equalsIgnoreCase("error")) {
//                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                    sessionManager.createLoginSession(fbres.get("email").toString(), fbres.get("id").toString());
//                                    Intent intent = new Intent(MainLogin.this, DashBoard.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hud.dismiss();
                        System.out.println("Error getting response");
                    }
                });
                jsonObjectRequest.setTag(REQ_TAG);
                requestQueue.add(jsonObjectRequest);

            } catch (Exception e) {
                Log.e(TAG, "Exceptionin [registerfb] :: " + e);
                e.printStackTrace();
            }
        }
    }

    private void userLogin(String id, String emailId){

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.createLoginSession(emailId, id);
        Intent intent = new Intent(MainLogin.this, DashBoard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

    }
    private void getKeyHash(Context context){
            PackageInfo packageInfo;
            String key = null;
            try {
                //getting application package name, as defined in manifest
                String packageName = context.getApplicationContext().getPackageName();

                //Retriving package info
                packageInfo = context.getPackageManager().getPackageInfo(packageName,
                        PackageManager.GET_SIGNATURES);

                Log.e("Package Name=", context.getApplicationContext().getPackageName());

                for (Signature signature : packageInfo.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    key = new String(Base64.encode(md.digest(), 0));

                    // String key = new String(Base64.encodeBytes(md.digest()));
                    Log.e("Key Hash=", key);
                }
            } catch (PackageManager.NameNotFoundException e1) {
                Log.e("Name not found", e1.toString());
            }
            catch (NoSuchAlgorithmException e) {
                Log.e("No such an algorithm", e.toString());
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }
        }

}