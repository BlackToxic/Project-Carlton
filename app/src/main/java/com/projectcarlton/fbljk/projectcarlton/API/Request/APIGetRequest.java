package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;

public class APIGetRequest {

    private Context context;
    private APICallback callback;
    private int callbackType;

    public APIGetRequest(Context context, APICallback callback, int callbackType) {
        this.context = context;
        this.callback = callback;
        this.callbackType = callbackType;
    }

    public void execute(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (callback != null)
                            callback.callback(callbackType, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null)
                            callback.callback(callbackType, null);
                    }
                }) {
        };

        SettingsCache.getRequestQueue(context).add(request);
    }
}