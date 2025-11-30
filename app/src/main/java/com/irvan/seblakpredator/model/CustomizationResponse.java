package com.irvan.seblakpredator.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomizationResponse {
    private String status;
    private List<CustomizationOption> data;

    public String getStatus() { return status; }
    public List<CustomizationOption> getData() { return data; }
}

