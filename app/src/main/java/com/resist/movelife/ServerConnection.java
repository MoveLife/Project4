package com.resist.movelife;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ServerConnection {
	private static final String apiKey = "7m6LYrI6XORcxA2DJqmjusSgeHNL6ZpcvcaFuGQUBUqydulMrqEcTCK5jIyJaFLAXxokWfOkCXApS697HJjFP6EcLFUfqHI5tDyH2b0aspZEW2v9QEOIU8HWHLpnmqWuMeDb5SwA7NCfJZ1ooYFLNz7XwQgLORWqjEgLrjdcJ3HQ2SK9wGjSn57k8BqqhaszKzyLPWwqE7sXfseDlzHN1GdhyLQo4N4eXfihvVRqN6qvePN6ww3TUCXyWTSPGZY0";
	private static final String registerKey = "uH7An8sTT0FDYmsJsZ2mT2aR7EQUjXhHPeXTmcZrHbxoTbZwSBKSix1w7iK83oSwIfq8xFDhSZptKyOnTbn49BmqwBgpImXTFhWNWEd8EDtOmiloxAQuwNIWrnrEUSRv";
	private static final String path = "http://145.24.222.140/api/";
	private static final int readTimeout = 10000;
	private static final int connectTimeout = 1500;
	private static final CookieManager cookieManager = new CookieManager();
	private static URI uri;
	private static URL url;
	private static boolean loggedIn = false;
	private static String returnValue = null;
	
	public static boolean isLoggedIn() {
		return loggedIn;
	}
	
	private static void init() {
		CookieHandler.setDefault(cookieManager);
		try {
			url = new URL(path);
			uri = new URI(path);
		} catch (Exception e) {
		}
	}
	
	private static void handleCookies(HttpURLConnection connection) {
		List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
		if(cookies != null) {
			for(String cookie : cookies) {
				cookieManager.getCookieStore().add(uri,HttpCookie.parse(cookie).get(0));
			}
		}
	}
	
	private static void handleReturn(HttpURLConnection connection) throws IOException {
		returnValue = readStream(new BufferedInputStream(connection.getInputStream()));
	}
	
	public static String readStream(InputStream input) throws IOException {
		StringBuilder str = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(input),1000);
		String chars;
		while((chars = br.readLine()) != null) {
			str.append(chars);
		}
		input.close();
		return str.toString();
	}
	
	private static HttpURLConnection post(Map<String,String> params) throws IOException {
		if(url == null) {
			init();
		}
		returnValue = null;
		params.put("apikey",apiKey);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setReadTimeout(readTimeout);
		connection.setConnectTimeout(connectTimeout);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Cookie",TextUtils.join("; ",cookieManager.getCookieStore().getCookies()));
		connection.setDoInput(true);
		connection.setDoOutput(true);
		OutputStream os = connection.getOutputStream();
		BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
		bf.write(buildParamString(params));
		bf.flush();
		bf.close();
		os.close();
		connection.connect();
		if(connection.getResponseCode() == 403) {
			loggedIn = false;
		}
		handleCookies(connection);
		handleReturn(connection);
		return connection;
	}
	
	public static String buildParamString(Map<String,String> params) throws UnsupportedEncodingException {
		StringBuilder str = new StringBuilder();
		boolean f = true;
		for(Map.Entry<String,String> entry : params.entrySet()) {
			if(f) {
				f = false;
			} else {
				str.append("&");
			}
			str.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
			str.append("=");
			str.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
		}
		return str.toString();
	}
	
	private static boolean login(String email,String password) {
		HttpURLConnection connection;
		Map<String,String> params = new TreeMap<String,String>();
		params.put("login_email",email);
		params.put("login_password",password);
		try {
			connection = post(params);
			return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void attemptLogin(String email,String password) {
		loggedIn = login(email,password);
	}
	
	public static void logout() {
		Map<String,String> params = new TreeMap<String,String>();
		params.put("login_logout","logout");
		try {
			post(params);
		} catch(IOException e) {
		}
	}
	
	public static int register(String email,String password) {
		int uid = 0;
		Map<String,String> params = new TreeMap<String,String>();
		params.put("register",registerKey);
		params.put("register_email",email);
		params.put("register_password",password);
		try {
			post(params);
		} catch (IOException e) {
			return uid;
		}
		if(returnValue != null) {
			try {
				uid = Integer.parseInt(returnValue);
			} catch(NumberFormatException e) {
			}
            if(uid != 0) {
                loggedIn = true;
            }
		}
		return uid;
	}
	
	public static JSONObject update(ContentValues cv) {
		JSONObject json = null;
		Map<String,String> params = new TreeMap<String,String>();
		for(String key : cv.keySet()) {
			params.put(key,cv.getAsString(key));
		}
		params.put("mode","update");
		try {
			post(params);
		} catch (IOException e) {
			return json;
		}
		if(returnValue != null) {
			try {
				json = new JSONObject(returnValue);
			} catch (JSONException e) {
			}
		}
		return json;
	}

    public static JSONObject addReview(int bid,Double rating,String review) {
        if(rating == null && review == null) {
            return null;
        }
        JSONObject json = null;
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","add_review");
        params.put("bid",""+bid);
        if(rating != null) {
            params.put("rating", rating.toString());
        }
        if(review != null && review.length() > 0) {
            params.put("review",review);
        }
        try {
            post(params);
        } catch(IOException e) {
            return json;
        }
        if(returnValue != null) {
            try {
                json = new JSONObject(returnValue);
            } catch(JSONException e) {}
        }
        return json;
    }

    private static JSONArray updateLocation(double longitude,double latitude,int time) {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("longitude",""+longitude);
        params.put("latitude",""+latitude);
        params.put("mode","update_location");
        if(time > 0) {
            params.put("time",""+time);
        }
        try {
            post(params);
        } catch(IOException e) {}
        JSONArray json = null;
        if(returnValue != null) {
            try {
                json = new JSONObject(returnValue).getJSONArray("friend_locations");
            } catch(JSONException e) {}
        }
        return json;
    }

    public static JSONArray updateLocation(double longitude,double latitude) {
        Cursor c = LocalDatabaseConnector.get("updatetime","users");
        int time = 0;
        if(c.moveToFirst()) {
            time = c.getInt(0);
        }
        return updateLocation(longitude,latitude,time);
    }

    public static JSONArray getFriendRequests() {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","get_pending_friends");
        try {
            post(params);
        } catch(IOException e) {}
        JSONArray json = null;
        if(returnValue != null) {
            try {
                json = new JSONObject(returnValue).getJSONArray("friend_requests");
            } catch (JSONException e) {
            }
        }
        return json;
    }

    public static void acceptFriendRequest(int uid) {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","accept_friend");
        params.put("friend",""+uid);
        try {
            post(params);
        } catch(IOException e) {}
    }

    public static void removeFriend(int uid) {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","remove_friend");
        params.put("friend",""+uid);
        try {
            post(params);
        } catch(IOException e) {}
    }

    public static boolean addFriend(String email) {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","add_friend");
        params.put("friend",email);
        try {
            post(params);
        } catch(IOException e) {
            return false;
        }
        if(returnValue != null && returnValue.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean changePassword(String newPassword,String oldPassword) {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","change_password");
        params.put("new_password",newPassword);
        params.put("old_password",oldPassword);
        try {
            post(params);
        } catch(IOException e) {
            return false;
        }
        if(returnValue != null && returnValue.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean changeEmail(String email) {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","change_email");
        params.put("email",email);
        try {
            post(params);
        } catch(IOException e) {
            return false;
        }
        if(returnValue != null && returnValue.equals("")) {
            return true;
        }
        return false;
    }

    public static JSONObject addEvent(String name,int bid,Date startdate,Date enddate,String description) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","add_event");
        params.put("name",name);
        params.put("bid",""+bid);
        params.put("startdate",df.format(startdate));
        params.put("enddate",df.format(enddate));
        if(description != null && !description.isEmpty()) {
            params.put("description",description);
        }
        JSONObject json = null;
        try {
            post(params);
        } catch(IOException e) {}
        if(returnValue != null) {
            try {
                json = new JSONObject(returnValue);
            } catch (JSONException e) {}
        }
        return json;
    }

    public static void setFacebook(String fid,String name) {
        Map<String,String> params = new TreeMap<String,String>();
        params.put("mode","set_facebook");
        params.put("fid",fid);
        params.put("name",name);
        try {
            post(params);
        } catch(IOException e) {}
    }
}
