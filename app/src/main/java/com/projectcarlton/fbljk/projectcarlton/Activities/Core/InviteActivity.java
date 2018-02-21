package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Adapter.InviteUsersAdapter;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class InviteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, APIUtilCallback {

    private ArrayList<User> users;
    private InviteUsersAdapter adapter;
    private APIUtil apiUtil;

    private LinearLayout progressBarLayout;
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        apiUtil = new APIUtil(getApplicationContext(), this);

        Toolbar invitetoolbar = (Toolbar) findViewById(R.id.invite_toolbar);
        invitetoolbar.setTitle("");
        setSupportActionBar(invitetoolbar);

        userListView = (ListView) findViewById(R.id.invite_userlist);
        userListView.setVisibility(View.VISIBLE);

        progressBarLayout = (LinearLayout) findViewById(R.id.invite_progressbar_layout);
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.invite_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.invite_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.equals(""))
            searchUsers(query);
        else {
            users = new ArrayList<User>();
            if (adapter != null)
                adapter.clear();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equals(""))
            searchUsers(newText);
        else {
            users = new ArrayList<User>();
            if (adapter != null)
                adapter.clear();
        }

        return false;
    }

    private void searchUsers(String userName) {
        progressBarLayout.setVisibility(View.VISIBLE);

        apiUtil.searchUsersAsync(userName, GroupActivity.currentGroup.groupId);
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.SEARCHUSERS_CALLBACK) {
            users = new ArrayList<User>();
            if (adapter != null)
                adapter.clear();

            if (result != null && result instanceof ArrayList) {
                users = (ArrayList<User>)result;
                adapter = new InviteUsersAdapter(users, getApplicationContext());
                userListView.setAdapter(adapter);
                userListView.setVisibility(View.VISIBLE);
            }
        }

        progressBarLayout.setVisibility(View.GONE);
    }
}
