package com.resist.movelife;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Thomas on 14-6-2014.
 */
public class ResultsInfoBedrijven extends Activity {
    public static Company filteredCompany = null;
    private Company company;
    private String currentEmail = Menu.getUpdater().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.alle_bedrijf_info);
        company = filteredCompany;
        List<Event> events = Event.getEventByCompany(company.getBid());
        List<Review> reviews = Review.getReviewByCompanyID(company.getBid());
        TextView tv = (TextView) findViewById(R.id.tv_bedrijfsnaam);
        TextView tvdesc = (TextView) findViewById(R.id.tv_bedrijfsinfo);
        TextView tvevent = (TextView) findViewById(R.id.tv_eventText);
        TextView reviewtxt = (TextView) findViewById(R.id.tv_reviewtxt);

        final ImageView logo = (ImageView)findViewById(R.id.iv_bedrijfsfoto);
        final ResultsInfoBedrijven parent = this;
        new Thread(new Runnable() {
            public void run() {
                final Bitmap bitmap = company.getLogo();
                if(bitmap != null) {
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            logo.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        }).start();

        if (events.size() >= 1){
            tvevent.setVisibility(View.GONE);
        }

        if (reviews.size() >= 1){
            reviewtxt.setVisibility(View.GONE);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.FILL_PARENT);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rb_rating);
        Log.d("reviewlist", ""+ reviews.size());
        for (Review review: reviews){

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.reviewll);
            TextView reviewUid = new TextView(this);
            TextView reviewText = new TextView(this);


            RatingBar reviewRating = new RatingBar(this ,null,android.R.attr.ratingBarStyleSmall);
            reviewRating.setLayoutParams(lp);
            reviewRating.setEnabled(false);
            reviewRating.setMax(5);
            reviewRating.setStepSize(0.01f);
            reviewRating.invalidate();
            reviewRating.setNumStars(5);


            review.getReview();
            review.getRating();

            reviewUid.setText(currentEmail);
            reviewUid.setLayoutParams(layoutParams);
            reviewText.setText(review.getReview());
            reviewRating.setRating(review.getRating());

            linearLayout.addView(reviewUid,0);
            linearLayout.addView(reviewRating,1);
            linearLayout.addView(reviewText,2);


        }

        Log.d("eventlist",""+ events.size());
        for (Event event: events){
            SimpleDateFormat df = new SimpleDateFormat("c d MMMM yyyy HH:mm");
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.info);
            TextView eventText = new TextView(this);
            TextView eventdesc = new TextView(this);
            TextView eventstart = new TextView(this);
            TextView eventeind = new TextView(this);

            event.getName();
            event.getDescription();

            eventText.setText(event.getName());
            eventdesc.setText(event.getDescription());
            eventstart.setText(df.format(event.getStartdate()));
            eventeind.setText( df.format(event.getEnddate()));

            linearLayout.addView(eventText,0);
            linearLayout.addView(eventdesc,1);
            linearLayout.addView(eventstart,2);
            linearLayout.addView(eventeind,3);

        }


        double rating = company.getRating();
        float frating = (float) rating;

        if (ratingBar != null) {
            ratingBar.setEnabled(false);
            ratingBar.setMax(5);
            ratingBar.setStepSize(0.01f);
            ratingBar.setRating(frating);
            ratingBar.invalidate();
        }
        tv.setText(company.getName());
        tvdesc.setText(company.getDescription());


        Button navigatieButton = (Button) findViewById(R.id.navbtn);
        navigatieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria cri = new Criteria();
                String bestProvider = locManager.getBestProvider(cri, true);
                Location loc = locManager.getLastKnownLocation(bestProvider);
                Log.d("location", "" + loc);
                String latMy = String.valueOf(loc.getLatitude());
                String lngMy = String.valueOf(loc.getLongitude());
                String endLat = String.valueOf(company.getLatitude());
                String endLong = String.valueOf(company.getLongitude());

                Intent navigation = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latMy + ","
                                + lngMy + "&daddr=" + endLat + "," + endLong)
                );
                startActivity(navigation);

            }
        });

        Button reviewButton = (Button) findViewById(R.id.reviewBtn);


        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PlaatsReview.class);
               startActivity(intent);
            }
        });


        Button bekijkOpKaart = (Button) findViewById(R.id.button);
        bekijkOpKaart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getBaseContext(), Map.class);
                intent.putExtra("latitude", company.getLatitude());
                intent.putExtra("longitude", company.getLongitude());
                startActivity(intent);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eventmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.maak_event)
        {
            Intent intent = new Intent(this, PlaatsEvent.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
