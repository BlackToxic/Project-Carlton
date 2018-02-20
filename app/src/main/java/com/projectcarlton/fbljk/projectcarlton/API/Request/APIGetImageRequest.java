package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;

import java.io.InputStream;
import java.net.URL;

public class APIGetImageRequest extends AsyncTask<String, Void, Bitmap> {

    private APICallback callback;

    public APIGetImageRequest(APICallback callback) {
        this.callback = callback;
    }

    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];

        Bitmap icon = null;
        try {
            InputStream in = new URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return icon;
    }

    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        if (callback != null)
            callback.callback(CallbackType.DOWNLOADIMAGE_CALLBACK, result);
    }

}