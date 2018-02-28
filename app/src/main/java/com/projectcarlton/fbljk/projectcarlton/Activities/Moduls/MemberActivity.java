package com.projectcarlton.fbljk.projectcarlton.Activities.Moduls;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Activities.Core.GroupActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.Core.GroupsActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.Core.InviteActivity;
import com.projectcarlton.fbljk.projectcarlton.Adapter.MemberAdapter;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemberActivity extends AppCompatActivity implements APIUtilCallback {

    private Button inviteButton;
    private ListView memberList;
    private View spacer;
    private LinearLayout progressBarLayout;

    private ArrayList<User> users;
    private MemberAdapter adapter;
    private APIUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        apiUtil = new APIUtil(getApplicationContext(), this);

        Toolbar membertoolbar = (Toolbar) findViewById(R.id.member_toolbar);
        membertoolbar.setTitle(R.string.group_memberbutton_text);
        setSupportActionBar(membertoolbar);

        progressBarLayout = (LinearLayout) findViewById(R.id.member_progressbar_layout);

        inviteButton = (Button) findViewById(R.id.member_invite_button);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberActivity.this, InviteActivity.class);
                startActivity(intent);
            }
        });

        memberList = (ListView) findViewById(R.id.member_memberlist);
        spacer = (View) findViewById(R.id.member_spacer_bottom);

        if (!SettingsCache.isUserAdmin()) {
            inviteButton.setVisibility(View.GONE);
            spacer.setVisibility(View.GONE);
        }

        loadMembers();
    }

    private void loadMembers() {
        progressBarLayout.setVisibility(View.VISIBLE);

        apiUtil.loadUsersByGroupAsync(SettingsCache.CURRENTGROUP.groupId);
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.LOADUSERS_CALLBACK) {
            if (result != null && result instanceof ArrayList) {
                users = (ArrayList<User>)result;
                adapter = new MemberAdapter(users, getApplicationContext());
                memberList.setAdapter(adapter);
                memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    }
                });
                memberList.setVisibility(View.VISIBLE);
            }
        }

        progressBarLayout.setVisibility(View.GONE);
    }
}
