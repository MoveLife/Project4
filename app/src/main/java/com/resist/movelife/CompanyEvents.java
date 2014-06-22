package com.resist.movelife;

import android.database.Cursor;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CompanyEvents {

    private int eid;
    private int bid;
    private int uid;
    private static List<CompanyEvents> companiesevents;
    private static SparseArray<List<CompanyEvents>> companiesEvents = new SparseArray<List<CompanyEvents>>();
    private String name;
    private String description;
    private Date startdate;
    private Date enddate;


    private void setAll(String name,String description) {
        this.name = name;
        this.description = description;
      //  this.startdate = startdate;
      //  this.enddate = enddate;
    }

    public CompanyEvents(int eid, int bid, int uid, String name, String description) {
        this.eid = eid;
        this.bid = bid;
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    private CompanyEvents(Cursor c) {
        this(
                c.getInt(c.getColumnIndex("eid")),
                c.getInt(c.getColumnIndex("bid")),
                c.getInt(c.getColumnIndex("uid")),
                c.getString(c.getColumnIndex("name")),
                c.getString(c.getColumnIndex("descrption"))
              //  c.getString(c.getColumnIndex("startdate")),
            //    c.getDouble(c.getColumnIndex("enddate"))
        );

        String name = null;
        String description = null;
        Date startdate = null;
        Date enddate = null;
        try {
            name = c.getString(c.getColumnIndex("name"));
        } catch(Exception e) {}
        try {
            description = c.getString(c.getColumnIndex("description"));
        } catch(Exception e) {}


        setAll(name,description);
    }

    public static void createCompanyEventsList() {
        companiesevents = new ArrayList<CompanyEvents>();
        Cursor c = LocalDatabaseConnector.get("events",new String[]{"eid","name","description","startdate","enddate","bid","uid"});
        if(c.moveToFirst()) {
            while(!c.isAfterLast()) {
                companiesevents.add(new CompanyEvents(c));
                c.moveToNext();
            }
        }
        c.close();
    }

    public static List<CompanyEvents> getCompanieEvents() {
        if(companiesevents == null) {
            createCompanyEventsList();
        }
        return companiesevents;
    }


    public static List<CompanyEvents> getCompaniesEvents(String name) {
            List<CompanyEvents> allCompanieEvents = getCompanieEvents();
            List<CompanyEvents> filtered = new ArrayList<CompanyEvents>();
            for(CompanyEvents c : allCompanieEvents) {

                if(name.equals(c.getName())  ) {
                    filtered.add(c);
                }
            }
            companiesEvents.put(Integer.parseInt(name),filtered);

        return companiesEvents.get(Integer.parseInt(name));
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
