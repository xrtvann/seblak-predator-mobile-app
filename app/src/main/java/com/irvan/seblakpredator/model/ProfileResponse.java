package com.irvan.seblakpredator.model;

public class ProfileResponse {

    private boolean success;
    private String message;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    // ===================== NESTED DATA CLASS =====================
    public static class Data {

        private User user;
        private TokenInfo token_info;
        private Permissions permissions;

        public User getUser() {
            return user;
        }

        public TokenInfo getToken_info() {
            return token_info;
        }

        public Permissions getPermissions() {
            return permissions;
        }
    }

    // ===================== USER =====================
    public static class User {
        private int id;
        private String name;
        private String email;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    // ===================== TOKEN INFO =====================
    public static class TokenInfo {
        private long expires_at;
        private long issued_at;
        private String jwt_id;

        public long getExpires_at() {
            return expires_at;
        }

        public long getIssued_at() {
            return issued_at;
        }

        public String getJwt_id() {
            return jwt_id;
        }
    }

    // ===================== PERMISSIONS =====================
    public static class Permissions {
        private boolean is_admin;
        private boolean is_staff;
        private boolean is_customer;

        public boolean isAdmin() {
            return is_admin;
        }

        public boolean isStaff() {
            return is_staff;
        }

        public boolean isCustomer() {
            return is_customer;
        }
    }
}
