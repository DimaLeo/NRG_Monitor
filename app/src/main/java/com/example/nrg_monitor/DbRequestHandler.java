package com.example.nrg_monitor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DbRequestHandler{

    private static final String WEB_SERVICE_URL = "http://10.0.2.2:8080";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String response="";

    public String insertToDb(String email,String username,String password){

        UserContainer user = new UserContainer(email,username,password);
        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        return response = dbCommunication("/users",userJson);
    }

    public Integer emailExists(String email){

        JsonObject obj = new JsonObject();

        obj.addProperty("email",email);

        response = dbCommunication("/users/findEmail",obj.toString());

        return Integer.parseInt(response);
    }

    public Integer usernameExists(String username){
        JsonObject obj = new JsonObject();
        obj.addProperty("username",username);

        response = dbCommunication("/users/findUsername",obj.toString());

        return Integer.parseInt(response);

    }

    private String dbCommunication(String endOfUrl, String jsonMessage){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonMessage, JSON);
        Request request = new Request.Builder().url(WEB_SERVICE_URL+""+endOfUrl).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            String httpResponse = response.body().string();
            Log.d("Dima",httpResponse);
            return httpResponse;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}



