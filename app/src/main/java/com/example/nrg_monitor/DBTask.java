package com.example.nrg_monitor;


import android.os.AsyncTask;

import okhttp3.Request;


public class DBTask extends AsyncTask<DBRunnable, Void, DBRunnable> {


    @Override
    protected Request doInBackground(DBRunnable... dbRunnables) {
        Request request = dbRunnables[0].executeDBTask();
        return request;

    }

    @Override
    protected void onPostExecute(DBRunnable runnable) {
        runnable.postExecuteDBTask(runnable.);
    }

    /*
    private Context activityContext;
    private UserContainer user;
    private DatabaseRequestHandler dbHandler = new DatabaseRequestHandler();
    //id of the process that calls execute

    public DBTask(Context context) {

        this.activityContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public Integer doInBackground(String... strings) {

        user = new UserContainer(strings[0], strings[1], strings[2]);
        Gson gson = new Gson();

        String userJsonString = gson.toJson(user);

        //Log.d("Dima", userJsonString);

        dbHandler.insertToUsersDb(userJsonString);
        JsonObject obj = new JsonObject();
        obj.addProperty("email",user.getEmail());

        dbHandler.emailExists(obj.toString());

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


     */


}
