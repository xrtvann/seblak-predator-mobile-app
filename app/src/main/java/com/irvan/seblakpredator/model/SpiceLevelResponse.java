package com.irvan.seblakpredator.model;

import java.util.List;
import java.util.Map;

public class SpiceLevelResponse {

    private boolean success;
    private List<SpiceLevel> data;
    private Map<String, Object> meta; // untuk total, page, per_page, dll
    private String message;

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<SpiceLevel> getData() {
        return data;
    }
    public void setData(List<SpiceLevel> data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }
    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
