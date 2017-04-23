package com.diplom.ilya.diplom;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.diplom.ilya.diplom.utils.AsyncResponse;
import com.diplom.ilya.diplom.utils.RunRequests;

public class ChooseSubjectActivity extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener{

    private ListView listView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_subject);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView t = (TextView) header.findViewById(R.id.nav_head_text_main);
        t.setText(getIntent().getStringExtra("user"));

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), ChooseTypeForSubjectActivity.class);
                i.putExtra("title", o.toString());
                i.putExtra("path", getIntent().getStringExtra("degree") + "/" + o.toString());
                startActivity(i);

            }
        });

        RunRequests.delegate = this;
        RunRequests.runGET(ChooseSubjectActivity.this, getIntent().getStringExtra("degree"));
    }

    @Override
    public void asyncProcessFinished(String result) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result.split(", "));
        listView.setAdapter(adapter);
        Toast.makeText(this, "response:"+result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        item.setChecked(true);
        if (id == R.id.nav_logout) {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();
            finish();
            Intent i = new Intent();
            i.setClass(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "Log out",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
