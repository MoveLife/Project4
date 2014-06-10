package com.resist.movelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class Menu extends Activity {

    DatabaseUpdater updater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        updater = new DatabaseUpdater();
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check of je myAccount aanklikt en start daarna de activiteit AccountSettings.
        int id = item.getItemId();
        if (id == R.id.myAccount) {
            Intent intent = new Intent(this, AccountSettings.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
     public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(updater.isRunning()) {
            updater.exit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    private void startUpdater() {
        updater.start(this);
    }

    public void act_Login (View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    public void act_Map (View view) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }
    public void act_Status (View view) {
        Intent intent = new Intent(this, Status.class);
        startActivity(intent);
    }
    public void act_Categories (View view) {
        Intent intent = new Intent(this, Categories.class);
        startActivity(intent);
    }
    public void act_Friends (View view) {
        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }
    public void act_Events (View view) {
        Intent intent = new Intent(this, Events.class);
        startActivity(intent);
    }
}