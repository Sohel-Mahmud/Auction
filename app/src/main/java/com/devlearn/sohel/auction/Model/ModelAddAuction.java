package com.devlearn.sohel.auction.Model;

import com.google.gson.annotations.SerializedName;

public class ModelAddAuction {
    @SerializedName("auction_id")
    private int auction_id;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("auction_title")
    private String auction_title;
    @SerializedName("auction_description")
    private String auction_description;
    @SerializedName("auction_image")
    private String auction_image;
    @SerializedName("auction_date")
    private String auction_date;
    @SerializedName("auction_validity")
    private String auction_validity;
    @SerializedName("auction_pricelimit")
    private double auction_pricelimit;
    @SerializedName("auction_bidprice")
    private double auction_bidprice;
    @SerializedName("auction_bid_user_id")
    private int auction_bid_user_id;
    @SerializedName("response")
    private String response;
    @SerializedName("auction_status")
    private String auction_status;

    public double getAuction_bidprice() {
        return auction_bidprice;
    }

    public void setAuction_bidprice(double auction_bidprice) {
        this.auction_bidprice = auction_bidprice;
    }

    public int getAuction_bid_user_id() {
        return auction_bid_user_id;
    }

    public void setAuction_bid_user_id(int auction_bid_user_id) {
        this.auction_bid_user_id = auction_bid_user_id;
    }

    public String getAuction_status() {
        return auction_status;
    }

    public void setAuction_status(String auction_status) {
        this.auction_status = auction_status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
