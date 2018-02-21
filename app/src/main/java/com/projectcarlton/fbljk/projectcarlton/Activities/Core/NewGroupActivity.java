package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbacks;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

public class NewGroupActivity extends AppCompatActivity implements APIUtilCallback {

    private EditText groupname, groupdesc;
    private LinearLayout progressBarLayout;

    private APIUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        apiUtil = new APIUtil(getApplicationContext(), this);

        Toolbar newgrouptoolbar = (Toolbar) findViewById(R.id.newgroup_toolbar);
        newgrouptoolbar.setTitle(R.string.newgroup_title);
        setSupportActionBar(newgrouptoolbar);

        groupname = (EditText) findViewById(R.id.newgroup_groupname);
        groupdesc = (EditText) findViewById(R.id.newgroup_groupdescription);

        progressBarLayout = (LinearLayout) findViewById(R.id.newgroup_progressbar_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newgroup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newgroup_action_done:

                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                saveGroup();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.newgroup_dialog_question).setPositiveButton(R.string.button_yes, dialogClickListener).setNegativeButton(R.string.button_no, dialogClickListener);
                builder.create().show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveGroup() {
        String groupName = groupname.getText().toString();
        String groupDesc = groupdesc.getText().toString();

        if (!groupName.equals("") && !groupDesc.equals("")) {
            progressBarLayout.setVisibility(View.VISIBLE);
            apiUtil.createGroupAsync(groupName, groupDesc, GroupsActivity.currentUser.userId);
        }
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.CREATEGROUP_CALLBACK) {
            if (result != null && result instanceof Boolean) {
                if ((boolean)result) {
                    ActivityCallbacks.request(ActivityCallbackType.GROUPRELOAD_CALLBACK);
                    onBackPressed();
                }
            }
        }

        progressBarLayout.setVisibility(View.GONE);
    }
}
