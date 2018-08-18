package com.quote.cwotit.Fragment.SubFragment;

/**
 * Created by guruji on 6/10/2018.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quote.cwotit.Message.MessageManager;
import com.quote.cwotit.Message.MessageType;
import com.quote.cwotit.POJO.AllPostPOJO;
import com.quote.cwotit.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class WriterFragment extends Fragment {

    @BindView(R.id.ser_writer)
    EditText serWriter;
    @BindView(R.id.btn_ser_writer)
    Button btnSerWriter;
    @BindView(R.id.list_writer)
    RecyclerView listWriter;
    Unbinder unbinder;
    private DishAdapter dAdapter;
    ArrayList<AllPostPOJO> allPostPOJOS = new ArrayList<>();
    String url = "http://uservission.com/cw/SearchInMood.php";

    public WriterFragment() {
        // Required empty public constructor
    }

    Context context;

    @SuppressLint("ValidFragment")
    public WriterFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_writer, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_ser_writer)
    public void onViewClicked() {
        String serch = serWriter.getText().toString();
        if (serch.isEmpty()) {
            serWriter.setFocusable(true);
        } else {
            AllPostPOJO allPostPOJO = new AllPostPOJO();
            allPostPOJO.setPost(serch);
            ReqSearch reqSearch = new ReqSearch();
            reqSearch.execute(MessageType.POST, url, allPostPOJO);
        }
    }

    public class ReqSearch extends MessageManager {
        @Override
        public void onPostExecute(String result) {
            if (result == null) {
                System.out.println(result);
            } else {
                System.out.println(result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                    for (int i = 0; jsonArray1.length() > i; i++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        AllPostPOJO allPostPOJO = new AllPostPOJO();
                        allPostPOJO.setUserId(jsonObject.get("userId").toString());
                        allPostPOJO.setCategoryId(jsonObject.get("categoryId").toString());
                        allPostPOJO.setPost(jsonObject.get("post").toString());
                        allPostPOJO.setLikes(jsonObject.get("likes").toString());
                        allPostPOJO.setDislike(jsonObject.get("dislike").toString());
                        allPostPOJO.setGop(jsonObject.get("gop").toString());
                        allPostPOJOS.add(allPostPOJO);
                    }
                    initViews();
                } catch (Exception e) {

                }
            }
        }
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listWriter.setLayoutManager(linearLayoutManager);
        dAdapter = new DishAdapter(getContext(), allPostPOJOS);
        listWriter.setItemAnimator(new DefaultItemAnimator());
        listWriter.setAdapter(dAdapter);
    }

    class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

        private List<AllPostPOJO> dishList;
        private Context context;

        public class DishViewHolder extends RecyclerView.ViewHolder {
            public TextView txtpost, txtlike, txtdislike, testusermode, cowtitthef, rew_cowtit;
            public ImageView imgdot, imglike, imgdislike;
            public LinearLayout linearLayout;

            public DishViewHolder(View view) {
                super(view);
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
            holder.txtpost.setText("\"" + dishPOJO.getPost() + "\"" + "  -" + dishPOJO.getFirstName());
            holder.txtlike.setText(dishPOJO.getLikes());
            holder.txtdislike.setText(dishPOJO.getDislike());

        }

        @Override
        public int getItemCount() {
            return dishList.size();
        }
    }


}