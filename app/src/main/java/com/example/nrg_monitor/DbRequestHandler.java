package com.example.nrg_monitor;

import android.util.Log;

import com.example.nrg_monitor.main.app.Device;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
    Class used to handle REST requests to database.
    That way the code is cleaner and easier to understand.
    Wanted to use Apache Http components at first but found out
    that library is deprecated since some version of Android.
    Used okhttp3 instead.
 */
public class DbRequestHandler {


    private static final String WEB_SERVICE_URL = "http://10.0.2.2:8080"; //10.0.2.2 points to the ip address of the parent of the emulator :8080 is the port of the tomcat server
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String response = "";

    public String insertToDb(String email, String username, String password) {

        //crate a user item using inputs from the user
        UserContainer user = new UserContainer(email, username, password);
        Gson gson = new Gson();
        //create a json item from the user object using gson
        String userJson = gson.toJson(user);

        // dbCommunication handles the communication
        // all methods use it , since only a Json is communicated to the server and it is parsed in the server methods depending on the call
        return response = dbCommunication("/users", userJson);
    }

    public Integer emailExists(String email) {

        JsonObject obj = new JsonObject();

        obj.addProperty("email", email);
        /*
            send the email input to database to check if it exists
            what we get back is the integer that counts how many emails were found
            used a method from an older assignment inside the db , that returns a list of
            users with a certain email address, and I return the size of that list
            since the email is unique in this database , it will either return 1 or 0
        */
        response = dbCommunication("/users/findEmail", obj.toString());


        return Integer.parseInt(response);
    }

    //the same as email, could be made into one method later
    public Integer usernameExists(String username) {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);

        response = dbCommunication("/users/findUsername", obj.toString());

        return Integer.parseInt(response);

    }


    public boolean checkPassword(String email, String password) {
        //gets the email and password from user login input and creates a json object
        JsonObject obj = new JsonObject();
        obj.addProperty("email", email);
        obj.addProperty("password", password);

        String jsonString = obj.toString();
        //Log.d("Dima",jsonString);

        //Inside the database it first checks if the email exists, if it does it compares the given password to the one registered
        //and returns either true or false
        response = dbCommunication("/users/checkPassword", jsonString);
        //Log.d("Dima",response);
        if (response.equals("true")) {
            return true;

        } else {
            return false;
        }

    }

    public String getUsernameFromDB(String email) {
        JsonObject obj = new JsonObject();
        obj.addProperty("email", email);

        String jsonString = obj.toString();
        response = dbCommunication("/users/getUsername", jsonString);
        return response;
    }

    public Boolean hasHomeConfigByUsername(String username) {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);

        String jsonString = obj.toString();
        response = dbCommunication("/users/getHomeConfig", jsonString);

        if (response.equals("0")) {
            return false;
        } else {
            return true;
        }


    }

    public Boolean hasHomeConfigByEmail(String email) {
        JsonObject obj = new JsonObject();
        obj.addProperty("email", email);

        String jsonString = obj.toString();
        response = dbCommunication("/users/getHomeConfig", jsonString);

        if (response.equals("0")) {
            return false;
        } else {
            return true;
        }


    }

    public void changeHomeConfigStatus(String username) {

        JsonObject obj = new JsonObject();

        String jsonString = obj.toString();
        response = dbCommunicationPut("/users/homeConfigChanged/" + username, jsonString);
        Log.d("Dima", response);
    }

    public ArrayList<Device> getAllDevices(String username){


        JsonObject obj = new JsonObject();
        obj.addProperty("username",username);
        String jsonString = obj.toString();
        response = dbCommunication("/findDevices",jsonString);
        Log.d("Dima",response);

        ArrayList<Device> devices = new ArrayList<Device>();


        try{
            JSONArray jsonArray = new JSONArray(response);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject object = jsonArray.getJSONObject(i);

                String device_name = object.getString("device_name");
                String device_type = object.getString("device_type");
                Integer device_brand = object.getInt("device_brand");
                Integer device_wattage = object.getInt("device_wattage");
                Double device_runtime = object.getDouble("device_runtime");
                Integer device_activity_status = object.getInt("device_activity_status");

                devices.add(new Device(device_name,device_type,device_brand,device_wattage,device_runtime,device_activity_status));

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return devices;

    }

    //handles request sending and reply receiving from server
    //gets the url for the required web service method and the json it is supposed to send
    private String dbCommunication(String endOfUrl, String jsonMessage) {

        //create a client
        OkHttpClient client = new OkHttpClient();
        //add Json String to the body of the request
        RequestBody body = RequestBody.create(jsonMessage, JSON);
        //send the request
        Request request = new Request.Builder().url(WEB_SERVICE_URL + "" + endOfUrl).post(body).build();
        try {
            //receive response
            Response response = client.newCall(request).execute();
            String httpResponse = response.body().string();
            //Log.d("Dima",httpResponse);
            return httpResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dbCommunicationPut(String endOfUrl, String jsonMessage) {

        //create a client
        OkHttpClient client = new OkHttpClient();
        //add Json String to the body of the request
        RequestBody body = RequestBody.create(jsonMessage, JSON);
        //send the request
        Request request = new Request.Builder().url(WEB_SERVICE_URL + "" + endOfUrl).put(body).build();
        try {
            //receive response
            Response response = client.newCall(request).execute();
            String httpResponse = response.body().string();
            //Log.d("Dima",httpResponse);
            return httpResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}



