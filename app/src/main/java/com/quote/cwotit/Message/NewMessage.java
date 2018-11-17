package com.quote.cwotit.Message;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


/**
 * Created by Palo12 on 25-10-2017.
 */

public class NewMessage extends JsonObjectRequest {

    public NewMessage(int method, String url, JSONObject jsonRequest,
                      Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method,url,jsonRequest,listener,errorListener);

    }

}
