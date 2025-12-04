package com.irvan.seblakpredator.model;

public class ChangePasswordRequest {
    private String user_id;
    private String old_password;
    private String new_password;
    private String confirm_password;

    // Constructor, Getter, and Setter
    public ChangePasswordRequest(String user_id, String old_password, String new_password, String confirm_password) {
        this.user_id = user_id;
        this.old_password = old_password;
        this.new_password = new_password;
        this.confirm_password = confirm_password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
}

