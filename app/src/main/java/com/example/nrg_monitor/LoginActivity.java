package com.example.nrg_monitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rel_lay_1;
    private TextView app_name;
    private Handler handler = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {

            rel_lay_1.setVisibility(View.VISIBLE);


        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {

            app_name.setVisibility(View.VISIBLE);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rel_lay_1 = findViewById(R.id.rel_lay_1);
        app_name = findViewById(R.id.app_name);

        handler.postDelayed(runnable2,1000);
        handler.postDelayed(runnable1, 2000);

        Button sign_up_button = findViewById(R.id.signup_button);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sign_up_intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(sign_up_intent);

            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient myGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }
}
