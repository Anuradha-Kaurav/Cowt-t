package com.quote.cwotit.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quote.cwotit.Common.RequestQueueSingleton;
import com.quote.cwotit.Common.SessionManager;
import com.quote.cwotit.Common.Urls;
import com.quote.cwotit.Common.Utils;
import com.quote.cwotit.POJO.CowitMoodPOJO;
import com.quote.cwotit.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class NewPostFragment extends Fragment {

    private static String TAG = NewPostFragment.class.getCanonicalName();
    Context context;
    String useriD;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    ProgressDialog progressdialog;
    RecyclerView recyclerView;
    public static String categoryId;
    public static ImageView selected_emoji;
    public static TextView selected_emoji_title;
    TextView post;
    EditText edit_post;
    private NewPostMoodAdapter newPostMoodAdapter;
    static final String REQ_TAG = "VACTIVITY";

    @SuppressLint("ValidFragment")
    public NewPostFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    public NewPostFragment() {
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
            view = inflater.inflate(R.layout.fragment_new_posts, container, false);
            selected_emoji = view.findViewById(R.id.selected_emoji);
            selected_emoji_title = view.findViewById(R.id.selected_emoji_title);
            edit_post = view.findViewById(R.id.edt_post);
            post = view.findViewById(R.id.txt_post_con);
            recyclerView = view.findViewById(R.id.category_list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            newPostMoodAdapter = new NewPostMoodAdapter(getContext(), MoodFragment.cowitMoodPOJOArrayList);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(newPostMoodAdapter);

            requestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();
            sessionManager = new SessionManager(getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            useriD = user.get(SessionManager.KEY_USERID);

            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!edit_post.getText().toString().isEmpty()){
                        if(!selected_emoji_title.getText().toString().isEmpty()){
                            JSONObject json = new JSONObject();
                            try {
                                json.put("categoryId", categoryId);
                                json.put("userId", useriD);
                                json.put("post", edit_post.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.createPost, json,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                                            selected_emoji.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_green_24dp));
                                            selected_emoji_title.setText("");
                                            edit_post.setText("");

                                            Utils.simpleSnackBar(getActivity(), "Posted successfully" );

//                                            Intent intent1 = new Intent(AddPost.this, DashBoard.class);
//                                            startActivity(intent1);
//                                            finish();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Error getting response");
                                }
                            });
                            jsonObjectRequest.setTag(REQ_TAG);
                            requestQueue.add(jsonObjectRequest);
                        }else{
                            Utils.simpleSnackBar(getActivity(), "Please select a category");
                        }
                    }else{
                        Utils.simpleSnackBar(getActivity(), "Please enter your quote");
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Exception in [onCreateView] :: " + e);
            e.printStackTrace();
        }
        return view;
    }

}

class NewPostMoodAdapter extends RecyclerView.Adapter<NewPostMoodAdapter.ViewHolder> {

    private List<CowitMoodPOJO> dishList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tet1_old;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            tet1_old = (TextView) view.findViewById(R.id.emoji_title);
            imageView = (ImageView) view.findViewById(R.id.emoji);
        }
    }

    public NewPostMoodAdapter(Context context, List<CowitMoodPOJO> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    private MoodFragment.MoodAdapter.DishViewHolder holder;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newpost_mood_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CowitMoodPOJO dishPOJO = dishList.get(position);
        holder.tet1_old.setText(dishPOJO.getCategory());
        Picasso.with(context)
                .load(dishPOJO.getImagePath().toString())
                .resize(150, 150)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setSelectedData(dishPOJO);
            }
        });
        holder.tet1_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedData(dishPOJO);
            }
        });
    }

    private void setSelectedData(CowitMoodPOJO dishPOJO){
        Picasso.with(context)
                .load(dishPOJO.getImagePath().toString())
                .resize(150, 150)
                .into(NewPostFragment.selected_emoji);

        NewPostFragment.selected_emoji_title.setText(dishPOJO.getCategory());

        NewPostFragment.categoryId = dishPOJO.getId();
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }


}
