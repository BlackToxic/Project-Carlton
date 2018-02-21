package com.projectcarlton.fbljk.projectcarlton.Helpers;

import android.content.Context;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APILoginGetRequest;
import com.projectcarlton.fbljk.projectcarlton.R;

public class APIUtil {

    private String BASE_URL;

    private Context context;
    private APICallback handler;

    public APIUtil(Context context, APICallback handler) {
        this.context = context;
        this.handler = handler;

        BASE_URL = context.getString(R.string.API_URL);
    }

    public APICallback getHandler() { return handler; }

    public void setHandler(APICallback context){ this.handler = handler; }

    public void getLoginAsync(String username, String password) {
        APILoginGetRequest request = new APILoginGetRequest(handler, CallbackType.LOGIN_CALLBACK, 1000, context);
        request.execute(BASE_URL, username, password);
    }
}