package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Adapter.GroupInvitesAdapter;
import com.projectcarlton.fbljk.projectcarlton.Data.Invite;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupInvitationsActivity extends AppCompatActivity implements APICallback {

    private ArrayList<Invite> invites;
    private static GroupInvitesAdapter adapter;

    private LinearLayout progressBarLayout;
    private ListView invitesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invitations);

        Toolbar invitetoolbar = (Toolbar) findViewById(R.id.groupinvites_toolbar);
        invitetoolbar.setTitle(R.string.groupinvitations_title);
        setSupportActionBar(invitetoolbar);

        invitesListView = (ListView) findViewById(R.id.groupinvites_inviteslist);
        invitesListView.setVisibility(View.GONE);

        progressBarLayout = (LinearLayout) findViewById(R.id.groupinvites_progressbar_layout);
        progressBarLayout.setVisibility(View.VISIBLE);

        if (GroupsActivity.currentUser != null) {
            String apiUrl = getString(R.string.API_URL) + "invite?userid=" + GroupsActivity.currentUser.userId;
            APIGetRequest request = new APIGetRequest(this, CallbackType.LOADINGINVITES_CALLBACK, 5000);
            request.execute(apiUrl);
        }
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.LOADINGINVITES_CALLBACK) {
            if (resultString != null && !resultString.equals("")) {
                try {
                    if (resultString.contains("id")) {
                        invites = new ArrayList<Invite>();

                        JSONArray array = new JSONArray(resultString);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            Invite invite = new Invite();
                            invite.inviteId = childObject.getString("id");
                            invite.senderName = "von " + childObject.getString("sender");
                            invite.groupName = childObject.getString("groupname");
                            invites.add(invite);
                        }

                        adapter = new GroupInvitesAdapter(invites, getApplicationContext());
                        invitesListView.setAdapter(adapter);
                        invitesListView.setVisibility(View.VISIBLE);
                    } else {
                        JSONObject resultObject = new JSONObject(resultString);

                        if (resultObject.has("code")) {
                            int errorCode = resultObject.getInt("code");

                        }
                    }
                } catch (Exception ex) {

                }

                progressBarLayout.setVisibility(View.GONE);
            }
        }
    }
}
