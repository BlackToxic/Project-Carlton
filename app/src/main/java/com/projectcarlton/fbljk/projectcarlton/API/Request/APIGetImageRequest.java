package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;

import java.io.InputStream;
import java.net.URL;

public class APIGetImageRequest {

    private Context context;
    private APICallback callback;
    private int callbackType;

    public APIGetImageRequest(Context context, APICallback callback, int callbackType) {
        this.context = context;
        this.callback = callback;
        this.callbackType = callbackType;
    }

    public void execute(String url) {
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        if (callback != null)
                            callback.callback(callbackType, response);
                    }
                },
                0,
                0,
                ImageView.ScaleType.FIT_XY,
                Bitmap.Config.RGB_565,
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