package com.resist.movelife;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Events extends ListActivity {

    ListView listView;
    private CustomBaseAdapterAlleEvents adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        //adapter = new CustomBaseAdapterAlleBedrijven(this);
        listView = (ListView) findViewById(android.R.id.list);
        View empty = findViewById(android.R.id.empty);
        listView.setEmptyView(empty);

        listView.setAdapter(adapter);


    }
}