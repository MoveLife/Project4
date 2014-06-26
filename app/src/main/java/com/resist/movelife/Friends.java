package com.resist.movelife;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Friends extends ListActivity implements
        AdapterView.OnItemClickListener {

    ListView listView;
    private CustomBaseAdapterAlleVrienden adapter = null;
    private static List<User> friends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.friends);


        List<User> lijst = getFriends();
        Log.d("vriendlijst", "" + lijst.size());
        adapter = new CustomBaseAdapterAlleVrienden(this, lijst);

        listView = (ListView) findViewById(android.R.id.list);
        View empty = findViewById(android.R.id.empty);
        listView.setEmptyView(empty);
        listView.setAdapter(adapter);


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
        Cursor c = LocalDatabaseConnector.rawQuery("SELECT user.uid,user.email,user.name,friendlocations.latitude,friendlocations.longitude,friendlocations.changed FROM user,friendlocations WHERE user.uid = friendlocations.uid AND friendlocations.longitude IS NOT NULL AND friendlocations.latitude IS NOT NULL", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                try {
                    users.add(new User(c));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                c.moveToNext();
            }
        }
        return users;
    }

    public static void createFriendList() {
        friends = new ArrayList<User>();

        Cursor c = LocalDatabaseConnector.get("user",new String[]{"uid","email","name","user != 1",null});
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                friends.add(new User(c));
                c.moveToNext();
            }
        }
        c.close();
    }

    public static List<User> getEvents() {
        if (friends == null) {
            createFriendList();
        }
        return friends;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vriendmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.vriendrequests) {
            Intent intent = new Intent(this, VriendToevoegen.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }
}