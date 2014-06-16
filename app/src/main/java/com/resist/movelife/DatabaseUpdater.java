package com.resist.movelife;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


public class DatabaseUpdater extends Thread {
	public static final long ONE_SECOND = 1000;
	public static final long ONE_MINUTE = 60*ONE_SECOND;
	public static final long ONE_HOUR = 60*ONE_MINUTE;
	private boolean running = true;
	private long sleep = ONE_SECOND;
	private Context context;
	private ConnectivityManager cm;
	
	public void run() {
		while(running) {
			if(!LocalDatabaseConnector.initialised()) {
				LocalDatabaseConnector.init(context);
				continue;
			} else {
				if(!isConnected()) {
					if(sleep < ONE_MINUTE) {
						sleep = ONE_MINUTE;
					} else if(sleep < ONE_HOUR) {
						sleep += ONE_MINUTE;
					}
				} else {
					sleep = ONE_SECOND;
					if(!ServerConnection.isLoggedIn()) {
						login();
					}
					if(!ServerConnection.isLoggedIn()) {
						sleep = ONE_MINUTE;
					} else {
						update();
					}
				}
			}
			try {
				sleep(sleep);
			} catch (InterruptedException e) {
				running = false;
			}
		}
	}
	
	private void update(JSONObject json) {
		JSONArray companies = null;
		try {
			companies = json.getJSONArray("companies");
		} catch (JSONException e) {}
		if(companies != null && companies.length() > 0) {
			updateCompanies(companies);
		}
	}
	
	private void updateCompanies(JSONArray companies) {
		final String[] ints = {"bid","uid","cid","tid","buystate"};
		final String[] floats = {"latitude","longitude","rating"};
		final String[] strings = {"name","postcode","address","telephone","description"};
		for(int n=0; n < companies.length();n++) {
			JSONObject company = null;
			try {
				company = companies.getJSONObject(n);
			} catch (JSONException e) {}
			if(company != null) {
				ContentValues cv = new ContentValues();
				for(String column : ints) {
					try {
						cv.put(column, company.getInt(column));
					} catch (JSONException e) {}
				}
				for(String column : floats) {
					try {
						cv.put(column, company.getDouble(column));
					} catch (JSONException e) {}
				}
				for(String column : strings) {
					String value = null;
					try {
						value = company.getString(column);
					} catch (JSONException e) {}
					if(value != null) {
						cv.put(column, value);
					}
				}
				LocalDatabaseConnector.insert("companies",cv);
			}
		}
        Company.createCompanyList();
	}
	
	private void update() {
		sleep = ONE_HOUR;
        String[] keys = {"cities","companies","companytype","events","eventsjoined","favourites","reviews","users"};
        ContentValues cv = new ContentValues();
        for(String k : keys) {
            cv.put(k,0);
        }
		Cursor c = LocalDatabaseConnector.get("updatetime",keys);
		if(c.moveToFirst()) {
			for(String k : keys) {
				cv.put(k,c.getInt(c.getColumnIndex(k)));
			}
		}
		JSONArray json_bids = new JSONArray();
		int[] bids = Company.getCompanyIDs();
		for(int bid : bids) {
			json_bids.put(bid);
		}
		cv.put("companies_exclude",json_bids.toString());
		JSONObject json = ServerConnection.update(cv);
        cv.remove("companies_exclude");
		if(json != null) {
            Log.d("MoveLife",json.toString());
			try {
				JSONObject updatetime = json.getJSONObject("updatetime");
				for(String k : keys) {
					cv.put(k,updatetime.getInt(k));
				}
			} catch (JSONException e) {
				Log.d("MoveLife",json.toString());
			}
			update(json);
			if(c.getCount() == 0) {
				LocalDatabaseConnector.insert("updatetime",cv);
			} else {
				LocalDatabaseConnector.update("updatetime",cv);
			}
		}
		c.close();
	}

	private String getEmail() {
		String em = getUserSetEmail();
		if(em != null) {
			return em;
		}
		Pattern emailP = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (emailP.matcher(account.name).matches()) {
		        return account.name;
		    }
		}
		return getPassword()+"@movelife.tk";
	}
	
	private String getPassword() {
		String pwd = getUserSetPassword();
		if(pwd != null) {
			return pwd;
		}
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	public void setUserEmail(String email) {
		if(!hasAccount()) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put("email",email);
		LocalDatabaseConnector.update("user",values);
	}
	
	private void setUserPassword(String password) {
		ContentValues values = new ContentValues();
		values.put("password",password);
		LocalDatabaseConnector.update("user",values);
	}
	
	public void setUserPassword(String newPassword,String oldPassword) {
		if(!hasAccount()) {
			return;
		}
		String pwd = getUserSetPassword();
		if(pwd == null) {
			setUserPassword(newPassword);
		} else if(pwd.equals(oldPassword)) {
			ContentValues values = new ContentValues();
			values.put("password",newPassword);
			LocalDatabaseConnector.update("user",values);
		}
	}
	
	private String getUserSetPassword() {
		Cursor c = LocalDatabaseConnector.get("user","password");
		int i = c.getColumnIndex("password");
		if(!c.moveToFirst() || i == -1 || c.isNull(i)) {
			return null;
		}
		String s = c.getString(i);
		c.close();
		return s;
	}
	
	private String getUserSetEmail() {
		Cursor c = LocalDatabaseConnector.get("user","email");
		int i = c.getColumnIndex("email");
		if(!c.moveToFirst() || i == -1 || c.isNull(i)) {
			return null;
		}
		String s = c.getString(i);
		c.close();
		return s;
	}
	
	private boolean hasAccount() {
		return !LocalDatabaseConnector.isEmpty("user");
	}
	
	private void insertUser(int uid,String email) {
		ContentValues values = new ContentValues();
		values.put("uid",uid);
		values.put("email",email);
		LocalDatabaseConnector.insert("user",values);
	}
	
	private void registerAccount() {
		String email = getEmail();
		int uid = ServerConnection.register(email,getPassword());
		if(uid != 0) {
			insertUser(uid,email);
		}
	}
	
	private void login() {
		if(hasAccount()) {
			ServerConnection.attemptLogin(getEmail(),getPassword());
		} else {
			registerAccount();
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void exit() {
		running = false;
	}
	
	public long getSleep() {
		return sleep;
	}
	
	public void setSleep(long l) {
		sleep = l;
	}
	
	public void start(Context ctx) {
		if(context == null) {
			context = ctx.getApplicationContext();
			cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		super.start();
	}
	
	public boolean isConnected() {
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnected() && ni.isAvailable();
	}
}
