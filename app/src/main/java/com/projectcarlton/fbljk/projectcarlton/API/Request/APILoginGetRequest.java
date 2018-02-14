package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.Activities.LoginActivity;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APILoginGetRequest extends AsyncTask<String, Void, String> {

    private static final String REQUEST_METHOD = "GET";
    private int readTimeout = 1500;
    private int connectionTimeout = 1500;

    private int callbackType;
    private APICallback callback;
    private Context context;

    public APILoginGetRequest(APICallback callback, int callbackType, int timeOut, Context context) {
        this.callback = callback;
        this.callbackType = callbackType;
        this.readTimeout = timeOut;
        this.connectionTimeout = timeOut;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings){
        String apiUrl = strings[0];
        String username = strings[1];
        String hashedPassword = strings[2];

        InstanceID instanceID = InstanceID.getInstance(context);
        String token = null;
        try {
            token = instanceID.getToken("1079168088792", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (Exception e) {

        }

        String urlS = "";
        if (token != null)
            urlS = apiUrl + "user?username=" + username + "&password=" + hashedPassword + "&deviceid=" + token;
        else
            urlS = apiUrl + "user?username=" + username + "&password=" + hashedPassword;

        String result;
        String inputLine = "";

        try {
            URL url = new URL(urlS);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(readTimeout);
            connection.setConnectTimeout(connectionTimeout);

            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            result = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (callback != null)
            callback.callback(callbackType, result);
    }
}
