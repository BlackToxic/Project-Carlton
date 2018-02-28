package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

public class WelcomeActivity extends AppCompatActivity implements APIUtilCallback {

    private Button registerButton;
    private TextView loginButton;
    private ConstraintLayout generalLayout;
    private LinearLayout progressBarLayout;

    private APIUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_welcome);

        apiUtil = new APIUtil(getApplicationContext(), this);

        registerButton = (Button) findViewById(R.id.welcome_registerButton);
        loginButton = (TextView) findViewById(R.id.welcome_loginButton);
        progressBarLayout = (LinearLayout) findViewById(R.id.welcome_progressbar_layout);
        generalLayout = (ConstraintLayout) findViewById(R.id.welcome_generallayout);

        checkForLoginData();

        //progressBarLayout.setVisibility(View.INVISIBLE);
        //generalLayout.setVisibility(View.VISIBLE);

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
        SettingsCache.setPreferences(pref);
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
        apiUtil.getLoginAsync(username, password);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.LOGIN_CALLBACK) {
            if (result != null && result instanceof User) {
                User user = (User) result;

                Intent intent = new Intent(this, GroupsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("UserId", user.userId);
                bundle.putString("UserName", user.userName);
                bundle.putString("UserPassword", user.userPassword);
                bundle.putString("UserEmail", user.userEmail);
                bundle.putString("UserPhoto", user.userPhoto);
                intent.putExtras(bundle);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
            } else {
                progressBarLayout.setVisibility(View.INVISIBLE);
                generalLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
