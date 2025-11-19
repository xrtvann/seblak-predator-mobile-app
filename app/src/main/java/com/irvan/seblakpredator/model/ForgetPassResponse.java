package com.irvan.seblakpredator.model;

public class ForgetPassResponse {
    private boolean success;
    private String message;
    private Integer expires_in; // hanya muncul saat send_otp

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Integer getExpiresIn() {
        return expires_in;
    }
}
