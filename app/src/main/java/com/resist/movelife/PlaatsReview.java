package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class PlaatsReview extends Activity {
	private RatingBar ratingBar;
	private Button btn;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		ratingBar = (RatingBar) findViewById(R.id.rb_review);
		btn = (Button) findViewById(R.id.btn_review);
		editText = (EditText) findViewById(R.id.et_review);
		final PlaatsReview parent = this;
		// Set ChangeListener to Rating Bar
		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
			}
		});
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				final double rating = ratingBar.getRating();
				final String reviews = editText.getText().toString();
				new Thread(new Runnable() {
					public void run() {
						Menu.getUpdater().addReview(ResultsInfoBedrijven.filteredCompany.getBid(), rating, reviews);
						parent.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(parent.getBaseContext(), parent.getResources().getString(R.string.review_made), Toast.LENGTH_LONG).show();
								parent.finish();
								btn.setVisibility(View.GONE);
							}
						});
					}
				}).start();
			}
		});
	}
}