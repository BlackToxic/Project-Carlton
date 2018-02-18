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

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Adapter.InviteUsersAdapter;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class InviteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, APICallback {

    private ArrayList<User> users;
    private InviteUsersAdapter adapter;

    private LinearLayout progressBarLayout;
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

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

        String apiUrl = getString(R.string.API_URL) + "user?username=" + userName + "&groupid=" + GroupActivity.currentGroup.groupId;
        APIGetRequest request = new APIGetRequest(this, CallbackType.LOADUSERS_CALLBACK, 5000);
        request.execute(apiUrl);
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.LOADUSERS_CALLBACK) {
            users = new ArrayList<User>();
            if (adapter != null)
                adapter.clear();

            if (resultString != null && !resultString.equals("") && !resultString.equals("null")) {
                try {
                    if (resultString.contains("id")) {
                        JSONArray array = new JSONArray(resultString);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            User user = new User();
                            user.userId = childObject.getString("id");
                            user.userName = childObject.getString("username");
                            users.add(user);
                        }

                        adapter = new InviteUsersAdapter(users, getApplicationContext());
                        userListView.setAdapter(adapter);
                        userListView.setVisibility(View.VISIBLE);
                    } else {
                        JSONObject resultObject = new JSONObject(resultString);

                        if (resultObject.has("code")) {
                            int errorCode = resultObject.getInt("code");
                        }
                    }
                } catch (Exception ex) {

                }
            }
            progressBarLayout.setVisibility(View.GONE);
        }
    }
}
