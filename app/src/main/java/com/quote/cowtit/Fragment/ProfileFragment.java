package com.quote.cowtit.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quote.cowtit.Activity.UpdateProfile;
import com.quote.cowtit.Common.RequestQueueSingleton;
import com.quote.cowtit.Common.SessionManager;
import com.quote.cowtit.POJO.ProfilePOJO;
import com.quote.cowtit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    private static String TAG = ProfileFragment.class.getCanonicalName();
    Context context;
    String useriD;
    Button update;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    ProgressDialog progressdialog;
    static final String REQ_TAG = "VACTIVITY";

    @SuppressLint("ValidFragment")
    public ProfileFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.profile_fragment, container, false);
            requestQueue = RequestQueueSingleton.getInstance(getContext())
                    .getRequestQueue();
            sessionManager = new SessionManager(getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            useriD = user.get(SessionManager.KEY_USERID);
            update = (Button) view.findViewById(R.id.btn_udate_profile);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressdialog = ProgressDialog.show(getContext(), null, null, true);
                    progressdialog.setContentView(R.layout.elemento_progress_splash);
                    progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressdialog.show();
                    getProfile();

                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreateView] :: " + e);
            e.printStackTrace();
        }
        return view;
    }


    public void getProfile() {
        try {
            String getprofile = "http://uservission.com/cw/getProfile.php";
            JSONObject json = new JSONObject();
            try {
                json.put("userId", useriD);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getprofile, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            progressdialog.dismiss();
                            try {
                                String status = (String) response.get("status");
                                if (status.equalsIgnoreCase("success")) {
                                    ProfilePOJO profilePOJO = new ProfilePOJO();
                                    JSONObject jsonObject = response.getJSONObject("profile");
                                    profilePOJO.setEmailId(jsonObject.get("emailId").toString());
                                    profilePOJO.setFirstName(jsonObject.get("firstName").toString());
                                    profilePOJO.setMobileNumber(jsonObject.get("mobileNumber").toString());
                                    profilePOJO.setUserName(jsonObject.get("userName").toString());
                                    Intent intent1 = new Intent(getContext(), UpdateProfile.class);
                                    intent1.putExtra("profilePOJO", profilePOJO);
                                    startActivity(intent1);

                                } else if (status.equalsIgnoreCase("error")) {
                                    Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
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
        } catch (Exception e) {
            Log.e(TAG, "Exception in [getProfile] :: " + e);
            e.printStackTrace();
        }
    }


}
