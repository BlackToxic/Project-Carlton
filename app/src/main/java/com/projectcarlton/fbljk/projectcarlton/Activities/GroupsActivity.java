package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.R;

public class GroupsActivity extends AppCompatActivity implements APICallback {

    private User currentUser;

    private LinearLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        progressBarLayout = (LinearLayout) findViewById(R.id.groups_progressbar_layout);
        progressBarLayout.setVisibility(View.VISIBLE);

        if (savedInstanceState != null) {
            currentUser = new User();
            currentUser.userId = savedInstanceState.getString("UserId");
            currentUser.userName = savedInstanceState.getString("UserName");
            currentUser.userPassword = savedInstanceState.getString("UserPassword");

            // TODO: Make this pretty
            String apiUrl = getString(R.string.API_URL) + "group?userid=";// + currentUser.userId;
            APIGetRequest request = new APIGetRequest(this, CallbackType.LOADINGGROUPS_CALLBACK, 5000);
            request.execute(apiUrl);
        }
    }

    @Override
    public void onBackPressed() {
        // Leave this empty, as we won't allow pressing the back button while being logged in
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.LOADINGGROUPS_CALLBACK) {

            // TODO: Fill a list with all groups

            if (resultString != null && !resultString.equals("")) {
                progressBarLayout.setVisibility(View.GONE);
            }
        }
    }
}