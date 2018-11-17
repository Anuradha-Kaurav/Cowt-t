package com.quote.cwotit.Message;


import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Palo12 on 25-10-2017.
 */

public class OldMessage extends AsyncTask<Object, Object, String> {

    private Context context;
//    private ProgressDialog progressdialog;

    public OldMessage() {
    }

    public OldMessage(Context context) {
        this();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Message.onPreExecute()");
//        if (context != null) {
//            progressdialog = ProgressDialog.show(context, null, null, true);
//            progressdialog.setContentView(R.layout.elemento_progress_splash);
//            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            progressdialog.show();
//        }
    }

    @Override
    protected String doInBackground(Object... params) {
        System.out.println("Message.doInBackground()");
        MessageType requesttype = ObjectCaster.cast(MessageType.class, params[0]);
        String urlPath = ObjectCaster.cast(String.class, params[1]);
        String response = null;
        if (requesttype == MessageType.POST) {
            Object request = null;
            if (params.length > 2) {
                request = params[2];
            }
            response = httpPostRequest(urlPath, request);
            return response;
        } else {
            response = httpGetRequest(urlPath);
            return response;
        }
    }

    public String httpGetRequest(String urlPath) {
        System.out.println("Message.httpGetRequest()" + urlPath);
        // Create a new HttpClient and Post Header
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(urlPath)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            String entity = response.body().string();
            System.out.println("entity"+entity);
            return entity;
        } catch (IOException e) {
            return null;
        }
    }

    public String httpPostRequest(String urlPath, Object request) {
        System.out.println("Message.httpPostRequest()");
        // Create a new HttpClient and Post Header
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString;
        try {
        jsonInString = mapper.writeValueAsString(request);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonInString);
//        System.out.println("request body");
        Request requestPost = new Request.Builder()
                    .url(urlPath)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(requestPost).execute();
            System.out.println("response" + response);
            String entity = response.body().string();
            return entity;
        } catch (IOException e) {
            System.out.print("err"+e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("SignUpMessage.onPostExecute()" + result);
        if (context != null) {
//            progressdialog.dismiss();
            try {
//                MessagePOJO messagePOJO = ObjectCaster.jSONcast(MessagePOJO.class, result);
//                String message = messagePOJO.getMessage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}