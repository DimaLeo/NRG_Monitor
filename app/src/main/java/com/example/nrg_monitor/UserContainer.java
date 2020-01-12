package com.example.nrg_monitor;

//used to store data of the user object
public class UserContainer{

    private int id;
    private String email;
    private String username;
    private String password;

    public UserContainer(int id,String email, String username, String password) {

        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public UserContainer(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public UserContainer() {
    }


    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
