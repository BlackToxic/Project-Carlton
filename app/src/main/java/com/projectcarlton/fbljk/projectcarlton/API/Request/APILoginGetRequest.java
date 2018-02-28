package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;

public class APILoginGetRequest {

    private Context context;
    private APICallback callback;
    private int callbackType;

    public APILoginGetRequest(Context context, APICallback callback, int callbackType) {
        this.context = context;
        this.callback = callback;
        this.callbackType = callbackType;
    }

    public void execute(String apiUrl, String username, String password) {
        InstanceID instanceID = InstanceID.getInstance(context);
        String token = null;
        try {
            token = instanceID.getToken("1079168088792", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (Exception e) {
        }

        String urlS = "";
        if (token != null)
            urlS = apiUrl + "user?username=" + username + "&password=" + password + "&deviceid=" + token;
        else
            urlS = apiUrl + "user?username=" + username + "&password=" + password;

        StringRequest request = new StringRequest(Request.Method.GET, urlS,
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