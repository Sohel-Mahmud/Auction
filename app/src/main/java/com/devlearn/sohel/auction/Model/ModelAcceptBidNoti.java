package com.devlearn.sohel.auction.Model;

import com.google.gson.annotations.SerializedName;

public class ModelAcceptBidNoti {
    @SerializedName("auction_id")
    private int auction_id;
    @SerializedName("auction_title")
    private String auction_title;
    @SerializedName("user_token")
    private String user_token;
    @SerializedName("response")
    private String response;

    public String getAuction_title() {
        return auction_title;
    }

    public void setAuction_title(String auction_title) {
        this.auction_title = auction_title;
    }

    public int getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(int auction_id) {
        this.auction_id = auction_id;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
