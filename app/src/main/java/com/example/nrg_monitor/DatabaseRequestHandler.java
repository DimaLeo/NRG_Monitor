package com.example.nrg_monitor;

import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseRequestHandler {

    //url for local spring web service on host machine, listens to port 8080(tomcat)
    private static final String URL = "http://10.0.2.2:8080";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");




    public boolean insertToUsersDb(String jsonString){

        OkHttpClient cleint = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonString,JSON);
        Request request = new Request.Builder().url(URL+"/users").post(body).build();

        try(Response response = cleint.newCall(request).execute()){
            Log.d("Dima",response.body().string());
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;

    }


}

