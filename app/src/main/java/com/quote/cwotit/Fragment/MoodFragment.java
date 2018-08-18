package com.quote.cwotit.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.quote.cwotit.Activity.AddPost;
import com.quote.cwotit.Common.RequestQueueSingleton;
import com.quote.cwotit.Common.SessionManager;
import com.quote.cwotit.Common.Urls;
import com.quote.cwotit.Message.MessageManager;
import com.quote.cwotit.Message.MessageType;
import com.quote.cwotit.POJO.AllPostPOJO;
import com.quote.cwotit.POJO.CowitMoodPOJO;
import com.quote.cwotit.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MoodFragment extends Fragment {

    private static String TAG = MoodFragment.class.getCanonicalName();
    String useriD, chatid;
    private MoodAdapter dAdapter;
    public static ArrayList<AllPostPOJO> allPostPOJOArrayList = new ArrayList<>();
    private CowitMoodPOJO cowitMoodPOJO;
    public static ArrayList<CowitMoodPOJO> cowitMoodPOJOArrayList = new ArrayList<>();
    ProgressDialog progressdialog;
    AllPostPOJO allPostPOJO;
    RequestQueue requestQueue;
    static final String REQ_TAG = "VACTIVITY";

    RecyclerView list;
    Context context;

    public MoodFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public MoodFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }


    //  private MoodAdapter dAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mood_fragment, container, false);
        // Inflate the layout for this fragment
        list = (RecyclerView) view.findViewById(R.id.list_cowtit);
        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        requestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();
        useriD = user.get(SessionManager.KEY_USERID);
        getData();
        return view;

    }

    public void getData() {
        Userdesh userdesh = new Userdesh();
        userdesh.execute(MessageType.GET, Urls.fetchCategory);
    }

    class Userdesh extends MessageManager {
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("OldDishesMessage.onPostExecute()" + result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                cowitMoodPOJOArrayList.clear();
                for (int i = 0; jsonArray.length() > i; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                    for (int j = 0; jsonArray1.length() > j; j++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                        cowitMoodPOJO = new CowitMoodPOJO();
                        cowitMoodPOJO.setId(jsonObject1.get("id").toString());
                        cowitMoodPOJO.setCategory(jsonObject1.get("category").toString());
                        cowitMoodPOJO.setActive(jsonObject1.get("active").toString());
                        cowitMoodPOJO.setImagePath(jsonObject1.get("imagePath").toString());
                        cowitMoodPOJOArrayList.add(cowitMoodPOJO);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();
            initViews();
        }
    }

    private void initViews() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        list.setLayoutManager(layoutManager);
        dAdapter = new MoodAdapter(context, cowitMoodPOJOArrayList);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(dAdapter);
    }

    class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.DishViewHolder> {

        private List<CowitMoodPOJO> dishList;
        private Context context;

        public class DishViewHolder extends RecyclerView.ViewHolder {
            public TextView tet1_old;
            public ImageView imageView;

            public DishViewHolder(View view) {
                super(view);
                tet1_old = (TextView) view.findViewById(R.id.txt_cowtit);
                imageView = (ImageView) view.findViewById(R.id.img_cowtit);

            }
        }

        public MoodAdapter(Context context, List<CowitMoodPOJO> dishList) {
            this.context = context;
            this.dishList = dishList;
        }

        private MoodAdapter.DishViewHolder holder;

        @Override
        public MoodAdapter.DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mood_adapter, parent, false);
            return new MoodAdapter.DishViewHolder(itemView);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(MoodAdapter.DishViewHolder holder, int position) {
            final CowitMoodPOJO dishPOJO = dishList.get(position);
            holder.tet1_old.setText(dishPOJO.getCategory());
            Picasso.with(getActivity())
                    .load(dishPOJO.getImagePath().toString())
                    .resize(150, 150)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatid = dishPOJO.getId().toString();
                    clickdfgd(dishPOJO.getCategory());
                }
            });
            holder.tet1_old.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatid = dishPOJO.getId().toString();
                    clickdfgd(dishPOJO.getCategory());
                }
            });


        }

        @Override
        public int getItemCount() {
            return dishList.size();
        }
    }


    public void clickdfgd(final String category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = new JSONObject();
                try {
                    json.put("categoryId", chatid);
                    json.put("userId", useriD);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, json.toString());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Urls.fetchQuotesBasedOnMood)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cache-Control", "no-cache")
                        .addHeader("Postman-Token", "9cb6731d-308f-43b3-8985-f0a4e6d78483")
                        .build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String s = response.body().string();
                    System.out.println(s);
                    allPostPOJOArrayList.clear();
                    JSONArray jsonArray = new JSONArray(s);
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
                    Intent intent = new Intent(getContext(), AddPost.class);
                    intent.putExtra("category", category);
                    intent.putExtra("allPostPOJOArrayList", allPostPOJOArrayList);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("ERRRROOR ",e.getMessage());
                }
            }
        }).start();
    }

}
