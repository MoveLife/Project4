package com.resist.movelife;


import android.app.Activity;
import android.os.Bundle;



public class Events extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

    }
}