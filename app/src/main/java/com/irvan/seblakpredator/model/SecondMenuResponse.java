package com.irvan.seblakpredator.model;

import java.util.List;

public class SecondMenuResponse {
    private boolean success;
    private List<Topping> data;
    private Meta meta;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public List<Topping> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public String getMessage() {
        return message;
    }
}
