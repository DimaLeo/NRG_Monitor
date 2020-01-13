package com.example.nrg_monitor;

//used to store data of the user object
public class UserContainer{

    private int id;
    private String email;
    private String username;
    private String password;
    private int hasHomeConfig;

    public UserContainer(int id,String email, String username, String password) {

        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.hasHomeConfig = 0;
    }

    public UserContainer(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.hasHomeConfig = 0;
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

    public int getHasHomeConfig() {
        return hasHomeConfig;
    }

    public void setHasHomeConfig(int hasHomeConfig) {
        this.hasHomeConfig = hasHomeConfig;
    }
}
