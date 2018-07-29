package com.quote.cowtit.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quote.cowtit.Common.RequestQueueSingleton;
import com.quote.cowtit.Common.SessionManager;
import com.quote.cowtit.Message.MessageManager;
import com.quote.cowtit.Message.MessageType;
import com.quote.cowtit.POJO.AllPostPOJO;
import com.quote.cowtit.POJO.ProfilePOJO;
import com.quote.cowtit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private static String TAG = HomeFragment.class.getCanonicalName();
    String url = "http://uservission.com/cw/fetchHighestLikedQuotes.php";
    private DishAdapter dAdapter;
    private AllPostPOJO allPostPOJO;
    SessionManager sessionManager;
    public String useriD;
    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";
    public ArrayList<AllPostPOJO> allPostPOJOArrayList = new ArrayList<>();
    JSONObject objectlike;
    Context context;
    RecyclerView list;

    private final static int INTERVAL = 1000 * 60 * 5; //2 minutes
    Handler mHandler = new Handler();


    @SuppressLint("ValidFragment")
    public HomeFragment(Context context) {

        try {
            Log.i(TAG, "In [HomeFragment] [Paramerized] [Constructor] ::  ");

            this.context = context;

        } catch (Exception e) {
            Log.e(TAG, "Exception in [HomeFragment]  [Paramerized] [Constructor]::  " + e);
            e.printStackTrace();
        }


        // Required empty public constructor
    }

    public HomeFragment() {

        try {
            Log.i(TAG, "In [HomeFragment] [Empty] [Constructor] ::  ");
        } catch (Exception e) {
            Log.e(TAG, "Exception in [HomeFragment] [Empty] [Constructor] ::  " + e);
            e.printStackTrace();
        }

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.i(TAG, "Exception in [onCreate] :: ");


        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreate] :: " + e);
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        try {
            Log.i(TAG, "In [onCreateView] ::  ");

            view = inflater.inflate(R.layout.home_fragment, container, false);
            list = (RecyclerView) view.findViewById(R.id.list_post);
            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sevp);
            // Inflate the layout for this fragment
            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            allPostPOJOArrayList.clear();
//                        allPostPOJOArrayList.removeAll(allPostPOJOArrayList);
                            // This method performs the actual data-refresh operation.
                            // The method calls setRefreshing(false) when it's finished.
                            getData();// call what you want to update in this method
                            swipeRefreshLayout.setRefreshing(false);
                            dAdapter.notifyDataSetChanged();
                        }
                    }
            );
            sessionManager = new SessionManager(context);
            HashMap<String, String> user = sessionManager.getUserDetails();
            useriD = user.get(SessionManager.KEY_USERID);

            requestQueue = RequestQueueSingleton.getInstance(context)
                    .getRequestQueue();
            getData();

        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreateView] :: " + e);
            e.printStackTrace();
        }
        return view;

    }

    public void getData() {
        try {
            Userdesh userdesh = new Userdesh();
            userdesh.execute(MessageType.GET, url);
        } catch (Exception e) {
            Log.e(TAG, "Exceptionin [getData] :: " + e);
            e.printStackTrace();
        }
    }

    class Userdesh extends MessageManager {

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "Userdesh.onPostExecute()" + result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; jsonArray.length() > i; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    allPostPOJO = new AllPostPOJO();
                    allPostPOJO.setId(jsonObject.get("id").toString());
                    allPostPOJO.setUserId(jsonObject.get("userId").toString());
                    allPostPOJO.setCategoryId(jsonObject.get("categoryId").toString());
                    allPostPOJO.setPost(jsonObject.get("post").toString());
                    allPostPOJO.setLikes(jsonObject.get("likes").toString());
                    allPostPOJO.setDislike(jsonObject.get("dislike").toString());
                    allPostPOJO.setGop(jsonObject.get("gop").toString());
                    allPostPOJO.setFirstName(jsonObject.get("firstName").toString());
                    allPostPOJO.setCategory(jsonObject.get("category").toString());
                    allPostPOJOArrayList.add(allPostPOJO);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception in [Userdesh] :: [onPostExecute] :: " + e);
                e.printStackTrace();
            }
            Log.i(TAG, "In [onPostExecute] :: ");
            initViews();

            startRepeatingTask();
        }
    }

    private void initViews() {
        try {
            Log.i(TAG, "In [initViews] :: allPostPOJOArrayList : " + allPostPOJOArrayList.size());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(linearLayoutManager);
            dAdapter = new DishAdapter(getContext(), allPostPOJOArrayList, useriD, requestQueue);
            list.setItemAnimator(new DefaultItemAnimator());
            list.setAdapter(dAdapter);


        } catch (Exception e) {
            Log.e(TAG, "Exception in [initViews] :: " + e);
            e.printStackTrace();
        }
    }

    void startRepeatingTask() {
        mHandlerTask.run();
    }


    void stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            doSomething();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    public void doSomething() {
        try {
            Log.i(TAG, "In [doSomething] :: ");
            //AllPostPOJO obj = allPostPOJOArrayList.removeFirst();
            //allPostPOJOArrayList.addLast(obj);
            initViews();

        } catch (Exception e) {
            Log.e(TAG, "Exception in [doSomething] :: " + e);
            e.printStackTrace();
        }
    }

    public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

        private String TAG = DishAdapter.class.getCanonicalName();
        private List<AllPostPOJO> dishList;
        private Context context;
        String useriD;
        static final String REQ_TAG = "VACTIVITY";
        RequestQueue requestQueue;


        public DishAdapter(Context context, List<AllPostPOJO> dishList, String useriD, RequestQueue requestQueue) {
            try {
                Log.i(TAG, "In [DishAdapter] [Constructor] :: dishList : " + dishList.size());
                Log.i(TAG, "In [DishAdapter] [Constructor] :: context : " + context);
                Log.i(TAG, "In [DishAdapter] [Constructor] :: useriD : " + useriD);
                Log.i(TAG, "In [DishAdapter] [Constructor] :: 11111 ::::: requestQueue : " + requestQueue);

                this.context = context;
                this.dishList = dishList;
                this.useriD = useriD;
                this.requestQueue = requestQueue;

                RequestQueue RQ = RequestQueueSingleton.getInstance(context)
                        .getRequestQueue();

                Log.i(TAG, "In [DishAdapter] [Constructor] :: 22222 :: RQ : " + RQ);

            } catch (Exception e) {
                Log.e(TAG, "Exception in [DishAdapter] [Constructor] :: " + e);
                e.printStackTrace();
            }
        }

        private DishAdapter.DishViewHolder holder;

        @Override
        public DishAdapter.DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = null;
            try {

                Log.i(TAG, "In [onCreateViewHolder] :: ");
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_adapter, parent, false);
            } catch (Exception e) {
                Log.e(TAG, "Exception in [onCreateViewHolder] :: " + e);
                e.printStackTrace();
            }

            return new DishAdapter.DishViewHolder(itemView);
        }

        public void onBindViewHolder(final DishAdapter.DishViewHolder holder, int position) {
            try {
                Log.i(TAG, "In [onBindViewHolder] :: ");

                final AllPostPOJO dishPOJO = dishList.get(position);
                holder.txtpost.setText("\"" + dishPOJO.getPost() + "\"" + "  -" + dishPOJO.getFirstName());
                holder.txtlike.setText(dishPOJO.getLikes());
                holder.txtdislike.setText(dishPOJO.getDislike());
                holder.testusermode.setText("(" + dishPOJO.getCategory() + ")");

                if (dishPOJO.getCategory().toString().equals("Romantic")) {
                    holder.linearLayout.setBackgroundResource(R.drawable.design);
                    //holder.imglike.setBackgroundResource(R.drawable.ic_thum_up_white);
                    //holder.imgdislike.setBackgroundResource(R.drawable.ic_thum_down_white);
                    holder.txtpost.setTextColor(context.getResources().getColor(R.color.white));
                    //holder.txtlike.setTextColor(context.getResources().getColor(R.color.white));
                    //holder.txtdislike.setTextColor(context.getResources().getColor(R.color.white));
                    holder.testusermode.setTextColor(context.getResources().getColor(R.color.white));
                    //holder.testusermode.setVisibility(View.INVISIBLE);
                    //holder.cowtitthef.setTextColor(context.getResources().getColor(R.color.white));
                    //holder.rew_cowtit.setTextColor(context.getResources().getColor(R.color.white));
                }


                holder.imgdot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String[] options = new String[]{
                                "Follow",
                                "Re-quote",
                                "Report Plagiarism"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);


                        builder.setItems(options, // Items array
                                new DialogInterface.OnClickListener() {// Item click listener

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Get the alert dialog selected item's text
                                        String selectedItem = Arrays.asList(options).get(i);
                                        // Display the selected item's text on toast
                                        Toast.makeText(context, "Checked : " + selectedItem, Toast.LENGTH_LONG).show();
                                        Log.i(TAG, "IN [OneFragment] :: i : " + i);

                                        switch (i) {
                                            case 0:
                                                follow(dishPOJO.getUserId().toString());
                                                break;

                                            case 1:
                                                //reCowtit(dishPOJO.getUserId().toString());
                                                break;

                                            case 2:
                                                break;
                                        }


                                    }
                                });

                        // Create the alert dialog
                        AlertDialog dialog = builder.create();

                        // Get the alert dialog ListView instance
                        ListView listView = dialog.getListView();

                        // Set the divider color of alert dialog list view
                        listView.setDivider(new ColorDrawable(Color.LTGRAY));

                        // Set the divider height of alert dialog list view
                        listView.setDividerHeight(1);

                        // Finally, display the alert dialog
                        dialog.show();
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


            } catch (Exception e) {
                Log.e(TAG, "Exception in [onBindViewHolder] :: " + e);
                e.printStackTrace();
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
                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_LONG).show();
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
            String dislike = "http://uservission.com/cw/increaseDisLike.php";
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
                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_LONG).show();
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
            Log.i(TAG, "In [follow] : postid : " + postid);
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
                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_LONG).show();
                                } else if (status.equalsIgnoreCase("error")) {
                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_LONG).show();
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

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return dishList.size();
        }


        public class DishViewHolder extends RecyclerView.ViewHolder {

            private String TAG = DishAdapter.DishViewHolder.class.getCanonicalName();

            public TextView txtpost, txtlike, txtdislike, testusermode, cowtitthef, rew_cowtit;
            public ImageView imgdot, imglike, imgdislike;
            public LinearLayout linearLayout;
            public LinearLayout llLikeDisComm;

            public DishViewHolder(View view) {

                super(view);
                try {

                    Log.i(TAG, "In [DishViewHolder] [Consttructor]:: ");

                    txtpost = (TextView) view.findViewById(R.id.txt_post);
                    cowtitthef = (TextView) view.findViewById(R.id.cowtit_thef);
                    rew_cowtit = (TextView) view.findViewById(R.id.rewcowtit);
                    txtlike = (TextView) view.findViewById(R.id.txt_like);
                    txtdislike = (TextView) view.findViewById(R.id.txt_dislike);
                    testusermode = (TextView) view.findViewById(R.id.txt_usermode);
                    imgdot = (ImageView) view.findViewById(R.id.img_dot);
                    imglike = (ImageView) view.findViewById(R.id.img_lick);
                    imgdislike = (ImageView) view.findViewById(R.id.img_dislike);
                    linearLayout = (LinearLayout) view.findViewById(R.id.singelist);
                    llLikeDisComm = (LinearLayout) view.findViewById(R.id.ll_like_dis_comm);


                } catch (Exception e) {
                    Log.e(TAG, "Exception in [DishViewHolder] :: " + e);
                    e.printStackTrace();
                }
            }
        }
    }


}
