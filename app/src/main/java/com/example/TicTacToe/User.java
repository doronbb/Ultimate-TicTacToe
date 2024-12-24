package com.example.TicTacToe;

public class User
{
    private String email;
    private String name;
    private String uid;


    public User() {
        // Default constructor for Firebase
    }

    public String getName() {
        return name;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    // Constructor to initialize the user object
    public User(String email) {
        this.email = email;
        int index = email.indexOf('@');
        this.name = email.substring(0,index);
    }
    public void UpdateUser()
    {
        int index = email.indexOf('@');
        this.name = email.substring(0,index);
    }

    // Getters and setters for user attributes
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
