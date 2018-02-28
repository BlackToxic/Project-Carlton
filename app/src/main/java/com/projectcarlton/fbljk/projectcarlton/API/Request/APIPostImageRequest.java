package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Helpers.FileHelper;

import java.util.HashMap;
import java.util.Map;

public class APIPostImageRequest {

    private Context context;
    private APICallback callback;
    private int callbackType;
    private Map<String, String> parameters;

    public APIPostImageRequest(Context context, APICallback callback, int callbackType) {
        this.context = context;
        this.callback = callback;
        this.callbackType = callbackType;
        this.parameters = new HashMap<String, String>();
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public void execute(String url, final Bitmap image) {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultString = new String(response.data);

                        if (callback != null)
                            callback.callback(callbackType, resultString);
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

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<String, DataPart>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", FileHelper.getFileDataFromDrawable(image)));
                return params;
            }
        };

        SettingsCache.getRequestQueue(context).add(request);
    }

}