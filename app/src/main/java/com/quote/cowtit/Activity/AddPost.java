package com.quote.cowtit.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quote.cowtit.Common.RequestQueueSingleton;
import com.quote.cowtit.Common.SessionManager;
import com.quote.cowtit.POJO.AllPostPOJO;
import com.quote.cowtit.POJO.ProfilePOJO;
import com.quote.cowtit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddPost extends AppCompatActivity {
    RecyclerView recyclerView;
    String urlpost = "http://uservission.com/cw/createPost.php";
    private DishAdapter dAdapter;
    private AllPostPOJO allPostPOJO;
    private TextView txtPost;
    private MultiAutoCompleteTextView edtpost;
    public ArrayList<AllPostPOJO> allPostPOJOArrayList = new ArrayList<>();
    private static final String TAG = AddPost.class.getSimpleName();

    String chatid, useriD;
    SessionManager sessionManager;
    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";

    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Intent intent = getIntent();
//        chatid = intent.getStringExtra("chatid");
         allPostPOJOArrayList = (ArrayList<AllPostPOJO>) intent.getSerializableExtra("allPostPOJOArrayList");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        useriD = user.get(SessionManager.KEY_USERID);
        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();
        txtPost = (TextView) findViewById(R.id.txt_post_con);
        edtpost = (MultiAutoCompleteTextView) findViewById(R.id.edt_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.rec_allpost);
        initViews();

        txtPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postdata = edtpost.getText().toString();

                if (postdata.isEmpty()){
                    edtpost.setError("Please enter your post");
                    edtpost.requestFocus();
                 }else {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("categoryId", chatid);
                        json.put("userId", useriD);
                        json.put("post", edtpost.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlpost, json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println(response.toString());
                                    Toast.makeText(AddPost.this, "Post Successfull", Toast.LENGTH_LONG).show();
                                    allPostPOJOArrayList.clear();
                                    Intent intent1 = new Intent(AddPost.this, DashBoard.class);
                                    startActivity(intent1);
                                    finish();
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
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(AddPost.this, AddPost.class);  //your class
        startActivity(i);
        finish();

    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dAdapter = new DishAdapter(getApplicationContext(), allPostPOJOArrayList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dAdapter);
    }

    class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

        private List<AllPostPOJO> dishList;
        private Context context;

        public class DishViewHolder extends RecyclerView.ViewHolder {
            public TextView txtpost, txtlike, txtdislike, testusermode;
            public ImageView imgdot, imglike, imgdislike;

            public DishViewHolder(View view) {
                super(view);
                txtpost = (TextView) view.findViewById(R.id.txt_post);
                txtlike = (TextView) view.findViewById(R.id.txt_like);
                txtdislike = (TextView) view.findViewById(R.id.txt_dislike);
                testusermode = (TextView) view.findViewById(R.id.txt_usermode);
                imgdot = (ImageView) view.findViewById(R.id.img_dot);
                imglike = (ImageView) view.findViewById(R.id.img_lick);
                imgdislike = (ImageView) view.findViewById(R.id.img_dislike);
            }
        }

        public DishAdapter(Context context, List<AllPostPOJO> dishList) {
            this.context = context;
            this.dishList = dishList;
        }

        private DishAdapter.DishViewHolder holder;

        @Override
        public DishAdapter.DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_adapter, parent, false);
            return new DishAdapter.DishViewHolder(itemView);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(final DishAdapter.DishViewHolder holder, int position) {
            final AllPostPOJO dishPOJO = dishList.get(position);
            holder.txtpost.setText("\"" + dishPOJO.getPost() + "\"" + "   -" + dishPOJO.getFirstName());
            holder.txtlike.setText(dishPOJO.getLikes());
            holder.txtdislike.setText(dishPOJO.getDislike());
//            holder.testusername.setText(dishPOJO.getFirstName());
            holder.testusermode.setText("(" + dishPOJO.getCategory() + ")");
            holder.imgdot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(context, holder.imgdot);
                    popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int i = item.getItemId();
                            if (i == R.id.action1) {
                                //do something
                                follow(dishPOJO.getUserId().toString());
                                return true;
                            } else if (i == R.id.action2) {
//                                reCowtit(dishPOJO.getUserId().toString());

                                return true;
                            } else {
                                return onMenuItemClick(item);
                            }
                        }
                    });
                    popup.show();
                }
            });
            holder.imglike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.imglike.isClickable()) {
                        if (holder.imgdislike.isClickable()) {
                            like(dishPOJO.getUserId().toString());
                            holder.imglike.setClickable(false);
                            int i = Integer.valueOf(dishPOJO.getLikes().toString());
                            i++;
                            holder.txtlike.setText(String.valueOf(i));
                        } else {
                            like(dishPOJO.getUserId().toString());
                            holder.imglike.setClickable(false);
                            holder.imgdislike.setClickable(true);
                            int i = Integer.valueOf(dishPOJO.getLikes().toString());
                            i++;
                            holder.txtlike.setText(String.valueOf(i));
                            int j = Integer.valueOf(holder.txtdislike.getText().toString());
                            j--;
                            holder.txtdislike.setText(String.valueOf(j));
                        }

                    }
                }
            });
            holder.imgdislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.imgdislike.isClickable()) {
                        if (holder.imglike.isClickable()) {
                            dislike(dishPOJO.getUserId().toString());
                            holder.imgdislike.setClickable(false);
                            holder.imglike.setClickable(true);
                            int i = Integer.valueOf(dishPOJO.getDislike().toString());
                            i++;
                            holder.txtdislike.setText(String.valueOf(i));
                        } else {
                            dislike(dishPOJO.getUserId().toString());
                            holder.imgdislike.setClickable(false);
                            holder.imglike.setClickable(true);
                            int i = Integer.valueOf(dishPOJO.getDislike().toString());
                            i++;
                            holder.txtdislike.setText(String.valueOf(i));
                            int j = Integer.valueOf(holder.txtlike.getText().toString());
                            j--;
                            holder.txtlike.setText(String.valueOf(j));
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dishList.size();
        }
    }


    public void like(String postid) {
        String postUserId = postid;
        String likeurl = "http://uservission.com/cw/increaseLike.php";
        JSONObject json = new JSONObject();
        try {
            json.put("userId", useriD);
            json.put("followedUserId", postUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, likeurl, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            String status = (String) response.get("status");
                            if (status.equalsIgnoreCase("success")) {
//                                Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
//                                objectlike = response;
                            } else if (status.equalsIgnoreCase("error")) {
                                Toast.makeText(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
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

    public void dislike(String postid) {
        String postUserId = postid;
        String dislike = "http://govermentsservices.com/Comenty/cowcit/increaseDisLike.php";
        JSONObject json = new JSONObject();
        try {
            json.put("userId", useriD);
            json.put("followedUserId", postUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dislike, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        ProfilePOJO profilePOJO = new ProfilePOJO();
                        try {
                            String status = (String) response.get("status");
                            if (status.equalsIgnoreCase("success")) {
//                                Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                            } else if (status.equalsIgnoreCase("error")) {
                                Toast.makeText(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
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

    public void follow(String postid) {
        String postUserId = postid;
        String flowurl = "http://uservission.com/cw/userFollow.php";
        JSONObject json = new JSONObject();
        try {
            json.put("userId", useriD);
            json.put("followedUserId", postUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, flowurl, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        ProfilePOJO profilePOJO = new ProfilePOJO();
                        try {
                            String status = (String) response.get("status");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                            } else if (status.equalsIgnoreCase("error")) {
                                Toast.makeText(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
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


