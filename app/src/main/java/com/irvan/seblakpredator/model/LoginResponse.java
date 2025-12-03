package com.irvan.seblakpredator.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private boolean success;

    private String message;


    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private int expiresIn;

    private User user;

    // ===== GETTERS =====
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public User getUser() {
        return user;
    }

    // ===== USER INNER CLASS =====
    public static class User {

        private String id;

        private String username;

        private String name;

        private String email;

        private String role;

        // ===== GETTERS =====
        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }

}
