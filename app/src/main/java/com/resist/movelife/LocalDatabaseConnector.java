package com.resist.movelife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabaseConnector {
    private static final String DATABASE_NAME = "MoveLife";
    private static final int DATABASE_VERSION = 3;
    private static Context context;
    private static DBHelper helper;
    private static SQLiteDatabase database;
    private static boolean changed = false;

    public static void init(Context ctx) {
        context = ctx.getApplicationContext();
        helper = new DBHelper();
        database = helper.getWritableDatabase();
        resetChanged();
    }

    public static void close() {
        helper.close();
        context = null;
    }

    public static boolean hasChanged() {
        return changed;
    }

    public static void resetChanged() {
        changed = false;
    }

    public static void restart() {
        Context c = context;
        close();
        init(c);
    }

    public static boolean initialised() {
        return context != null;
    }

    public static long insert(String table,String nullColumnHack,ContentValues values) {
        changed = true;
        return database.insert(table,nullColumnHack,values);
    }

    public static long insert(String table,ContentValues values) {
        changed = true;
        return database.insert(table,null,values);
    }

    public static int update(String table,ContentValues values,String whereClause,String[] whereArgs) {
        changed = true;
        return database.update(table,values,whereClause,whereArgs);
    }

    public static int update(String table,ContentValues values) {
        changed = true;
        return database.update(table,values,null,null);
    }

    public static int delete(String table,String whereClause,String[] whereArgs) {
        changed = true;
        return database.delete(table,whereClause,whereArgs);
    }

    public static boolean isEmpty(String table) {
        Cursor c = database.rawQuery("SELECT 1 FROM "+table,null);
        boolean b = !c.moveToFirst();
        c.close();
        return b;
    }

    public static Cursor get(String table,String[] columns,String selection,String[] selectionArgs) {
        return database.query(table,columns,selection,selectionArgs,null,null,null);
    }

    public static Cursor get(String table,String column,String selection,String[] selectionArgs) {
        return get(table,new String[]{column},selection,selectionArgs);
    }

    public static Cursor get(String table,String[] columns) {
        return get(table,columns,null,null);
    }

    public static Cursor get(String table,String column) {
        return get(table,new String[]{column});
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper() {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE user (uid INTEGER, email VARCHAR(255) NOT NULL, password TEXT, PRIMARY KEY(uid));");
            db.execSQL("CREATE TABLE companies (bid INTEGER, uid INTEGER NOT NULL, name VARCHAR(255) NOT NULL, latitude FLOAT NOT NULL, longitude FLOAT NOT NULL, cid INTEGER, postcode VARCHAR(20), address VARCHAR(255), rating FLOAT, tid INTEGER, telephone VARCHAR(20), description TEXT, buystate INTEGER, PRIMARY KEY(bid));");
            db.execSQL("CREATE TABLE reviews (uid INTEGER NOT NULL, bid INTEGER NOT NULL, review TEXT, rating FLOAT, PRIMARY KEY(uid,bid));");
            db.execSQL("CREATE TABLE favourites (bid INTEGER NOT NULL, uid INTEGER NOT NULL, PRIMARY KEY(bid,uid));");
            db.execSQL("CREATE TABLE companytype (tid INTEGER NOT NULL, companytype VARCHAR(255) NOT NULL, PRIMARY KEY(tid));");
            db.execSQL("CREATE TABLE events (eid INTEGER NOT NULL, name VARCHAR(255) NOT NULL, description TEXT, startdate DATETIME NOT NULL, enddate DATETIME NOT NULL, bid INTEGER NOT NULL, uid INTEGER NOT NULL, PRIMARY KEY(eid));");
            db.execSQL("CREATE TABLE eventsjoined (uid INTEGER NOT NULL, eid INTEGER NOT NULL, PRIMARY KEY(uid,eid));");
            db.execSQL("CREATE TABLE cities (cid INTEGER NOT NULL, country VARCHAR(3), name VARCHAR(255), PRIMARY KEY(cid));");
            db.execSQL("CREATE TABLE updatetime (cities INTEGER NOT NULL, companies INTEGER NOT NULL, companytype INTEGER NOT NULL, events INTEGER NOT NULL, eventsjoined INTEGER NOT NULL, favourites INTEGER NOT NULL, reviews INTEGER NOT NULL, users INTEGER NOT NULL);");
            db.execSQL("CREATE TABLE friendlocations (uid INTEGER NOT NULL, latitude FLOAT NOT NULL, longitude FLOAT NOT NULL, changed INTEGER NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS user;");
            db.execSQL("DROP TABLE IF EXISTS companies;");
            db.execSQL("DROP TABLE IF EXISTS reviews;");
            db.execSQL("DROP TABLE IF EXISTS favourites;");
            db.execSQL("DROP TABLE IF EXISTS companytype;");
            db.execSQL("DROP TABLE IF EXISTS events;");
            db.execSQL("DROP TABLE IF EXISTS eventsjoined;");
            db.execSQL("DROP TABLE IF EXISTS cities;");
            db.execSQL("DROP TABLE IF EXISTS updatetime;");
            db.execSQL("DROP TABLE IF EXISTS friendlocations;");
            onCreate(db);

        }

    }
}