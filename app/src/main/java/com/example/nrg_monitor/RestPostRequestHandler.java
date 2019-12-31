package com.example.nrg_monitor;


import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;


public class RestPostRequestHandler extends AsyncTask<String, Integer, Integer> {

    private Context activityContext;
    private UserContainer user;
    private DatabaseRequestHandler dbHandler = new DatabaseRequestHandler();

    public RestPostRequestHandler(Context context) {

        this.activityContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... strings) {

        user = new UserContainer(strings[0], strings[1], strings[2]);
        Gson gson = new Gson();

        String userJsonString = gson.toJson(user);

        //Log.d("Dima", userJsonString);

        dbHandler.insertToUsersDb(userJsonString);

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

    }



}
