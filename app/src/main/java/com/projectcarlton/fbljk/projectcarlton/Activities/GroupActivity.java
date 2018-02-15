package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbacks;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Activities.Moduls.MemberActivity;
import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.R;

public class GroupActivity extends AppCompatActivity implements APICallback {

    public static Group currentGroup;

    private Button memberButton;
    private Button deleteButton;
    private Button leaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        if (getIntent() != null && getIntent().getExtras() != null) {
            currentGroup = new Group();
            currentGroup.groupId = getIntent().getExtras().getString("GroupId");
            currentGroup.groupName = getIntent().getExtras().getString("GroupName");
            currentGroup.groupDescription = getIntent().getExtras().getString("GroupDescription");
            currentGroup.groupAdmin = getIntent().getExtras().getString("GroupAdmin");

            Toolbar grouptoolbar = (Toolbar) findViewById(R.id.group_toolbar);
            grouptoolbar.setTitle(currentGroup.groupName);
            setSupportActionBar(grouptoolbar);
        }

        memberButton = (Button) findViewById(R.id.group_member_button);
        memberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, MemberActivity.class);
                startActivity(intent);
            }
        });

        deleteButton = (Button) findViewById(R.id.group_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteGroup();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                builder.setMessage(R.string.deletegroup_dialog_message).setPositiveButton(R.string.button_yes, dialogClickListener).setNegativeButton(R.string.button_no, dialogClickListener);
                builder.create().show();
            }
        });

        leaveButton = (Button) findViewById(R.id.group_leave_button);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                leaveGroup();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                builder.setMessage(R.string.leavegroup_dialog_message).setPositiveButton(R.string.button_yes, dialogClickListener).setNegativeButton(R.string.button_no, dialogClickListener);
                builder.create().show();
            }
        });

        if (!isUserAdmin()) {
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void leaveGroup() {
        String apiUrl = getString(R.string.API_URL) + "group?userid=" + GroupsActivity.currentUser.userId + "&groupname=" + groupName + "&groupdescription=" + groupDesc;
        APIGetRequest request = new APIGetRequest(this, CallbackType.LEAVEGROUP_CALLBACK, 5000);
        request.execute(apiUrl);
    }

    private void deleteGroup() {
        String apiUrl = getString(R.string.API_URL) + "group?userid=" + GroupsActivity.currentUser.userId + "&groupname=" + groupName + "&groupdescription=" + groupDesc;
        APIGetRequest request = new APIGetRequest(this, CallbackType.DELETEGROUP_CALLBACK, 5000);
        request.execute(apiUrl);
    }

    public static boolean isUserAdmin() {
        if (GroupsActivity.currentUser != null && GroupActivity.currentGroup != null) {
            if (GroupActivity.currentGroup.groupAdmin.equals(GroupsActivity.currentUser.userId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.LEAVEGROUP_CALLBACK) {
            if (resultString != null && !resultString.equals("") && !resultString.equals("null")) {
                if (resultString.equals("1")) {
                    ActivityCallbacks.request(ActivityCallbackType.GROUPRELOAD_CALLBACK);
                    onBackPressed();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.API_ERROR, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        } else if (callbackType == CallbackType.DELETEGROUP_CALLBACK) {
            if (resultString != null && !resultString.equals("") && !resultString.equals("null")) {
                if (resultString.equals("1")) {
                    ActivityCallbacks.request(ActivityCallbackType.GROUPRELOAD_CALLBACK);
                    onBackPressed();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.API_ERROR, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
    }
}