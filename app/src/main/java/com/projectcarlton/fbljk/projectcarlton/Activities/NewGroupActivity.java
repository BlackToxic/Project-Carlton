package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.projectcarlton.fbljk.projectcarlton.R;

public class NewGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        setTitle(R.string.newgroup_title);
    }
}
