package com.irvan.seblakpredator.apiclient;

public class ProfileRequest {
    private String name;
    private String email;
    private String username;
    private String phone;
    public ProfileRequest(String name, String email, String username, String phone){
        this.name=name;
        this.email=email;
        this.username=username;
        this.phone=phone;
    }
}
