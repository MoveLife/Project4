package com.resist.movelife;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private int uid;
    private String name;

    public User(int uid,String name) {
        this.uid = uid;
        this.name = name;
    }

    public User(JSONObject json) throws JSONException {
        this(json.getInt("uid"),json.getString("name"));
    }
}
