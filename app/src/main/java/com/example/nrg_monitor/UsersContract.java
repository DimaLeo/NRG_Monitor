package com.example.nrg_monitor;

import android.provider.BaseColumns;

public class UsersContract {

    public UsersContract() {
    }

    public static final class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USR_NAME = "user_name";
        public static final String COLUMN_EMAIL = "email_address";
        public static final String COLUMN_PASS = "password";
    }
}
