package com.example.nrg_monitor.home.config;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;

public class HomeConfigActivity extends AppCompatActivity {

    private String username;
    private String email;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_config);
        Intent sourceIntent = getIntent();
        Bundle sourceExtras = sourceIntent.getExtras();
        Log.d("Dima","From Home config Activity"+sourceExtras.getString("email"));
        Log.d("Dima","From Home config Activity"+sourceExtras.getString("source"));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView username_view = findViewById(R.id.username_text);

        if(!mSharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.logged_in),false)){
            if(sourceExtras.getString("source").equals("postRegister")){
                username= sourceExtras.getString("username");
                email = sourceExtras.getString("email");
                username_view.setText(username);

                mSharedPreferences.edit().putBoolean(getApplicationContext().getResources().getString(R.string.logged_in),true).apply();
                mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user),username).apply();
                mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user_email),email).apply();

            }
            else if(sourceExtras.getString("source").equals("postLogin")){

                mSharedPreferences.edit().putBoolean(getApplicationContext().getResources().getString(R.string.logged_in),true).apply();
                email = sourceExtras.getString("email");
                mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user_email),email).apply();


                String savedUsername = mSharedPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user),"");
                if(!savedUsername.equals("")){
                    username = savedUsername;
                    mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user),username).apply();

                    username_view.setText(username);
                }
                else {
                    new GetUsernameFromDB().execute(email);

                }

            }
        }
        else {
            username = mSharedPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user),"");
            username_view.setText(username);
        }


        HomeConfigFragment homeConfigFragment = HomeConfigFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_config_fragment_container,homeConfigFragment).commit();



    }



    @Override
    protected void onPause() {
        super.onPause();

        Log.d("Dima","From Home Config Activity: on Pause");
        mSharedPreferences.edit().putBoolean(getApplicationContext().getResources().getString(R.string.logged_in),true).apply();
        mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user),username).apply();
        mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user_email),email).apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Dima","From Home Config Activity: on Resume");
        username = mSharedPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user),"");
        Log.d("Dima","From Home Config Activity: on Resume. username = "+username);
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
