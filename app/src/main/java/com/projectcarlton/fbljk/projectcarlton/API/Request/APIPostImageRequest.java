package com.projectcarlton.fbljk.projectcarlton.API.Request;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class APIPostImageRequest extends AsyncTask<String, Void, String> {

    private int callbackType;
    private APICallback callback;
    private HashMap<String, String> params;
    private Bitmap imageToUpload;

    private String delimiter = "--";
    private String boundary = "SwA" + Long.toString(System.currentTimeMillis()) + "SwA";

    public APIPostImageRequest(APICallback callback, int callbackType, Bitmap img) {
        this.callback = callback;
        this.callbackType = callbackType;
        this.imageToUpload = img;
        this.params = new HashMap<String, String>();
    }

    public void addParameter(String paramName, String value) {
        params.put(paramName, value);
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlS = strings[0];
        String result = "";
        String inputLine = "";

        try {
            URL url = new URL(urlS);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.connect();
            OutputStream os = connection.getOutputStream();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                os.write((delimiter + boundary + "rn").getBytes());
                os.write(("Content-Type: text/plainrn").getBytes());
                os.write(("Content-Disposition: form-data; name=" + entry.getKey() + "rn").getBytes());
                os.write(("rn" + entry.getValue() + "rn").getBytes());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageToUpload.compress(Bitmap.CompressFormat.PNG, 0, baos);

            os.write((delimiter + boundary + "rn").getBytes());
            os.write(("Content-Disposition: form-data; name=file; filename=newimage.pngrn").getBytes());
            os.write(("Content-Type: application/octet-streamrn").getBytes());
            os.write(("Content-Transfer-Encoding: binaryrn").getBytes());
            os.write("rn".getBytes());

            os.write(baos.toByteArray());
            os.write("\r\n".getBytes());

            os.flush();
            os.close();

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
        }

        return result;
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);

        if (callback != null)
            callback.callback(callbackType, result);
    }
}