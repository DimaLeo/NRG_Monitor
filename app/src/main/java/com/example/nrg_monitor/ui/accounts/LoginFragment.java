package com.example.nrg_monitor.ui.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;
import com.example.nrg_monitor.home.config.HomeConfigActivity;
import com.example.nrg_monitor.main.app.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private boolean hasHomeConfigured;

    private Button loginButton;
    private TextInputEditText emailInput,passwordInput;
    private boolean emailFound = false;
    private boolean correctPassword = false;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    //similar to RegisterFragment code, difference is in the passwordCheck async call
    //checks if the password entered matches the password for the record with the entered email address
    //gets user to next page
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = v.findViewById(R.id.login_email);
        passwordInput = v.findViewById(R.id.login_password);
        loginButton = v.findViewById(R.id.login_button);

        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    FindEmailCall checkMail= new FindEmailCall();
                    checkMail.execute(emailInput.getText().toString());
                }
            }
        });

        passwordInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                   new CheckPassword().execute(emailInput.getText().toString(),passwordInput.getText().toString());
                }
                else{
                    TextInputLayout passLayout = getView().findViewById(R.id.login_password_parent);
                    if(passLayout.getError()!=null){
                        passLayout.setError(null);

                    }
                }

            }
        });

        loginButton.setOnClickListener(this);




        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.login_button:

                //TODO: make db call to check wether the user has a home config by his email
                new TempFixForButtonOnClick().execute(emailInput.getText().toString());

                break;

        }



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity){
            a=(Activity) context;
        }
    }

    private class FindEmailCall extends AsyncTask<String,Void,Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TextInputLayout email_layout = (TextInputLayout) getView().findViewById(R.id.login_email_parent);
            email_layout.setError(null);

        }

        @Override
        protected Integer doInBackground(String... strings) {

            DbRequestHandler dbRequestHandler = new DbRequestHandler();

            //insertToDb returns a string with the object added to the db
            return dbRequestHandler.emailExists(strings[0]);

        }


        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            Log.d("Dima","Entries found with that email :"+i);
            Integer matches_found = i;
            if(matches_found>0){
                emailFound = true;
            }
            else{
                emailFound = false;
            }

        }
    }


    private class CheckPassword extends AsyncTask<String ,Void,Boolean>{
        @Override
        protected void onPreExecute() {



        }

        @Override
        protected Boolean doInBackground(String... strings) {

            DbRequestHandler dbRequestHandler = new DbRequestHandler();
            return dbRequestHandler.checkPassword(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean){
                correctPassword = true;
            }
            else{
                correctPassword = false;
            }

        }


    }


    private class TempFixForButtonOnClick extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            TextInputEditText passText = getView().findViewById(R.id.login_password);
            passText.clearFocus();
            //Log.d("Dima","From Home Login Fragment password check:"+correctPassword);
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            DbRequestHandler dbRequestHandler = new DbRequestHandler();
            hasHomeConfigured=dbRequestHandler.hasHomeConfigByEmail(strings[0]);

            return hasHomeConfigured;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            TextInputLayout password_outer = getView().findViewById(R.id.login_password_parent);
            TextInputLayout email_layout = getView().findViewById(R.id.login_email_parent);

            boolean emailEmpty = emailInput.getText().toString().isEmpty();
            boolean passwordEmpty = passwordInput.getText().toString().isEmpty();

            if(emailEmpty){
                email_layout.setError("Email field cannot be empty\nEnter a valid email address");
            }
            if(passwordEmpty){
                password_outer.setError("Password cannot be empty\nEnter a valid password");
            }

            if(!emailEmpty && !passwordEmpty){

                if(emailFound){

                    if(correctPassword){

                        mSharedPreferences.edit().putString(mContext.getResources().getString(R.string.logged_in_user_email),emailInput.getText().toString()).apply();
                        mSharedPreferences.edit().putBoolean(mContext.getResources().getString(R.string.logged_in),true).apply();

                        if(hasHomeConfigured){

                            Intent takeMeToMainMenu = new Intent((RegisterActivity)mContext, MainActivity.class);
                            Bundle extra = new Bundle();
                            extra.putString("email",emailInput.getText().toString());
                            takeMeToMainMenu.putExtras(extra);
                            mContext.startActivity(takeMeToMainMenu);


                        }
                        else {

                            Intent takeMeToMainMenu = new Intent((RegisterActivity)mContext, HomeConfigActivity.class);
                            Bundle extra = new Bundle();
                            extra.putString("source","postLogin");
                            extra.putString("email",emailInput.getText().toString());
                            takeMeToMainMenu.putExtras(extra);
                            mContext.startActivity(takeMeToMainMenu);
                        }

                    }
                    else{
                        password_outer.setError("Incorrect password");
                    }
                }
                else{
                    email_layout.setError("No user with that email address exists");

                }

            }





        }
    }
}
