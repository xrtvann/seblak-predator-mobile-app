package com.irvan.seblakpredator.model;

public class UpdateProfileRequest {
    private String name;
    private String username;
    private String email;
    private String phone_number;

    public UpdateProfileRequest(String name, String username, String email, String phone_number) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
    }
}
