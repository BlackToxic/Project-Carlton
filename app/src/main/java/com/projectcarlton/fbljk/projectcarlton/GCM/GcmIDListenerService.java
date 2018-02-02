package com.projectcarlton.fbljk.projectcarlton.GCM;

import android.content.SharedPreferences;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APILoginGetRequest;
import com.projectcarlton.fbljk.projectcarlton.R;

public class GcmIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        SharedPreferences pref = getSharedPreferences("PROECTCARLTON_PREF", MODE_PRIVATE);
        String username = pref.getString("UserName", null);
        String password = pref.getString("UserPassword", null);

        if (username != null && password != null){
            APILoginGetRequest request = new APILoginGetRequest(null, CallbackType.LOGIN_CALLBACK, 1000, getApplicationContext());
            request.execute(getString(R.string.API_URL), username, password);
        }
    }

}