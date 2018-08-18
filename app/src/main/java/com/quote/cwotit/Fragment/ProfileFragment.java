package com.quote.cwotit.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.quote.cwotit.Common.RequestQueueSingleton;
import com.quote.cwotit.Common.SessionManager;
import com.quote.cwotit.Common.Urls;
import com.quote.cwotit.POJO.ProfilePOJO;
import com.quote.cwotit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    private static String TAG = ProfileFragment.class.getCanonicalName();
    Context context;
    String useriD;
    ImageView profilePic;
    TextView userName, quotesCount, followersCount, followingCount, update;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    ProgressDialog progressdialog;
    static final String REQ_TAG = "VACTIVITY";
    KProgressHUD hud;

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

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.profile_fragment, container, false);
            profilePic = view.findViewById(R.id.profile_pic);
            userName = view.findViewById(R.id.profile_name);
            quotesCount = view.findViewById(R.id.quotes_count);
            followersCount = view.findViewById(R.id.followers_count);
            followingCount = view.findViewById(R.id.following_count);
            requestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();
            sessionManager = new SessionManager(getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            useriD = user.get(SessionManager.KEY_USERID);
            update = (TextView) view.findViewById(R.id.btn_udate_profile);

//            progressdialog = ProgressDialog.show(getContext(), null, null, true);
//            progressdialog.setContentView(R.layout.elemento_progress_splash);
//            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            progressdialog.show();



            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   // getProfile();

                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreateView] :: " + e);
            e.printStackTrace();
        }
        getProfile();
        return view;
    }


    public void getProfile() {
        try {
            JSONObject json = new JSONObject();
            try {
                json.put("userId", useriD);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.getProfile, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            //progressdialog.dismiss();
                            hud.dismiss();
                            try {
                                String status = (String) response.get("status");
                                if (status.equalsIgnoreCase("success")) {
                                    ProfilePOJO profilePOJO = new ProfilePOJO();
                                    JSONObject jsonObject = response.getJSONObject("profile");
                                    profilePOJO.setEmailId(jsonObject.get("emailId").toString());
                                    profilePOJO.setFirstName(jsonObject.get("firstName").toString());
                                    profilePOJO.setMobileNumber(jsonObject.get("mobileNumber").toString());
                                    profilePOJO.setUserName(jsonObject.get("userName").toString());
//                                    Intent intent1 = new Intent(getContext(), UpdateProfile.class);
//                                    intent1.putExtra("profilePOJO", profilePOJO);
//                                    startActivity(intent1);


                                    userName.setText(profilePOJO.getFirstName());
                                    quotesCount.setText("0");
                                    followersCount.setText("0");
                                    followingCount.setText("0");
//                                    if(jsonObject.getString("followers")!=null && !jsonObject.getString("followers").toString().equals(JSONObject.NULL)){
//                                        followersCount.setText(jsonObject.get("followers").toString());
//                                    }else{
//                                        followersCount.setText("0");
//                                    }
//                                    if(jsonObject.getString("following").toString()!=null && !jsonObject.getString("following").toString().equals(JSONObject.NULL)){
//                                        followingCount.setText(jsonObject.get("following").toString());
//                                    }else{
//                                        followingCount.setText("0");
//                                    }

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
