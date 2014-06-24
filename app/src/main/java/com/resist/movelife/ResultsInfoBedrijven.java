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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

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
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rb_rating);


        Log.d("eventlist",""+ events.size());
        for (Event event: events){
            View linearLayout =  findViewById(R.id.info);
            TextView eventText = new TextView(this);
            event.getName();
            eventText.setText(event.getName());
            eventText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) linearLayout).addView(eventText);





            event.getDescription();


            event.getStartdate();


            event.getEnddate();




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

        Button eventButton = (Button) findViewById(R.id.btn_eventStart);
       // if () {
       //     eventButton.setVisibility(View.GONE);

      //  } else {

       //     eventButton.setVisibility(View.VISIBLE);
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getBaseContext(), Map.class);
                    // intent.putExtra("latitude", company.getLatitude());
                    //  intent.putExtra("longitude", company.getLongitude());
                    startActivity(intent);

                }
            });
       // }

    }
}
