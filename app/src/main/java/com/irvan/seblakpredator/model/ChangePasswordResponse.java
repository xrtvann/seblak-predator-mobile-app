package com.irvan.seblakpredator.model;

public class ChangePasswordResponse {
    private boolean success;
    private String message;

    // Getter dan Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

