package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.os.AsyncTask;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIGetRequest extends AsyncTask<String, Void, String> {

    private static final String REQUEST_METHOD = "GET";
    private int readTimeout = 1500;
    private int connectionTimeout = 1500;

    private int callbackType;
    private APICallback callback;

    public APIGetRequest(APICallback callback, int callbackType, int timeOut) {
        this.callback = callback;
        this.callbackType = callbackType;
        this.readTimeout = timeOut;
        this.connectionTimeout = timeOut;
    }

    @Override
    protected String doInBackground(String... strings){
        String urlS = strings[0];
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