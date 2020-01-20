package com.example.nrg_monitor.main.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;
import com.example.nrg_monitor.ui.accounts.RegisterActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private SharedPreferences mSharedPreferences ;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navigation_username_text = (TextView)headerView.findViewById(R.id.username_text);
        navigation_username_text.setText(username);
        TextView navigation_email_text = (TextView)headerView.findViewById(R.id.email_text);
        navigation_email_text.setText(mSharedPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user_email),""));
        new GetUsernameFromDB().execute(navigation_email_text.getText().toString());

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navigation_username_text = (TextView)headerView.findViewById(R.id.username_text);
        navigation_username_text.setText(username);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_device_control:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DeviceControlFragment()).commit();
                break;
            case R.id.nav_log_out:
                mSharedPreferences.edit().putBoolean(getApplicationContext().getResources().getString(R.string.logged_in),false).apply();
                mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user),"").apply();
                mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user_email),"").apply();
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                MainActivity.this.startActivity(intent);

                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    private class GetUsernameFromDB extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            DbRequestHandler dbRequestHandler = new DbRequestHandler();

            String usernameReturned = dbRequestHandler.getUsernameFromDB(strings[0]);

            return usernameReturned;

        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            username = s;
            mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user),username).apply();
            TextView username_view = findViewById(R.id.username_text);
            username_view.setText(s);


        }
    }


}
