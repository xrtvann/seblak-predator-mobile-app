package com.irvan.seblakpredator;

public class Users {
        public String email;
        public String otp;
        public boolean verified;

        public Users() {} // Diperlukan Firebase

        public Users(String email, String otp, boolean verified) {
            this.email = email;
            this.otp = otp;
            this.verified = verified;
        }


}
