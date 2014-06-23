package com.resist.movelife;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DatabaseUpdater extends Thread {
	public static final long ONE_SECOND = 1000;
	public static final long ONE_MINUTE = 60*ONE_SECOND;
	public static final long ONE_HOUR = 60*ONE_MINUTE;
	private boolean running = true;
	private long sleep = ONE_SECOND;
    private long update_sleep = 0;
    private long gps_sleep = 0;
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
                        if(gps_sleep <= 0) {
                            updateLocation();
                        } else {
                            gps_sleep -= sleep;
                        }
                        if(update_sleep <= 0) {
                            update();
                        } else {
                            update_sleep -= sleep;
                        }
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

    private void insertEventsJoined(JSONArray joined,int eid) {
        for(int n=0;n < joined.length();n++) {
            ContentValues cv = new ContentValues();
            try {
                cv.put("uid",joined.getInt(n));
            } catch(JSONException e) {
                continue;
            }
            cv.put("eid",eid);
            LocalDatabaseConnector.insert("eventsjoined",cv);
        }
    }

    private void updateEventsJoined(JSONArray joined,int eid) {
        LocalDatabaseConnector.delete("eventsjoined","eid = ?",new String[] {""+eid});
        if(joined.length() != 0) {
            insertEventsJoined(joined,eid);
        }
    }

    private ContentValues getEventCV(JSONObject event) {
        final String[] ints = {"eid","uid"};
        final String[] strings = {"name","description","startdate","enddate"};
        ContentValues cv = new ContentValues();
        for(String column : ints) {
            try {
                cv.put(column, event.getInt(column));
            } catch (JSONException e) {}
        }
        for(String column : strings) {
            try {
                cv.put(column, event.getString(column));
            } catch (JSONException e) {}
        }
        return cv;
    }

    private void insertEvents(JSONArray events) {
        for(int n=0;n < events.length();n++) {
            JSONObject event = null;
            try {
                event = events.getJSONObject(n);
                insertEventsJoined(event.getJSONArray("joined"),event.getInt("eid"));
            } catch (JSONException e) {}
            if(event != null) {
                LocalDatabaseConnector.insert("events",getEventCV(event));
            }
        }
    }

    private void deleteEvents(JSONArray events) {
        String[] eids = new String[events.length()];
        StringBuilder where = new StringBuilder();
        where.append("eid IN(");
        for(int n=0;n < events.length();n++) {
            try {
                eids[n] = events.getString(n);
            } catch (JSONException e) {
                continue;
            }
            if(n != 0) {
                where.append(",");
            }
            where.append("?");
        }
        where.append(")");
        LocalDatabaseConnector.delete("events", where.toString(), eids);
        LocalDatabaseConnector.delete("eventsjoined",where.toString(),eids);
    }

    private void updateEvents(JSONArray events) {
        for(int n=0;n < events.length();n++) {
            JSONObject event = null;
            try {
                event = events.getJSONObject(n);
                updateEventsJoined(event.getJSONArray("joined"),event.getInt("eid"));
            } catch (JSONException e) {}
            if(event != null) {
                ContentValues cv = getEventCV(event);
                LocalDatabaseConnector.update("events", cv, "eid = ?", new String[]{cv.getAsString("eid")});
            }
        }
    }

    private void updateFriendLocations(JSONArray locations) {
        Cursor c = LocalDatabaseConnector.get("friendlocations","uid");
        List<Integer> uids = new ArrayList<Integer>();
        if(c.moveToFirst()) {
            while(!c.isAfterLast()) {
                uids.add(c.getInt(0));
                c.moveToNext();
            }
        }
        int time = 0;
        for(int n=0;n < locations.length();n++) {
            JSONObject location = null;
            try {
                location = locations.getJSONObject(n);
            } catch(JSONException e) {}
            if(location != null) {
                ContentValues cv = new ContentValues();
                try {
                    cv.put("uid",location.getInt("uid"));
                    cv.put("changed",location.getInt("changed"));
                    cv.put("latitude",location.getInt("latitude"));
                    cv.put("longitude",location.getInt("longitude"));
                } catch(JSONException e) {
                    continue;
                }
                time = Math.max(time,cv.getAsInteger("changed"));
                if(uids.contains(cv.getAsInteger("uid"))) {
                    LocalDatabaseConnector.update("friendlocations",cv,"uid = ?",new String[] {cv.getAsString("uid")});
                } else {
                    LocalDatabaseConnector.insert("friendlocations",cv);
                }
            }
        }
        if(time > 0) {
            ContentValues cv = new ContentValues();
            cv.put("users",time);
            LocalDatabaseConnector.update("updatetime",cv,"users < ?",new String[] {""+time});
        }
    }

    private void updateLocation() {
        gps_sleep += ONE_MINUTE*5;
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(l != null) {
            JSONArray json = ServerConnection.updateLocation(l.getLongitude(), l.getLatitude());
            if(json != null) {
                updateFriendLocations(json);
            }
        } else {
            gps_sleep += ONE_HOUR;
        }
    }

    private ContentValues getCompanyCV(JSONObject company) {
        final String[] ints = {"bid","uid","cid","tid","buystate"};
        final String[] floats = {"latitude","longitude","rating"};
        final String[] strings = {"name","postcode","address","telephone","description"};
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
        return cv;
    }

	private void insertCompanies(JSONArray companies) {
		for(int n=0;n < companies.length();n++) {
			JSONObject company = null;
			try {
				company = companies.getJSONObject(n);
			} catch (JSONException e) {}
			if(company != null) {
				LocalDatabaseConnector.insert("companies",getCompanyCV(company));
			}
		}
	}

    private void deleteCompanies(JSONArray companies) {
        String[] bids = new String[companies.length()];
        StringBuilder where = new StringBuilder();
        where.append("bid IN(");
        for(int n=0;n < companies.length();n++) {
            try {
              bids[n] = companies.getString(n);
            } catch (JSONException e) {
                continue;
            }
            if(n != 0) {
                where.append(",");
            }
            where.append("?");
        }
        where.append(")");
        LocalDatabaseConnector.delete("companies",where.toString(),bids);
    }

    private void updateCompanies(JSONArray companies) {
        for(int n=0;n < companies.length();n++) {
            JSONObject company = null;
            try {
                company = companies.getJSONObject(n);
            } catch (JSONException e) {}
            if(company != null) {
                ContentValues cv = getCompanyCV(company);
                LocalDatabaseConnector.update("companies",cv,"bid = ?",new String[] {cv.getAsString("bid")});
            }
        }
    }

    private void update(JSONObject json) {
        boolean rebuildCompanies = false;
        JSONArray companies = null;
        JSONArray companies_delete = null;
        JSONArray companies_update = null;
        JSONArray friend_locations = null;
        JSONArray events = null;
        JSONArray events_delete = null;
        JSONArray events_update = null;
        try {
            companies = json.getJSONArray("companies");
        } catch (JSONException e) {}
        try {
            companies_delete = json.getJSONArray("deleted_companies");
        } catch (JSONException e) {}
        try {
            companies_update = json.getJSONArray("updated_companies");
        } catch (JSONException e) {}
        try {
            friend_locations = json.getJSONArray("friend_locations");
        } catch (JSONException e) {}
        try {
            events = json.getJSONArray("events");
        } catch (JSONException e) {}
        try {
            events_delete = json.getJSONArray("deleted_events");
        } catch (JSONException e) {}
        try {
            events_update = json.getJSONArray("updated_events");
        } catch (JSONException e) {}
        if(companies != null && companies.length() > 0) {
            insertCompanies(companies);
            rebuildCompanies = true;
        }
        if(companies_delete != null && companies_delete.length() > 0) {
            deleteCompanies(companies_delete);
            rebuildCompanies = true;
        }
        if(companies_update != null && companies_update.length() > 0) {
            updateCompanies(companies_update);
            rebuildCompanies = true;
        }
        if(friend_locations != null && friend_locations.length() > 0) {
            updateFriendLocations(friend_locations);
        }
        if(events != null && events.length() > 0) {
            insertEvents(events);
        }
        if(events_delete != null && events_delete.length() > 0) {
            deleteEvents(events_delete);
        }
        if(events_update != null && events_update.length() > 0) {
            updateEvents(events_update);
        }
        if(rebuildCompanies) {
            update_sleep += ONE_HOUR;
            Company.createCompanyList();
        }
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

    public static void addReview(int bid,Double rating,String review) {
        JSONObject json = ServerConnection.addReview(bid,rating,review);
        if(json != null) {
            ContentValues cv = new ContentValues();
            ContentValues cv2 = new ContentValues();
            ContentValues cv3 = new ContentValues();
            try {
                cv.put("uid",json.getInt("uid"));
                cv.put("bid",json.getInt("bid"));
                cv2.put("rating",json.getDouble("company_rating"));
                cv3.put("companies",json.getDouble("time"));
                cv3.put("reviews",json.getDouble("time"));
            } catch(JSONException e) {
                return;
            }
            try {
                cv.put("review",json.getString("review"));
            } catch(JSONException e) {}
            try {
                cv.put("rating",json.getDouble("rating"));
            } catch(JSONException e) {}
            LocalDatabaseConnector.insert("reviews",cv);
            LocalDatabaseConnector.update("companies",cv2,"bid = ?",new String[] {cv.getAsString("bid")});
            LocalDatabaseConnector.update("updatetime",cv3);
        }
    }
}