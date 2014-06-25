package com.resist.movelife;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private int uid;
    private String name;
    private String email;
    private Double latitude;
    private Double longitude;

    public User(int uid,String name) {
        this.uid = uid;
        this.name = name;
    }

    public User(int uid,String name,String email,double latitude,double longitude) {
        this.uid = uid;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(Cursor c) {
        this(
                c.getInt(c.getColumnIndex("uid")),
                null,
                c.getString(c.getColumnIndex("email")),
                c.getDouble(c.getColumnIndex("latitude")),
                c.getDouble(c.getColumnIndex("longitude"))
        );
        try {
            this.name = c.getString(c.getColumnIndex("name"));
        } catch(Exception e) {}
    }

    public User(JSONObject json) throws JSONException {
        this(json.getInt("uid"),json.getString("name"));
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
