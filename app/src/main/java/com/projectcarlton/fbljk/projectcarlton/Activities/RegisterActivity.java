package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.PasswordHelper;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements APICallback {

    private EditText emailTextbox;
    private EditText usernameTextbox;
    private EditText passwordTextbox;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailTextbox = (EditText) findViewById(R.id.register_email);
        usernameTextbox = (EditText) findViewById(R.id.register_username);
        passwordTextbox = (EditText) findViewById(R.id.register_password);
        registerButton = (Button) findViewById(R.id.register_registerbutton);

        if (registerButton != null) {
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = emailTextbox.getText().toString();
                    String username = usernameTextbox.getText().toString();
                    String password = passwordTextbox.getText().toString();

                    if (!email.equals("") && !username.equals("") && !password.equals("")) {
                        register(email, username, password);
                    }
                }
            });
        }
    }

    private void register(String email, String username, String password) {
        String hashedPassword = PasswordHelper.createMD5(password);

        String apiUrl = getString(R.string.API_URL) + "user?register=1&email=" + email + "&username=" + username + "&password=" + hashedPassword;
        APIGetRequest request = new APIGetRequest(this, CallbackType.REGISTER_CALLBACK, 5000);
        request.execute(apiUrl);
    }

    private void clearInput() {
        emailTextbox.setText("");
        usernameTextbox.setText("");
        passwordTextbox.setText("");
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.REGISTER_CALLBACK) {
            if (resultString != null && !resultString.equals("")) {
                try {
                    JSONObject resultObject = new JSONObject(resultString);

                    if (resultObject.has("code")) {
                        int errorCode = resultObject.getInt("code");

                        if (errorCode == APIException.EXISTING_EMAIL) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_existing_email, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else if (errorCode == APIException.EXISTING_USERNAME) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_existing_username, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else if (errorCode == APIException.INVALID_EMAIL) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_invalid_email, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                    } else if (resultObject.has("id")) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_success, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();

                        clearInput();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
