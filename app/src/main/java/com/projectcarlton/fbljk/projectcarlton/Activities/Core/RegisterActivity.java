package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

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
import android.widget.Toast;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIExceptionType;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.Helpers.PasswordHelper;
import com.projectcarlton.fbljk.projectcarlton.R;

public class RegisterActivity extends AppCompatActivity implements APIUtilCallback {

    private EditText emailTextbox;
    private EditText usernameTextbox;
    private EditText passwordTextbox;
    private Button registerButton;
    private ConstraintLayout generalLayout;
    private LinearLayout progressBarLayout;

    private APIUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_register);

        apiUtil = new APIUtil(getApplicationContext(), this);

        emailTextbox = (EditText) findViewById(R.id.register_emailTxt);
        usernameTextbox = (EditText) findViewById(R.id.register_usernameTxt);
        passwordTextbox = (EditText) findViewById(R.id.register_passwordTxt);
        registerButton = (Button) findViewById(R.id.register_registerButton);
        progressBarLayout = (LinearLayout) findViewById(R.id.register_progressbar_layout);
        generalLayout = (ConstraintLayout) findViewById(R.id.register_generallayout);

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

    private void register(String email, String username, String password) {
        progressBarLayout.setVisibility(View.VISIBLE);
        generalLayout.setVisibility(View.INVISIBLE);

        String hashedPassword = PasswordHelper.createMD5(password);
        apiUtil.getRegisterAsync(email, username, hashedPassword);
    }

    private void clearInput() {
        emailTextbox.setText("");
        usernameTextbox.setText("");
        passwordTextbox.setText("");
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.REGISTER_CALLBACK) {
            if (result != null && result instanceof Boolean) {
                if ((boolean)result) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_success, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                    clearInput();
                }
            } else if (result != null && result instanceof APIException) {
                APIException exception = (APIException)result;
                int errorCode = exception.getType();

                if (errorCode == APIExceptionType.EXISTING_EMAIL) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_existing_email, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else if (errorCode == APIExceptionType.EXISTING_USERNAME) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_existing_username, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else if (errorCode == APIExceptionType.INVALID_EMAIL) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_invalid_email, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.API_ERROR, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }

        progressBarLayout.setVisibility(View.INVISIBLE);
        generalLayout.setVisibility(View.VISIBLE);
    }
}
