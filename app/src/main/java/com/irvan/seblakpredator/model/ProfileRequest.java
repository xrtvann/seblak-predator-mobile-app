package com.irvan.seblakpredator.model;

public class ProfileRequest {
    private String name;
    private String email;
    private String username;
    private int phone;
    public ProfileRequest(String name, String email, String username, int phone){
        this.name=name;
        this.email=email;
        this.username=username;
        this.phone=phone;
    }
}
