package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.R;

public class NewGroupActivity extends AppCompatActivity implements APICallback {

    private EditText groupname, groupdesc;
    private LinearLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

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

                String groupName = groupname.getText().toString();
                String groupDesc = groupdesc.getText().toString();

                if (!groupName.equals("") && !groupDesc.equals("")) {
                    String apiUrl = getString(R.string.API_URL) + "group?userid=" + GroupsActivity.currentUser.userId + "&groupname=" + groupName + "&groupdescription=" + groupDesc;
                    APIGetRequest request = new APIGetRequest(this, CallbackType.CREATEGROUP_CALLBACK, 5000);
                    progressBarLayout.setVisibility(View.VISIBLE);
                    request.execute(apiUrl);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.CREATEGROUP_CALLBACK) {
            if (resultString != null && !resultString.equals("")) {
                // TODO: Go back to GroupActivity and reload
                onBackPressed();
            }

            progressBarLayout.setVisibility(View.GONE);
        }
    }
}
