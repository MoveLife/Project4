package com.resist.movelife;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 25-6-2014.
 */
public class Review {
	private int uid;
	private Integer bid;
	private String review;
	private Float rating;
	private static List<Review> reviews;

	public Review(int uid, Integer bid, String review, Float rating) {
		this.uid = uid;
		this.bid = bid;
		this.review = review;
		this.rating = rating;
	}

	private Review(Cursor c) {
		this(c.getInt(c.getColumnIndex("uid")), c.getInt(c.getColumnIndex("bid")), c.getString(c.getColumnIndex("review")), c.getFloat(c.getColumnIndex("rating")));
	}

	public static void createReviewList() {
		if(LocalDatabaseConnector.hasChanged()) {
			LocalDatabaseConnector.restart();
		}
		reviews = new ArrayList<Review>();
		Cursor c = LocalDatabaseConnector.get("reviews", new String[]{"uid", "bid", "review", "rating"});
		if(c.moveToFirst()) {
			while(!c.isAfterLast()) {
				reviews.add(new Review(c));
				c.moveToNext();
			}
		}
		c.close();
	}

	public static List<Review> getReviews() {
		if(reviews == null) {
			createReviewList();
		}
		return reviews;
	}

	public static List<Review> getReviewByCompanyID(int bid) {
		List<Review> output = new ArrayList<Review>();
		Cursor c = LocalDatabaseConnector.get("reviews", new String[]{"uid", "bid", "review", "rating"}, "bid = ?", new String[]{"" + bid});
		if(c.moveToFirst()) {
			while(!c.isAfterLast()) {
				output.add(new Review(c));
				c.moveToNext();
			}
		}
		c.close();
		return output;
	}

	public int getUid() {
		return uid;
	}

	public Integer getBid() {
		return bid;
	}

	public String getReview() {
		return review;
	}

	public Float getRating() {
		return rating;
	}
}