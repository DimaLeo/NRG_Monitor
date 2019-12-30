package com.example.nrg_monitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import androidx.annotation.Nullable;

import com.example.nrg_monitor.UsersContract.UserEntry;

import static com.example.nrg_monitor.UsersContract.UserEntry.TABLE_NAME;

public class UsersDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final int DATABASE_VERSION = 1;

    public UsersDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase usersDb = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntry.COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                UserEntry.COLUMN_USR_NAME + " TEXT NOT NULL UNIQUE , " +
                UserEntry.COLUMN_PASS + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String email, String username, String password) {

        SQLiteDatabase usersDb = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(UsersContract.UserEntry.COLUMN_EMAIL, email);
        cv.put(UsersContract.UserEntry.COLUMN_USR_NAME, username);
        cv.put(UsersContract.UserEntry.COLUMN_PASS, password);

        long result = usersDb.insert(TABLE_NAME, null, cv);

        //in SQLiteOpenHelper insert declaration if the result is -1 then the transaction has failed
        return result != -1;
    }

    public boolean emailExists(String email){


        SQLiteDatabase usersDb = getWritableDatabase();

        String query = "SELECT "+UserEntry.COLUMN_EMAIL+
                       " FROM "+TABLE_NAME +
                       " WHERE "+UserEntry.COLUMN_EMAIL+" =?";

        Cursor cursor = usersDb.rawQuery(query,new String[]{email});

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        else return false;

    }

    public boolean usernameExists(String username){


        SQLiteDatabase usersDb = getWritableDatabase();

        String query = "SELECT "+UserEntry.COLUMN_USR_NAME+
                " FROM "+TABLE_NAME +
                " WHERE "+UserEntry.COLUMN_USR_NAME+" =?";

        Cursor cursor = usersDb.rawQuery(query,new String[]{username});

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        else return false;

    }


}
