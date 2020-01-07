package com.example.nrg_monitor.ui.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;
import com.example.nrg_monitor.UserContainer;
import com.example.nrg_monitor.home.config.HomeConfigActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    //regular expression Pattern for passwords and username
    private final static Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    private final static Pattern USERNAME_PATTERN =
            Pattern.compile("/^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$/");


    private TextInputEditText username, email, password, rep_password;
    private boolean email_exists = false;
    private boolean username_exists = false;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    //used onViewCreated because I had problems with onCreateView at first, too much of a haste to change everything up again
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mContext = getContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        //get views for needed on screen elements
        username = (TextInputEditText) getView().findViewById(R.id.reg_username);

        /*
            when username textEdit loses focus an async task is triggered to check if the username exists in the database
            had trouble doing all the checks inside the onClick
            the variable username_exits takes a value in onPostExecute of the method so that onClick can check
            things later. There might be a better solution.
            Checks inputs and informs user before pressing the button
         */
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    FindUsernameCall checkUser = new FindUsernameCall();
                    checkUser.execute(username.getText().toString());
                }
            }
        });

        //same as username
        email = (TextInputEditText) getView().findViewById(R.id.reg_email);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    FindEmailCall checkMail= new FindEmailCall();
                    checkMail.execute(email.getText().toString());
                }
            }
        });
        password = (TextInputEditText) getView().findViewById(R.id.reg_password);
        Button register_button = (Button) getView().findViewById(R.id.register_button);
        rep_password = (TextInputEditText) getView().findViewById(R.id.reg_password_repeat);

        //set on click listener to register button
        register_button.setOnClickListener(this);

    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);


    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //checks which button was clicked , only one in our case here
            case R.id.register_button:

                TextInputLayout email_layout = (TextInputLayout) getView().findViewById(R.id.reg_email_parent);
                TextInputLayout user_layout = (TextInputLayout) getView().findViewById(R.id.reg_username_parent);



                //checks if all the inputs follow the required patterns and checks if email and username exists in the database
                if (allValid() && (!email_exists) && (!username_exists)) {
                    //calls Insert Async task
                    InsertCall insert = new InsertCall();
                    insert.execute(email.getText().toString(),username.getText().toString(),password.getText().toString());
                    Intent startHomeConfig = new Intent(getActivity(),HomeConfigActivity.class);
                    startHomeConfig.putExtra("email",email.getText().toString());
                    startHomeConfig.putExtra("username",username.getText().toString());
                    startHomeConfig.putExtra("source","postRegister");

                    mSharedPreferences.edit().putBoolean(mContext.getResources().getString(R.string.logged_in),true).apply();
                    mSharedPreferences.edit().putString(mContext.getResources().getString(R.string.logged_in_user),username.getText().toString());
                    mSharedPreferences.edit().putString(mContext.getResources().getString(R.string.logged_in_user_email),email.getText().toString());

                    mContext.startActivity(startHomeConfig);
                }

                if(email_exists){
                    //prints the error
                    email_layout.setError("User with that email already exists");
                }
                if(username_exists){
                    //prints error
                    user_layout.setError("User with that username already exists");

                }

                break;
        }

    }

    public boolean isEmailValid() {

        //checks if Email input is empty , follows a pattern
        String email_string = email.getText().toString();
        TextInputLayout email_layout = (TextInputLayout) getView().findViewById(R.id.reg_email_parent);
        if (email_string.isEmpty()) {
            email_layout.setError("An e-mail address is required");
            return false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email_layout.setError("Wrong e-mail format");
            return false;
        }else {
            email_layout.setError(null);
            return true;
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

    public boolean isValidPassword() {

        //checks if password follows a pattern , is empty
        String password_string = password.getText().toString();
        TextInputLayout password_layout = (TextInputLayout) getView().findViewById(R.id.reg_password_parent);

        if (!PASSWORD_PATTERN.matcher(password_string).matches()) {
            password_layout.setError("Password should have no whitespaces\n" +
                    "Password must be at least 8 characters\n" +
                    "Password must contain at least one digit");
            return false;
        } else if (password_string.isEmpty()) {
            password_layout.setError("A password is required");
            return false;
        } else {
            password_layout.setError(null);
            return true;
        }


    }

    public boolean matchingPasswords() {

        //checks  if password Repeat filled has the same input as the password field
        String password_string = password.getText().toString();
        String rep_password_string = rep_password.getText().toString();
        //Log.d("Dima",password_string+" "+rep_password_string);
        TextInputLayout rep_password_layout = (TextInputLayout) getView().findViewById(R.id.reg_password_repeat_parent);

        if (isValidPassword()) {
            if (!password_string.equals(rep_password_string)) {
                rep_password_layout.setError("Passwords not matching");
                return false;
            } else {
                rep_password_layout.setError(null);
                return true;
            }
        } else if (rep_password_string.isEmpty()) {
            rep_password_layout.setError("Repeat the password entered above");
            return false;
        } else
            return false;

    }

    public boolean isValidUsername() {

        //checks if username field is empty
        String username_string = username.getText().toString();
        TextInputLayout username_layout = (TextInputLayout) getView().findViewById(R.id.reg_username_parent);
        if (username_string.isEmpty()) {
            username_layout.setError("A username is required");
            return false;
        }else {
            username_layout.setError(null);
            return true;
        }



    }

    public boolean allValid() {
        boolean email, usrname, pass, repeat;

        email = isEmailValid();
        usrname = isValidUsername();
        pass = isValidPassword();
        repeat = matchingPasswords();

        //collects ll results from checks , if all true returns true
        return email && usrname && pass && repeat;

    }

    private class InsertCall extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {

            //executes insert call to database from DB handler
            DbRequestHandler dbRequestHandler = new DbRequestHandler();

            //insertToDb returns a string with the object added to the db
            return dbRequestHandler.insertToDb(strings[0],strings[1],strings[2]);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            UserContainer user= new UserContainer();
            user = gson.fromJson(s,UserContainer.class);
            //Log.d("Dima",""+user.getEmail());

            Toast.makeText(getView().getContext(),"User with email: "+user.getEmail()+"\n"+"and username: "+user.getUsername()+" registered successfully!",Toast.LENGTH_LONG).show();
        }
    }

    private class FindEmailCall extends AsyncTask<String,Void,Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TextInputLayout email_layout = (TextInputLayout) getView().findViewById(R.id.reg_email_parent);
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
            // if the server returns 1 , it means there is one record with the email of the input , returns true
            //if 0 , none , returns false
            super.onPostExecute(i);
            //Log.d("Dima","Entries found with that email :"+i);
            Integer matches_found = i;
            if(matches_found>0){
                TextInputLayout email_layout = (TextInputLayout) getView().findViewById(R.id.reg_email_parent);
                email_layout.setError("User with that email already exists");
                email_exists = true;
            }
            else{
                email_exists = false;
            }

        }
    }
//same as email
    private class FindUsernameCall extends AsyncTask<String,Void,Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            TextInputLayout user_layout = (TextInputLayout) getView().findViewById(R.id.reg_username_parent);

            user_layout.setError(null);
        }

        @Override
        protected Integer doInBackground(String... strings) {

            DbRequestHandler dbRequestHandler = new DbRequestHandler();

            //insertToDb returns a string with the object added to the db
            return dbRequestHandler.usernameExists(strings[0]);

        }


        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            //Log.d("Dima","Entries found with that username :"+i);
            Integer matches_found = i;
            if(matches_found>0){
                TextInputLayout user_layout = (TextInputLayout) getView().findViewById(R.id.reg_username_parent);
                user_layout.setError("User with that username already exists");
                username_exists = true;
                //Log.d("Dima","value: "+username_exists);
            }
            else{
                username_exists = false;
            }
        }
    }

}
