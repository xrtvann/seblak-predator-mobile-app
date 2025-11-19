package com.irvan.seblakpredator.model;

public class RegisterResponse {
    private boolean success;

    private String message;
    private User user;

    public boolean isSuccess(){
        return success;
    }
    public String getMessage(){
        return message;
    }
    public User getUser() {
        return user;
    }
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

