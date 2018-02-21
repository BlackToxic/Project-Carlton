package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIExceptionType;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.Helpers.PasswordHelper;
import com.projectcarlton.fbljk.projectcarlton.R;

public class LoginActivity extends AppCompatActivity implements APIUtilCallback {

    private EditText userNameTextbox;
    private EditText passwordTextbox;
    private Button loginButton;
    private TextView forgotButton;
    private ConstraintLayout generalLayout;
    private LinearLayout progressBarLayout;

    private APIUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_login);

        apiUtil = new APIUtil(getApplicationContext(), this);

        userNameTextbox = (EditText) findViewById(R.id.login_usernameTxt);
        passwordTextbox = (EditText) findViewById(R.id.login_passwordTxt);
        loginButton = (Button) findViewById(R.id.login_loginButton);
        forgotButton = (TextView) findViewById(R.id.login_forgotButton);
        progressBarLayout = (LinearLayout) findViewById(R.id.login_progressbar_layout);
        generalLayout = (ConstraintLayout) findViewById(R.id.login_generallayout);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameTextbox.getText().toString();
                String password = passwordTextbox.getText().toString();

                if (!username.equals("") && !password.equals("")) {
                    login(username, password);
                }
            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void login(String username, String password) {
        progressBarLayout.setVisibility(View.VISIBLE);
        generalLayout.setVisibility(View.INVISIBLE);

        String hashedPassword = PasswordHelper.createMD5(password);
        apiUtil.getLoginAsync(username, hashedPassword);
    }

    private void saveUserCredentials(User user) {
        if (user != null)
            getSharedPreferences("PROJECTCARLTON_PREF", MODE_PRIVATE).edit().putString("UserId", user.userId).putString("UserName", user.userName).putString("UserPassword", user.userPassword).apply();
        else
            getSharedPreferences("PROJECTCARLTON_PREF", MODE_PRIVATE).edit().clear().apply();
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.LOGIN_CALLBACK) {
            if (result != null && result instanceof User) {
                User user = (User) result;

                saveUserCredentials(user);

                Intent intent = new Intent(this, GroupsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("UserId", user.userId);
                bundle.putString("UserName", user.userName);
                bundle.putString("UserPassword", user.userPassword);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (result != null && result instanceof APIException) {
                APIException exception = (APIException)result;
                int errorCode = exception.getType();

                if (errorCode == APIExceptionType.UNKNOWN_USER || errorCode == APIExceptionType.WRONG_PASSWORD) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.loginresponse_unknown_credentials, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else if (errorCode == APIExceptionType.NO_ACTIVATED_ACCOUNT) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.loginresponse_noactivated_account, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }

                progressBarLayout.setVisibility(View.INVISIBLE);
                generalLayout.setVisibility(View.VISIBLE);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.API_ERROR, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

                progressBarLayout.setVisibility(View.INVISIBLE);
                generalLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
