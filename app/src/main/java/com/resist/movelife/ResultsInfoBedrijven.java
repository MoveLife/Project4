package com.resist.movelife;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Thomas on 14-6-2014.
 */
public class ResultsInfoBedrijven extends Activity {


    public static Company filteredCompany = null;
    private Company company;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.alle_bedrijf_info);
        company = filteredCompany;
        List<Event> events = Event.getEventByCompany(company.getBid());
        TextView tv = (TextView) findViewById(R.id.tv_bedrijfsnaam);
        TextView tvdesc = (TextView) findViewById(R.id.tv_bedrijfsinfo);
        TextView tvevent = (TextView) findViewById(R.id.tv_eventText);

        if (events.size() >= 1){

            tvevent.setVisibility(View.GONE);

        }

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rb_rating);


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
}
