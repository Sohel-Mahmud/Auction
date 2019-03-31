package com.devlearn.sohel.auction.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("user_id")
    int user_id;
    @SerializedName("user_name")
    String user_name;
    @SerializedName("user_email")
    String user_email;
    @SerializedName("user_phone")
    String user_phone;
    @SerializedName("user_password")
    String user_password;
    @SerializedName("user_address")
    String user_address;
    @SerializedName("user_token")
    String user_token;
    @SerializedName("user_role")
    String user_role;
    @SerializedName("response")
    String response;

//    public UserModel(String user_name, String user_email, String user_phone,String user_password, String user_address, String user_token, String user_role) {
//        this.user_name = user_name;
//        this.user_email = user_email;
//        this.user_phone = user_phone;
//        this.user_password = user_password;
//        this.user_address = user_address;
//        this.user_token = user_token;
//        this.user_role = user_role;
//    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
