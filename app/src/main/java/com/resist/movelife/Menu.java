package com.resist.movelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Menu extends Activity{
    private ViewFlipper myViewFlipper;
    private float initialXPoint;
    int[] image = { R.drawable.back,R.drawable.back1, R.drawable.back2,R.drawable.back3};
    private static DatabaseUpdater updater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.menu);
        if (updater == null) {
            updater = new DatabaseUpdater();

            updater.start(this);
        }
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

       // Look up the AdView as a resource and load a request.
       AdView adView = (AdView)this.findViewById(R.id.adView);
       AdRequest adRequest = new AdRequest.Builder()
      //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
      //.addTestDevice("DC5E69B2C6B90CD8B81EDA2BB2729EFF")
        .build();
        adView.loadAd(adRequest);

        myViewFlipper = (ViewFlipper) findViewById(R.id.myflipper);

        for (int i = 0; i < image.length; i++) {
            ImageView imageView = new ImageView(Menu.this);
            imageView.setImageResource(image[i]);
            myViewFlipper.addView(imageView);
        }
    }

    //Slideshow
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialXPoint = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalx = event.getX();
                if (initialXPoint > finalx) {
                    if (myViewFlipper.getDisplayedChild() == image.length)
                        break;
                    myViewFlipper.showNext();
                } else {
                    if (myViewFlipper.getDisplayedChild() == 0)
                        break;
                    myViewFlipper.showPrevious();
                }
                myViewFlipper.setAutoStart(true);
                myViewFlipper.setFlipInterval(8000);
                myViewFlipper.startFlipping();
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(updater.isRunning()) {
            updater.exit();
        }
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
    public void myAccount (View view) {
        Intent intent = new Intent(this, AccountSettings.class);
        startActivity(intent);
    }
    public void act_Status (View view) {
        Intent intent = new Intent(this, Status.class);
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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.myAccount) {
            Intent intent = new Intent(this, AccountSettings.class);
            startActivity(intent);
        }

       if (id == R.id.action_search)
       {
           Intent intent = new Intent(this, ZoekBedrijven.class);
           startActivity(intent);
       }

        return super.onOptionsItemSelected(item);
    }

    public static String currentEmail() {
        return updater.getEmail();
    }
    public static boolean isConnected() {
        return updater.isConnected();
    }
}
