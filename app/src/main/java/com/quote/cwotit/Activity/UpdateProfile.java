package com.quote.cwotit.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.quote.cwotit.Common.SessionManager;
import com.quote.cwotit.Message.MessageManager;
import com.quote.cwotit.Message.MessageType;
import com.quote.cwotit.POJO.ProfilePOJO;
import com.quote.cwotit.POJO.UpdateUserPOJO;
import com.quote.cwotit.R;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UpdateProfile extends AppCompatActivity {


    private static String TAG = UpdateProfile.class.getCanonicalName();

    @BindView(R.id.btn_updateprofile)
    Button btnUpdateprofile;
    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";
    String updateprofile = "http://uservission.com/cw/updateProfile.php";
    SessionManager sessionManager;
    JSONObject jsonObject;
    ProfilePOJO profilePOJO;
    @BindView(R.id.update_email)
    TextView updateEmail;
    @BindView(R.id.edt_up_name)
    EditText edtUpName;
    @BindView(R.id.edt_up_Uname)
    EditText edtUpUname;
    @BindView(R.id.edt_up_phone_no)
    EditText edtUpPhoneNo;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_update_profile);
            ButterKnife.bind(this);
            Intent intent = getIntent();
            profilePOJO = (ProfilePOJO) intent.getSerializableExtra("profilePOJO");

            updateEmail.setText(profilePOJO.getEmailId().toString() != null ? profilePOJO.getEmailId().toString() : null);
            edtUpName.setText(profilePOJO.getFirstName().toString() != null ? profilePOJO.getFirstName().toString() : null);
            edtUpUname.setText(profilePOJO.getUserName().toString() != null ? profilePOJO.getUserName().toString() : null);
            edtUpPhoneNo.setText(profilePOJO.getMobileNumber().toString() != null ? profilePOJO.getMobileNumber().toString() : null);
            sessionManager = new SessionManager(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate] ::" + e);
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn_updateprofile)
    public void onViewClicked() {

        updateprofile();
    }

    public void updateprofile() {
        try {
            progressdialog = ProgressDialog.show(UpdateProfile.this, null, null, true);
            progressdialog.setContentView(R.layout.elemento_progress_splash);
            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
           // progressdialog.show();
            HashMap<String, String> user = sessionManager.getUserDetails();
            String useriD = user.get(SessionManager.KEY_USERID);
            UpdateUserPOJO updateUserPOJO = new UpdateUserPOJO();
            updateUserPOJO.setFirstName(edtUpName.getText().toString());
            updateUserPOJO.setMobileNumber(edtUpPhoneNo.getText().toString());
            updateUserPOJO.setUserName(edtUpUname.getText().toString());
            updateUserPOJO.setUserId(useriD);
            ReqUpdateProfile reqUpdateProfile = new ReqUpdateProfile();
            reqUpdateProfile.execute(MessageType.POST, updateprofile, updateUserPOJO);
        } catch (Exception e) {
            Log.e(TAG, "Exception in [updateprofile] ::" + e);
            e.printStackTrace();
        }
    }

    class ReqUpdateProfile extends MessageManager {

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("OldDishesMessage.onPostExecute()" + result);
            progressdialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);

                String status = (String) jsonObject.get("status");
                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(UpdateProfile.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UpdateProfile.this, DashBoard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateProfile.this, "Update is not succesfull", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

                Log.e(TAG, "Exception in [ReqUpdateProfile] :: " + e);
                e.printStackTrace();
            }
        }
    }
}
