package com.resist.movelife;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.List;

public class PlaatsReview extends Activity {
  //  private PlaatsReview parent;
    RatingBar ratingBar;
    Button btn;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        ratingBar=(RatingBar)findViewById(R.id.rb_review);
        btn=(Button)findViewById(R.id.btn_review);
        editText = (EditText) findViewById(R.id.et_review);
        final PlaatsReview parent = this;
        final Button plaatsreviewbtn;
        plaatsreviewbtn = (Button)findViewById(R.id.btn_review);

        // Set ChangeListener to Rating Bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final double rating = ratingBar.getRating();
                final String reviews = editText.getText().toString();

                new Thread(new Runnable() {
                    public void run() {
                        DatabaseUpdater.addReview(ResultsInfoBedrijven.filteredCompany.getBid(), rating, reviews);
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(parent.getBaseContext(), "Review geplaatst", Toast.LENGTH_LONG).show();
                                parent.finish();
                                plaatsreviewbtn.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
