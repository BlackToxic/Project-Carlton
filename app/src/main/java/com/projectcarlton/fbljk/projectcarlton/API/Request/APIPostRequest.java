package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;

import java.util.HashMap;
import java.util.Map;

public class APIPostRequest {

    private Context context;
    private APICallback callback;
    private int callbackType;
    private Map<String, String> parameters;

    public APIPostRequest(Context context, APICallback callback, int callbackType) {
        this.context = context;
        this.callback = callback;
        this.callbackType = callbackType;
        this.parameters = new HashMap<String, String>();
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public void execute(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
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

            @Override
            protected Map<String, String> getParams() {
                return parameters;
            }
        };

        SettingsCache.getRequestQueue(context).add(request);
    }

}