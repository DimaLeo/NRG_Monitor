package com.example.nrg_monitor.splash.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.R;
import com.example.nrg_monitor.home.config.HomeConfigActivity;
import com.example.nrg_monitor.main.app.MainActivity;
import com.example.nrg_monitor.ui.accounts.RegisterActivity;


public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Dima","From splash activity : on Destroyed");
    }

    private SharedPreferences mPreferences;
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //mPreferences.edit().putBoolean(getApplicationContext().getResources().getString(R.string.logged_in),false).apply();
        //mPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user),"").apply();
        //mPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user_email),"").apply();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Dima","From Splash Screen activity : on Start");
        boolean key = mPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.logged_in),false);
        Log.d("Dima","From Splash Screen activity : on Start, logged in ="+key);
        final boolean logged_in = mPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.logged_in),false);
        final boolean hasHomeConfig = mPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.has_home_config),false);
        final String username,email;
        if(logged_in){

            username = mPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user),"");
            email = mPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user_email),"");

        }
        else {
            email="";
            username="";
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(logged_in){
                    Intent signedInAction;
                    Bundle extra = new Bundle();
                    if(hasHomeConfig){

                        signedInAction = new Intent(SplashScreenActivity.this, MainActivity.class);
                        extra.putString("email",email);
                        extra.putString("username",username);
                        signedInAction.putExtras(extra);
                        SplashScreenActivity.this.startActivity(signedInAction);

                    }
                    else {
                        signedInAction = new Intent(SplashScreenActivity.this, HomeConfigActivity.class);
                        extra.putString("email",email);
                        extra.putString("username",username);
                        signedInAction.putExtras(extra);
                        SplashScreenActivity.this.startActivity(signedInAction);
                    }



                }
                else{
                    Intent notSignedInAction = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                    SplashScreenActivity.this.startActivity(notSignedInAction);
                }


            }
        },SPLASH_TIME_OUT);
    }
}
