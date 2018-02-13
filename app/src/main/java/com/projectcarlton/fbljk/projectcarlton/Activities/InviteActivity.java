package com.projectcarlton.fbljk.projectcarlton.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.projectcarlton.fbljk.projectcarlton.R;

public class InviteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        Toolbar invitetoolbar = (Toolbar) findViewById(R.id.invite_toolbar);
        invitetoolbar.setTitle("");
        setSupportActionBar(invitetoolbar);
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
        // TODO: Search for Users

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // TODO: Search for Users

        return false;
    }
}
