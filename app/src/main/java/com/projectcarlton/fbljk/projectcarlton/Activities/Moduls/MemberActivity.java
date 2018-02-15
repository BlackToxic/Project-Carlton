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

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Activities.GroupActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.GroupsActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.InviteActivity;
import com.projectcarlton.fbljk.projectcarlton.Adapter.MemberAdapter;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemberActivity extends AppCompatActivity implements APICallback {

    private Button inviteButton;
    private ListView memberList;
    private View spacer;
    private LinearLayout progressBarLayout;

    private ArrayList<User> users;
    private MemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

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

        if (!GroupActivity.isUserAdmin()) {
            inviteButton.setVisibility(View.GONE);
            spacer.setVisibility(View.GONE);
        }

        loadMembers();
    }

    private void loadMembers() {
        progressBarLayout.setVisibility(View.VISIBLE);

        String apiUrl = getString(R.string.API_URL) + "user?groupid=" + GroupActivity.currentGroup.groupId;
        APIGetRequest request = new APIGetRequest(this, CallbackType.LOADUSERS_CALLBACK, 5000);
        request.execute(apiUrl);
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.LOADUSERS_CALLBACK) {
            if (resultString != null && !resultString.equals("")) {
                try {
                    if (resultString.contains("id")) {
                        users = new ArrayList<User>();

                        JSONArray array = new JSONArray(resultString);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            User newUser = new User();
                            newUser.userId = childObject.getString("id");
                            newUser.userName = childObject.getString("username");

                            if (!newUser.userId.equals(GroupsActivity.currentUser.userId))
                                users.add(newUser);
                        }

                        adapter = new MemberAdapter(users, getApplicationContext());
                        memberList.setAdapter(adapter);
                        memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            }
                        });
                        memberList.setVisibility(View.VISIBLE);
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
