package com.resist.movelife;


import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Friends extends ListActivity implements
        AdapterView.OnItemClickListener{

    ListView listView;
    private CustomBaseAdapterAlleVrienden adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.friends);
        listView = (ListView) findViewById(android.R.id.list);
        View empty = findViewById(android.R.id.empty);
        listView.setEmptyView(empty);
        listView.setAdapter(adapter);

        List<Company> lijst = Company.getCompanies();
        adapter = new CustomBaseAdapterAlleVrienden(this, lijst);

        listView.setOnItemClickListener(this);
    }
    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public static List<User> getFriends() {
        List<User> users = new ArrayList<User>();
        Cursor c = LocalDatabaseConnector.rawQuery("SELECT user.email,user.name,friendlocations.latitude,friendlocations.longitude,friendlocations.changed FROM user,friendlocations WHERE user.uid = friendlocations.uid AND friendlocations.longitude IS NOT NULL AND friendlocations.latitude IS NOT NULL",null);
        if(c.moveToFirst()) {
            while(c.isBeforeFirst()) {
                try {
                    users.add(new User(c));
                } catch(Exception e) {}
                c.moveToNext();
            }
        }
        return users;
    }
}