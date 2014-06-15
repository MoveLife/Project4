package com.resist.movelife;

import android.database.Cursor;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Company {
	private static List<Company> companies;
    private static SparseArray<List<Company>> companiesOfType = new SparseArray<List<Company>>();
	private int bid;
	private int uid;
	private String name;
	private double latitude;
	private double longitude;
	private Integer cid;
	private String postcode;
	private String address;
	private Double rating;
	private Integer type;
	private String telephone;
	private String description;
	private Integer buystate;



    private void setAll(Integer cid,String postcode,String address,Double rating,Integer type,String telephone,String description,Integer buystate) {
		this.cid = cid;
		this.postcode = postcode;
		this.address = address;
		this.rating = rating;
		this.type = type;
		this.telephone = telephone;
		this.description = description;
		this.buystate = buystate;
	}
	
	public Company(int bid,int uid,String name,double latitude,double longitude) {
		this.bid = bid;
		this.uid = uid;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Company(int bid,int uid,String name,double latitude,double longitude,Integer cid,String postcode,String address,Double rating,Integer type,String telephone,String description,Integer buystate) {
		this(bid,uid,name,latitude,longitude);
		setAll(cid,postcode,address,rating,type,telephone,description,buystate);
	}
	
	private Company(Cursor c) {
		this(
				c.getInt(c.getColumnIndex("bid")),
				c.getInt(c.getColumnIndex("uid")),
				c.getString(c.getColumnIndex("name")),
				c.getDouble(c.getColumnIndex("latitude")),
				c.getDouble(c.getColumnIndex("longitude"))
			);
		Integer cid = null;
		String postcode = null;
		String address = null;
		Double rating = null;
		Integer type = null;
		String telephone = null;
		String description = null;
		Integer buystate = null;
		try {
			cid = c.getInt(c.getColumnIndex("cid"));
		} catch(Exception e) {}
		try {
			postcode = c.getString(c.getColumnIndex("postcode"));
		} catch(Exception e) {}
		try {
			address = c.getString(c.getColumnIndex("address"));
		} catch(Exception e) {}
		try {
			rating = c.getDouble(c.getColumnIndex("rating"));
		} catch(Exception e) {}
		try {
			type = c.getInt(c.getColumnIndex("tid"));
		} catch(Exception e) {}
		try {
			telephone = c.getString(c.getColumnIndex("telephone"));
		} catch(Exception e) {}
		try {
			description = c.getString(c.getColumnIndex("description"));
		} catch(Exception e) {}
		try {
			buystate = c.getInt(c.getColumnIndex("buystate"));
		} catch(Exception e) {}
		setAll(cid,postcode,address,rating,type,telephone,description,buystate);
	}
	
	public static void createCompanyList() {
		companies = new ArrayList<Company>();
		Cursor c = LocalDatabaseConnector.get("companies",new String[]{"bid","uid","name","latitude","longitude","cid","postcode","address","rating","tid","telephone","description","buystate"});
		if(c.moveToFirst()) {
			while(!c.isAfterLast()) {
				companies.add(new Company(c));
				c.moveToNext();
			}
		}
		c.close();
	}
	
	public static List<Company> getCompanies() {
		if(companies == null) {
			createCompanyList();
		}
		return companies;
	}


	public static List<Company> getCompaniesOfType(Integer type) {
		if(companiesOfType.indexOfKey(type) < 0) {
			List<Company> allCompanies = getCompanies();
			List<Company> filtered = new ArrayList<Company>();
			for(Company c : allCompanies) {
				if(type.equals(c.getType())) {
					filtered.add(c);
				}
			}
			companiesOfType.put(type,filtered);
		}
		return companiesOfType.get(type);
	}

	public static int[] getCompanyIDs() {
		Cursor c = LocalDatabaseConnector.get("companies","bid");
		int[] out = new int[c.getCount()];
		if(c.moveToFirst()) {
			int n = 0;
			while(!c.isAfterLast()) {
				out[n] = c.getInt(c.getColumnIndex("bid"));
				c.moveToNext();
				n++;
			}
		}
		c.close();
		return out;
	}
	

	public int getBid() {
		return bid;
	}
	
	public int getUid() {
		return uid;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getName() {
		return name;
	}

	public Integer getCid() {
		return cid;
	}

	public String getAddress() {
		return address;
	}

	public String getPostcode() {
		return postcode;
	}

	public Double getRating() {
		return rating;
	}

	public Integer getType() {
		return type;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getDescription() {
		return description;
	}

	public Integer getBuystate() {
		return buystate;
	}
	
	public boolean equals(Object that) {
		if(that instanceof Company) {
			return this.bid == ((Company)that).bid;
		}
		return false;
	}
}
