package com.devlearn.sohel.auction.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPref {
    private Context context;
    private int user_id;
    private String user_name;
    private String user_email;
    private String user_role;
    private SharedPreferences sp;
    private int defaultValue = -1;

    public UserSharedPref(Context context)
    {
        this.context = context;
        sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
    }

    public int getUser_id() {
        return sp.getInt("user_id",-1);
    }

    public void setUser_id(int user_id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("user_id",user_id);
        editor.apply();
        this.user_id = user_id;
    }

    public String getUser_name() {
        return sp.getString("user_name","");
    }

    public void setUser_name(String user_name) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_name", user_name);
        editor.apply();
    }

    public String getUser_email() {
        return sp.getString("user_email","");
    }

    public void setUser_email(String user_email) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_email", user_email);
        editor.apply();
    }

    public String getUser_role() {
        return sp.getString("user_role","");
    }

    public void setUser_role(String user_role) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_role",user_role);
        editor.apply();
    }

    public void clearAll()
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
