package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity implements APICallback {

    private User currentUser;
    private ArrayList<Group> groups;

    private LinearLayout progressBarLayout;
    private ListView groupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Toolbar grouptoolbar = (Toolbar) findViewById(R.id.groups_toolbar);
        grouptoolbar.setTitle(R.string.groups_actionbar_title);
        setSupportActionBar(grouptoolbar);

        groupListView = (ListView) findViewById(R.id.groups_grouplist);
        groupListView.setVisibility(View.GONE);

        progressBarLayout = (LinearLayout) findViewById(R.id.groups_progressbar_layout);
        progressBarLayout.setVisibility(View.VISIBLE);

        if (getIntent() != null && getIntent().getExtras() != null) {
            currentUser = new User();
            currentUser.userId = getIntent().getExtras().getString("UserId");
            currentUser.userName = getIntent().getExtras().getString("UserName");
            currentUser.userPassword = getIntent().getExtras().getString("UserPassword");

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
                Intent newgroup_intent = new Intent(this, NewGroupActivity.class);
                startActivity(newgroup_intent);

                return true;
            case R.id.groups_action_invites:

                // TODO: Open activity to accept invites
                Intent invites_intent = new Intent(this, GroupInvitationsActivity.class);
                startActivity(invites_intent);

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
            if (resultString != null && !resultString.equals("")) {
                try {
                    JSONObject resultObject = new JSONObject(resultString);

                    if (resultObject.has("code")) {
                        int errorCode = resultObject.getInt("code");


                    } else if (resultObject.has("groupid")) {
                        groups = new ArrayList<Group>();

                        JSONArray array = new JSONArray(resultString);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            Group newGroup = new Group();
                            newGroup.groupId = childObject.getString("groupid");
                            newGroup.groupName = childObject.getString("groupname");
                            newGroup.groupDescription = childObject.getString("groupdescription");
                            newGroup.groupAdmin = childObject.getString("groupadmin");
                            groups.add(newGroup);
                        }

                        final ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, groups);
                        groupListView.setAdapter(adapter);
                        groupListView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {

                }

                progressBarLayout.setVisibility(View.GONE);
            }
        }
    }
}