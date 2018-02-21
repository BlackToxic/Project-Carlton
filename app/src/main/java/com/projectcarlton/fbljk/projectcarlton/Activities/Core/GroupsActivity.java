package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbacks;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Adapter.GroupAdapter;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity implements APIUtilCallback, ActivityCallback {

    private ArrayList<Group> groups;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView groupListView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private APIUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        apiUtil = new APIUtil(getApplicationContext(), this);

        ActivityCallbacks.registerActivityCallback(this, ActivityCallbackType.GROUPRELOAD_CALLBACK);
        ActivityCallbacks.registerActivityCallback(this, ActivityCallbackType.GROUPOPEN_CALLBACK);

        Toolbar grouptoolbar = (Toolbar) findViewById(R.id.groups_toolbar);
        grouptoolbar.setTitle(R.string.groups_actionbar_title);
        setSupportActionBar(grouptoolbar);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.groups_refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadGroups();
            }
        });

        groupListView = (RecyclerView) findViewById(R.id.groups_grouplist);
        groupListView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        groupListView.setLayoutManager(layoutManager);

        if (getIntent() != null && getIntent().getExtras() != null) {
            SettingsCache.CURRENTUSER = new User();
            SettingsCache.CURRENTUSER.userId = getIntent().getExtras().getString("UserId");
            SettingsCache.CURRENTUSER.userName = getIntent().getExtras().getString("UserName");
            SettingsCache.CURRENTUSER.userPassword = getIntent().getExtras().getString("UserPassword");

            loadGroups();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityCallbacks.deregisterActivityCallback(this, ActivityCallbackType.GROUPRELOAD_CALLBACK);
        ActivityCallbacks.deregisterActivityCallback(this, ActivityCallbackType.GROUPOPEN_CALLBACK);
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

                Intent newgroup_intent = new Intent(this, NewGroupActivity.class);
                startActivity(newgroup_intent);

                return true;
            case R.id.groups_action_invites:

                Intent invites_intent = new Intent(this, GroupInvitationsActivity.class);
                startActivity(invites_intent);

                return true;

            case R.id.groups_action_profile:

                Intent profile_intent = new Intent(this, ProfileActivity.class);
                startActivity(profile_intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // Leave this empty, as we won't allow pressing the back button while being logged in
    }

    public void loadGroups() {
        refreshLayout.setRefreshing(true);

        apiUtil.loadGroupsAsync(SettingsCache.CURRENTUSER.userId);
    }

    public void openGroup(Group group) {
        Intent group_intent = new Intent(GroupsActivity.this, GroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("GroupId", group.groupId);
        bundle.putString("GroupName", group.groupName);
        bundle.putString("GroupDescription", group.groupDescription);
        bundle.putString("GroupAdmin", group.groupAdmin);
        group_intent.putExtras(bundle);
        startActivity(group_intent);
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.LOADINGGROUPS_CALLBACK) {
            if (result != null && result instanceof ArrayList) {
                groups = (ArrayList<Group>) result;
                adapter = new GroupAdapter(groups);
                groupListView.setAdapter(adapter);
            }
        }

        refreshLayout.setRefreshing(false);
    }

    @Override
    public void callbackActivity(int activityCallbackType, Object... options) {
        if (activityCallbackType == ActivityCallbackType.GROUPRELOAD_CALLBACK) {
            loadGroups();
        } else if (activityCallbackType == ActivityCallbackType.GROUPOPEN_CALLBACK) {
            if (options != null && options[0] != null && options[0] instanceof Group) {
                openGroup((Group)options[0]);
            }
        }
    }
}