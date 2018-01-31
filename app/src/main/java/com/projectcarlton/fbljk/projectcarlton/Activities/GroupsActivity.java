package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        Toolbar grouptoolbar = (Toolbar) findViewById(R.id.groups_toolbar);
        grouptoolbar.setTitle(R.string.groups_actionbar_title);
        setSupportActionBar(grouptoolbar);

        progressBarLayout = (LinearLayout) findViewById(R.id.groups_progressbar_layout);
        progressBarLayout.setVisibility(View.VISIBLE);

        if (savedInstanceState != null) {
            currentUser = new User();
            currentUser.userId = savedInstanceState.getString("UserId");
            currentUser.userName = savedInstanceState.getString("UserName");
            currentUser.userPassword = savedInstanceState.getString("UserPassword");

            // TODO: Make this pretty
            String apiUrl = getString(R.string.API_URL) + "group?userid=" + currentUser.userId;
            APIGetRequest request = new APIGetRequest(this, CallbackType.LOADINGGROUPS_CALLBACK, 5000);
            request.execute(apiUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.groups_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.groups_action_add:

                // TODO: Open activity to create a new group

                return true;
            case R.id.groups_action_invites:

                // TODO: Open activity to accept invites

                return true;
            default:
                return super.onOptionsItemSelected(item);
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