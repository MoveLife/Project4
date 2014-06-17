package com.resist.movelife;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Thomas on 17-6-2014.
 */
public class PlaatsReview extends Activity {

    RatingBar ratingBar;
    Button btn;
    ContentValues cv = new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        ratingBar=(RatingBar)findViewById(R.id.rb_review);
        btn=(Button)findViewById(R.id.btn_review);

        // Set ChangeListener to Rating Bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {
            // TODO Auto-generated method stub
                float rating = ratingBar.getRating();
                List<Company> lijst = Company.getCompanies();


                  //  cv.put("uid", );
                   // cv.put("name", );
                   // cv.put("latitude", );
                   // cv.put("longitude", );
                   // cv.put("rating", rating);
                   // LocalDatabaseConnector.insert("companies", null, cv);

                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating), Toast.LENGTH_LONG).show();
            }
        });

    }
}












