package ftn.proj.sportcenters.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SportCenterSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SPORT_CENTER = "sport_center";
    public static final String TABLE_USER = "user";
    public static final String TABLE_SPORT = "sport";
    public static final String TABLE_COMMENT = "comment";
    public static final String TABLE_INVITATION = "invitation";
    public static final String TABLE_RESERVATION = "reservation";
    public static final String TABLE_FAVORITES = "favorites";


    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_WORKING_HOURS = "working_hours";
    public static final String COLUMN_SPORTS = "sports";

    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_POINTS = "points";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_SPORT_CENTER_ID = "sport_center_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_COMMENT_DATE = "comment_date";

    public static final String COLUMN_RESERVATION_ID = "reservation_id";
    public static final String COLUMN_ACCEPTED = "accepted";

    public static final String COLUMN_SPORT_ID = "sport_id";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PERIOD = "period";

    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LONGG = "longg";
    public static final String COLUMN_RATING = "rating";

    public static final String COLUMN_SPORT_CENTER_NAME= "sport_center_name";
    public static final String COLUMN_SPORT_NAME = "sport_name";
    public static final String COLUMN_EVENT_ID = "event_id";

//favorites


    private static final String DATABASE_NAME = "sportcenters.db";
    private static final int DATABASE_VERSION = 4;

    private static final String DB_CREATE_SPORT_CENTER = "create table "
            + TABLE_SPORT_CENTER + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_NAME + " text , "
            + COLUMN_ADDRESS + " text, "
            + COLUMN_IMAGE + " integer, "
            + COLUMN_WORKING_HOURS + " text, "
            + COLUMN_SPORTS + " text, "
            + COLUMN_LAT + " real, "
            + COLUMN_LONGG + " real, "
            + COLUMN_RATING + " real "
            + ")";
    private static final String DB_CREATE_USER = "create table "
            + TABLE_USER + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_FIRSTNAME + " text, "
            + COLUMN_LASTNAME + " text, "
            + COLUMN_USERNAME + " text unique, "
            + COLUMN_PASSWORD + " text, "
            + COLUMN_EMAIL + " text, "
            + COLUMN_CITY + " text, "
            + COLUMN_POINTS + " integer, "
            + COLUMN_SPORTS + " text "
            + ")";
    private static final String DB_CREATE_SPORT = "create table "
            + TABLE_SPORT + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_NAME + " text "
            + ")";
    private static final String DB_CREATE_COMMENT = "create table "
            + TABLE_COMMENT + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_USER_ID + " integer, "
            + COLUMN_SPORT_CENTER_ID + " integer, "
            + COLUMN_TEXT + " text, "
            + COLUMN_COMMENT_DATE + " text "
            + ")";
    private static final String DB_CREATE_INVITATION = "create table "
            + TABLE_INVITATION + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_USER_ID + " integer, "
            + COLUMN_RESERVATION_ID + " integer, "
            + COLUMN_ACCEPTED + " integer "
            + ")";

    private static final String DB_CREATE_RESERVATION = "create table "
            + TABLE_RESERVATION + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_USER_ID + " integer, "
            + COLUMN_SPORT_CENTER_ID + " integer, "
            + COLUMN_SPORT_NAME + " text, "
            + COLUMN_PRICE + " real, "
            + COLUMN_DATE + " text, "
            + COLUMN_PERIOD + " text, "
            + COLUMN_EVENT_ID + " integer "
            + ")";

    private static final String DB_CREATE_FAVORITES = "create table "
            + TABLE_FAVORITES+ "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_USER_ID + " integer, "
            + COLUMN_SPORT_CENTER_ID + " integer "
            + ")";
    public SportCenterSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_SPORT_CENTER);
        db.execSQL(DB_CREATE_USER);
        db.execSQL(DB_CREATE_SPORT);
        db.execSQL(DB_CREATE_COMMENT);
        db.execSQL(DB_CREATE_INVITATION);
        db.execSQL(DB_CREATE_RESERVATION);
        db.execSQL(DB_CREATE_FAVORITES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPORT_CENTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVITATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

        onCreate(db);
    }
}
