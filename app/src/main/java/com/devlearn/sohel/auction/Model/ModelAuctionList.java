package com.devlearn.sohel.auction.Model;

import com.google.gson.annotations.SerializedName;

public class ModelAuctionList {
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
    @SerializedName("auction_status")
    private String auction_status;
    @SerializedName("num_of_bids")
    private int num_of_bids;

    public int getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(int auction_id) {
        this.auction_id = auction_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAuction_title() {
        return auction_title;
    }

    public void setAuction_title(String auction_title) {
        this.auction_title = auction_title;
    }

    public String getAuction_description() {
        return auction_description;
    }

    public void setAuction_description(String auction_description) {
        this.auction_description = auction_description;
    }

    public String getAuction_image() {
        return auction_image;
    }

    public void setAuction_image(String auction_image) {
        this.auction_image = auction_image;
    }

    public String getAuction_date() {
        return auction_date;
    }

    public void setAuction_date(String auction_date) {
        this.auction_date = auction_date;
    }

    public String getAuction_validity() {
        return auction_validity;
    }

    public void setAuction_validity(String auction_validity) {
        this.auction_validity = auction_validity;
    }

    public double getAuction_pricelimit() {
        return auction_pricelimit;
    }

    public void setAuction_pricelimit(double auction_pricelimit) {
        this.auction_pricelimit = auction_pricelimit;
    }

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

    public int getNum_of_bids() {
        return num_of_bids;
    }

    public void setNum_of_bids(int num_of_bids) {
        this.num_of_bids = num_of_bids;
    }
}
