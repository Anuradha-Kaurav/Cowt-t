package com.quote.cwotit.Message;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.quote.cwotit.R;

import org.json.JSONObject;

public class MessageManager {

    private static RequestQueue requestQueue;
    private static Context publicContext;
    private Context localContext;
    private ProgressDialog progressdialog;
    private Response.Listener listener;
    private Response.ErrorListener errorListener;
    private static Response.Listener defaultListener;
    private static Response.ErrorListener defaultErrorListener;

    public MessageManager() {
    }

    public MessageManager(Context context) {
        this();
        this.localContext = context;
    }

    public static void setPublicContext(Context context) {
        publicContext = context;
    }

    public static synchronized RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(publicContext);
        }
        return requestQueue;
    }

    private void show() {
        progressdialog = ProgressDialog.show(localContext, null, null, true);
        progressdialog.setContentView(R.layout.elemento_progress_splash);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressdialog.show();
    }

    public void execute(Object... params) {
        if (this.localContext != null) {
            show();
            int RequestMethod = Request.Method.GET;
            MessageType requesttype = ObjectCaster.cast(MessageType.class, params[0]);
            String url = ObjectCaster.cast(String.class, params[1]);
            JSONObject jsonObject = null;
            String response = null;
            if (requesttype == MessageType.POST) {
                RequestMethod = Request.Method.POST;
                try {
                    if (params.length > 2) {
                        Object request = params[2];
                        if (request != null) {
                            Gson gson = new Gson();
                            String json = gson.toJson(request);
                            jsonObject = new JSONObject(json);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            listener = (Response.Listener) getObject(Response.Listener.class, params);
            errorListener = (Response.ErrorListener) getObject(Response.ErrorListener.class, params);
            NewMessage message = new NewMessage(RequestMethod, url, jsonObject, listener, errorListener);
            getRequestQueue().add(message);
        } else {
            OldMessage message = new OldMessage() {
                @Override
                protected void onPostExecute(String result) {
                    MessageManager.this.onPostExecute(result);
                }
            };
            message.execute(params);
        }
    }

    public void onPostExecute(String result) {
        System.out.print("result" + result);
    }

    private Object getObject(Class cls, Object... oA) {
        for (Object o : oA) {
            if (cls.isInstance(o)) {
                return o;
            }
        }
        if (cls.isAssignableFrom(Response.Listener.class)) {
            if (defaultListener == null) {
                defaultListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        onPostExecute(json);
                        progressdialog.dismiss();
                    }
                };
            }
            return defaultListener;
        }
        if (cls.isAssignableFrom(Response.ErrorListener.class)) {
            if (defaultErrorListener == null) {
                defaultErrorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                    }
                };
            }
            return defaultErrorListener;
        }
        return null;
    }

}