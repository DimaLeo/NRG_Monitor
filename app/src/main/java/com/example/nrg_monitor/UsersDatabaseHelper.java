package com.example.nrg_monitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.nrg_monitor.UsersContract.UserEntry;

public class UsersDatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "users.db";
    public static final int DATABASE_VERSION = 1;

    public UsersDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE "+
                UserEntry.TABLE_NAME+" ("+
                UserEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                UserEntry.COLUMN_EMAIL+" TEXT NOT NULL UNIQUE, "+
                UserEntry.COLUMN_USR_NAME+" TEXT NOT NULL UNIQUE , "+
                UserEntry.COLUMN_PASS+" TEXT NOT NULL"+
                ");";

        db.execSQL(SQL_CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+UserEntry.TABLE_NAME);

    }
}
