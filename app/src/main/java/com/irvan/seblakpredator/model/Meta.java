package com.irvan.seblakpredator.model;

import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("total")
    private String total;

    @SerializedName("page")
    private int page;

    @SerializedName("per_page")
    private int perPage;

    @SerializedName("last_page")
    private int lastPage;

    @SerializedName("from")
    private int from;

    @SerializedName("to")
    private String to;

    public String getTotal() { return total; }
    public int getPage() { return page; }
    public int getPerPage() { return perPage; }
    public int getLastPage() { return lastPage; }
    public int getFrom() { return from; }
    public String getTo(){return to;}

}
