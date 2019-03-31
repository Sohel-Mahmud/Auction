package com.devlearn.sohel.auction.Model;

import com.google.gson.annotations.SerializedName;

public class ModelAuctionDetails {
    @SerializedName("auction_bid_user_id")
    private int auction_bid_user_id;
    @SerializedName("auction_validity")
    private String auction_validity;
    @SerializedName("auction_bidprice")
    private double auction_bidprice;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("user_phone")
    private String user_phone;
    @SerializedName("latest_user_name")
    private String latest_user_name;
    @SerializedName("user_token")
    private String user_token;
    @SerializedName("auction_status")
    private String auction_status;

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getAuction_status() {
        return auction_status;
    }

    public void setAuction_status(String auction_status) {
        this.auction_status = auction_status;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public int getAuction_bid_user_id() {
        return auction_bid_user_id;
    }

    public void setAuction_bid_user_id(int auction_bid_user_id) {
        this.auction_bid_user_id = auction_bid_user_id;
    }

    public String getAuction_validity() {
        return auction_validity;
    }

    public void setAuction_validity(String auction_validity) {
        this.auction_validity = auction_validity;
    }

    public double getAuction_bidprice() {
        return auction_bidprice;
    }

    public void setAuction_bidprice(double auction_bidprice) {
        this.auction_bidprice = auction_bidprice;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLatest_user_name() {
        return latest_user_name;
    }

    public void setLatest_user_name(String latest_user_name) {
        this.latest_user_name = latest_user_name;
    }
}
