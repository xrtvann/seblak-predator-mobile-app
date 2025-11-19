package com.irvan.seblakpredator.apiclient;

public class ForgetPassRequest {
    private String action;
    private String email;
    private String otp;
    private String new_password;
    private String confirm_password;

    // SEND OTP
    public ForgetPassRequest(String action, String email) {
        this.action = action;
        this.email = email;
    }

    // VERIFY OTP
    public ForgetPassRequest(String action, String email, String otp) {
        this.action = action;
        this.email = email;
        this.otp = otp;
    }

    // RESET PASSWORD
    public ForgetPassRequest(String action, String email, String otp, String new_password, String confirm_password) {
        this.action = action;
        this.email = email;
        this.otp = otp;
        this.new_password = new_password;
        this.confirm_password = confirm_password;
    }
}
