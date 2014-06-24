package com.resist.movelife;

import android.database.Cursor;
import android.util.SparseArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {

    private int eid;
    private int bid;
    private int uid;
    private String name;
    private String description;
    private Date startdate;
    private Date enddate;


    public Event(int eid, int bid, int uid, String name, String description, String startdate, String enddate ){

        this.eid = eid;
        this.bid = bid;
        this.uid = uid;
        this.name = name;
        this.description = description;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.startdate = sdf.parse(startdate);
        } catch(ParseException e) {
            //geen date
        } try {
            this.enddate = sdf.parse(enddate);
        } catch (ParseException e){
            // geen date
        }

    }

    private Event(Cursor c) {
        this(
                c.getInt(c.getColumnIndex("eid")),
                c.getInt(c.getColumnIndex("bid")),
                c.getInt(c.getColumnIndex("uid")),
                c.getString(c.getColumnIndex("name")),
                c.getString(c.getColumnIndex("descrption")),
                c.getString(c.getColumnIndex("startdate")),
                c.getString(c.getColumnIndex("enddate"))
        );

    }

    public static List<Event> getEventByCompany(int bid){

        List<Event> output = new ArrayList<Event>();

        Cursor c = LocalDatabaseConnector.get("events",new String[]{"eid","name","description","startdate","enddate","bid","uid"},"bid = ?",new String[]{""+bid});
        if(c.moveToFirst()) {
            while(!c.isAfterLast()) {
                output.add(new Event(c));
                c.moveToNext();
            }
        }
        c.close();

        return output;
    }


    public int getEid() {
        return eid;
    }

    public int getBid() {
        return bid;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartdate() {
        return startdate;
    }

    public Date getEnddate() {
        return enddate;
    }





}
