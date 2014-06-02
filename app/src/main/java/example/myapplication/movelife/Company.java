package example.myapplication.movelife;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class Company {
	private static List<Company> companies;
	private int bid;
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
	
	public Company(int bid,String name,double latitude,double longitude) {
		this.bid = bid;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Company(int bid,String name,double latitude,double longitude,Integer cid,String postcode,String address,Double rating,Integer type,String telephone,String description,Integer buystate) {
		this(bid,name,latitude,longitude);
		setAll(cid,postcode,address,rating,type,telephone,description,buystate);
	}
	
	private Company(Cursor c) {
		this(
				c.getInt(c.getColumnIndex("bid")),
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
	
	private static void createCompanyList() {
		companies = new ArrayList<Company>();
		Cursor c = LocalDatabaseConnector.get("companies",new String[]{"bid","name","latitude","longitude","cid","postcode","address","rating","tid","telephone","description","buystate"});
		if(c.moveToFirst()) {
			while(!c.isAfterLast()) {
				companies.add(new Company(c));
				c.moveToNext();
			}
		}
	}
	
	public static List<Company> getCompanies() {
		if(companies == null) {
			createCompanyList();
		}
		return companies;
	}

	public int getBid() {
		return bid;
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
