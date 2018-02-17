package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APILoginGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Activities.GroupsActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.LoginActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.RegisterActivity;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.PasswordHelper;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity implements APICallback {

    private Button registerButton;
    private TextView loginButton;
    private ConstraintLayout generalLayout;
    private LinearLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_welcome);

        registerButton = (Button) findViewById(R.id.welcome_registerButton);
        loginButton = (TextView) findViewById(R.id.welcome_loginButton);
        progressBarLayout = (LinearLayout) findViewById(R.id.welcome_progressbar_layout);
        generalLayout = (ConstraintLayout) findViewById(R.id.welcome_generallayout);

        checkForLoginData();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
            }
        });
    }

    private void checkForLoginData() {
        SharedPreferences pref = getSharedPreferences("PROJECTCARLTON_PREF", MODE_PRIVATE);
        String username = pref.getString("UserName", null);
        String password = pref.getString("UserPassword", null);

        if (username != null && password != null){
            login(username, password);
        } else {
            progressBarLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        }
    }

    private void login(String username, String password) {
        String hashedPassword = password;
        APILoginGetRequest request = new APILoginGetRequest(this, CallbackType.LOGIN_CALLBACK, 1000, getApplicationContext());
        request.execute(getString(R.string.API_URL), username, hashedPassword);
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.LOGIN_CALLBACK) {
            if (resultString != null && !resultString.equals("")) {
                try {
                    JSONObject resultObject = new JSONObject(resultString);

                    if (resultObject.has("code")) {

                    } else if (resultObject.has("username")) {
                        User user = new User();
                        user.userId = resultObject.getString("id");
                        user.userName = resultObject.getString("username");
                        user.userPassword = resultObject.getString("hash");

                        Intent intent = new Intent(this, GroupsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("UserId", user.userId);
                        bundle.putString("UserName", user.userName);
                        bundle.putString("UserPassword", user.userPassword);
                        intent.putExtras(bundle);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                    }
                } catch (JSONException e){
                }
            } else {
                progressBarLayout.setVisibility(View.INVISIBLE);
                generalLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
