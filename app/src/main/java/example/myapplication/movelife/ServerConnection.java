package example.myapplication.movelife;

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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.text.TextUtils;


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
		}
		return uid;
	}
	
	public static JSONObject update(ContentValues cv) {
		return update(cv.getAsInteger("cities"),
				cv.getAsInteger("companies"),
				cv.getAsInteger("companytype"),
				cv.getAsInteger("events"),
				cv.getAsInteger("eventsjoined"),
				cv.getAsInteger("favourites"),
				cv.getAsInteger("reviews"),
				cv.getAsInteger("users"));
	}
	
	public static JSONObject update(int cities,int companies,int companytype,int events,int eventsjoined,int favourites,int reviews,int users) {
		JSONObject json = null;
		Map<String,String> params = new TreeMap<String,String>();
		params.put("mode","update");
		params.put("cities",""+cities);
		params.put("companies",""+companies);
		params.put("companytype",""+companytype);
		params.put("events",""+events);
		params.put("eventsjoined",""+eventsjoined);
		params.put("favourites",""+favourites);
		params.put("reviews",""+reviews);
		params.put("users",""+users);
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
}
