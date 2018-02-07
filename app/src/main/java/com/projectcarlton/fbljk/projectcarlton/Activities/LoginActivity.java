package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APILoginGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.PasswordHelper;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements APICallback {

    private EditText userNameTextbox;
    private EditText passwordTextbox;
    private Button loginButton;
    private TextView registerButton;
    private LinearLayout progressBarLayout;
    private RelativeLayout generalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBarLayout = (LinearLayout) findViewById(R.id.login_progressbar_layout);
        generalLayout = (RelativeLayout) findViewById(R.id.login_general_layout);

        checkForLoginData();

        userNameTextbox = (EditText) findViewById(R.id.login_username);
        passwordTextbox = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_loginbutton);
        registerButton = (TextView) findViewById(R.id.login_registerbutton);

        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = userNameTextbox.getText().toString();
                    String password = passwordTextbox.getText().toString();

                    if (!username.equals("") && !password.equals("")) {
                        login(username, password, false);
                    }
                }
            });
        }

        if (registerButton != null) {
            registerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void checkForLoginData() {
        SharedPreferences pref = getSharedPreferences("PROJECTCARLTON_PREF", MODE_PRIVATE);
        String username = pref.getString("UserName", null);
        String password = pref.getString("UserPassword", null);

        if (username != null && password != null){
            login(username, password, true);
        }
    }

    private void login(String username, String password, boolean passwordAlreadyHashed) {
        generalLayout.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.VISIBLE);

        String hashedPassword = null;
        if (!passwordAlreadyHashed)
            hashedPassword = PasswordHelper.createMD5(password);
        else
            hashedPassword = password;

        APILoginGetRequest request = new APILoginGetRequest(this, CallbackType.LOGIN_CALLBACK, 1000, getApplicationContext());
        request.execute(getString(R.string.API_URL), username, hashedPassword);
    }

    private void saveUserCredentials(User user) {
        if (user != null)
            getSharedPreferences("PROJECTCARLTON_PREF", MODE_PRIVATE).edit().putString("UserId", user.userId).putString("UserName", user.userName).putString("UserPassword", user.userPassword).apply();
        else
            getSharedPreferences("PROJECTCARLTON_PREF", MODE_PRIVATE).edit().clear().apply();
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.LOGIN_CALLBACK) {
            if (resultString != null && !resultString.equals("")) {
                try {
                    JSONObject resultObject = new JSONObject(resultString);

                    if (resultObject.has("code")) {
                        int errorCode = resultObject.getInt("code");

                        if (errorCode == APIException.UNKNOWN_USER || errorCode == APIException.WRONG_PASSWORD) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.loginresponse_unknown_credentials, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else if (errorCode == APIException.NO_ACTIVATED_ACCOUNT) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.loginresponse_noactivated_account, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                    } else if (resultObject.has("username")) {
                        User user = new User();
                        user.userId = resultObject.getString("id");
                        user.userName = resultObject.getString("username");
                        user.userPassword = resultObject.getString("hash");

                        saveUserCredentials(user);

                        Intent intent = new Intent(this, GroupsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("UserId", user.userId);
                        bundle.putString("UserName", user.userName);
                        bundle.putString("UserPassword", user.userPassword);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.API_ERROR, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }

            progressBarLayout.setVisibility(View.GONE);
            generalLayout.setVisibility(View.VISIBLE);
        }
    }
}
