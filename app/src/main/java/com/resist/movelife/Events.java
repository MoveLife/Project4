package com.resist.movelife;


import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class Events extends ListActivity {

    private ListView listView;
    private CustomBaseAdapterAlleEvents adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        List<Event> lijst = Event.getEvents();

        adapter = new CustomBaseAdapterAlleEvents(this, lijst);
        listView = (ListView) findViewById(android.R.id.list);
        View empty = findViewById(android.R.id.empty);
        listView.setEmptyView(empty);

        listView.setAdapter(adapter);


    }
}