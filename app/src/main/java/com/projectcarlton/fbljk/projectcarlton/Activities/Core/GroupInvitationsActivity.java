package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Adapter.GroupInvitesAdapter;
import com.projectcarlton.fbljk.projectcarlton.Data.Invite;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class GroupInvitationsActivity extends AppCompatActivity implements APIUtilCallback {

    private ArrayList<Invite> invites;
    private GroupInvitesAdapter adapter;
    private APIUtil apiUtil;

    private LinearLayout progressBarLayout;
    private ListView invitesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invitations);

        apiUtil = new APIUtil(getApplicationContext(), this);

        Toolbar invitetoolbar = (Toolbar) findViewById(R.id.groupinvites_toolbar);
        invitetoolbar.setTitle(R.string.groupinvitations_title);
        setSupportActionBar(invitetoolbar);

        invitesListView = (ListView) findViewById(R.id.groupinvites_inviteslist);
        invitesListView.setVisibility(View.GONE);

        progressBarLayout = (LinearLayout) findViewById(R.id.groupinvites_progressbar_layout);
        progressBarLayout.setVisibility(View.VISIBLE);

        if (GroupsActivity.currentUser != null) {
            apiUtil.loadInvitesAsync(GroupsActivity.currentUser.userId);
        }
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.LOADINGINVITES_CALLBACK) {
            if (result != null && result instanceof ArrayList) {
                invites = (ArrayList<Invite>) result;
                adapter = new GroupInvitesAdapter(invites, getApplicationContext());
                invitesListView.setAdapter(adapter);
                invitesListView.setVisibility(View.VISIBLE);
            }
        }

        progressBarLayout.setVisibility(View.GONE);
    }
}
