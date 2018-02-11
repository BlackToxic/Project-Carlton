package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.R;

public class GroupActivity extends AppCompatActivity {

    public static Group currentGroup;

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
    }
}