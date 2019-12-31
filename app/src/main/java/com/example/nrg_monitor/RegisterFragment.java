package com.example.nrg_monitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private LocalUsersDatabaseHelper databaseHelper;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        username = (TextInputEditText) getView().findViewById(R.id.reg_username);
        email = (TextInputEditText) getView().findViewById(R.id.reg_email);
        password = (TextInputEditText) getView().findViewById(R.id.reg_password);
        Button register_button = (Button) getView().findViewById(R.id.register_button);
        rep_password = (TextInputEditText) getView().findViewById(R.id.reg_password_repeat);

        register_button.setOnClickListener(this);

        databaseHelper = new LocalUsersDatabaseHelper(getView().getContext());


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

            case R.id.register_button:

                if (allValid()) {
                    //if result is true the transaction was completed successfully
                    //boolean result = databaseHelper.insertData(email.getText().toString(), username.getText().toString(), password.getText().toString());
                    RestPostRequestHandler postRequest = new RestPostRequestHandler(getView().getContext());
                    //Toast.makeText(getView().getContext(), "Returned " + result, Toast.LENGTH_SHORT).show();

                    postRequest.execute(email.getText().toString(),username.getText().toString(),password.getText().toString());

                }


                break;

        }

    }

    public boolean isEmailValid() {

        String email_string = email.getText().toString();
        TextInputLayout email_layout = (TextInputLayout) getView().findViewById(R.id.reg_email_parent);
        if (email_string.isEmpty()) {
            email_layout.setError("An e-mail address is required");
            return false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email_layout.setError("Wrong e-mail format");
            return false;
        }
        /*
        else if (databaseHelper.emailExists(email_string)) {
            email_layout.setError("Account with that e-mail address already exists");
            return false;
        }*/
         else {
            email_layout.setError(null);
            return true;
        }
    }

    public boolean isValidPassword() {

        String password_string = password.getText().toString();
        TextInputLayout password_layout = (TextInputLayout) getView().findViewById(R.id.reg_password_parent);

        if (!PASSWORD_PATTERN.matcher(password_string).matches()) {
            password_layout.setError("Password should have no whitespaces\n" +
                    "Password must be at least 8 characters\n" +
                    "Password must contain at least one digit");
            return false;
        }
        else if (password_string.isEmpty()) {
            password_layout.setError("A password is required");
            return false;
        } else {
            password_layout.setError(null);
            return true;
        }


    }

    public boolean matchingPasswords() {

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

        String username_string = username.getText().toString();
        TextInputLayout username_layout = (TextInputLayout) getView().findViewById(R.id.reg_username_parent);
        if (username_string.isEmpty()) {
            username_layout.setError("A username is required");
            return false;
        }/*
        else if (databaseHelper.emailExists(username_string)) {
            username_layout.setError("Account with that username already exists");
            return false;
        }*/
        else {
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

        return email && usrname && pass && repeat;

    }


}
