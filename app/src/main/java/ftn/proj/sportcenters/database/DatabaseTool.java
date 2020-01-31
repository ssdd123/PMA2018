package ftn.proj.sportcenters.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ftn.proj.sportcenters.R;

public class DatabaseTool {

    //Pozivamo kad god imamo neki niz da cuvamo u bazi npr working hours za sportske
    // centre da pretvorimo niz stringova u string jedan

    public static String arrayToString(List<?> array, String jsonArrayName){
        JSONObject json = new JSONObject();
        try {
            json.put(jsonArrayName, new JSONArray(array));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    //ovde gurati sve podatke inicijalne

    public static void initDB(Activity activity){

        ArrayList<String> workingHours = new ArrayList<String>();
        workingHours.add("Mon 08:00-22:00");
        workingHours.add("Tue 08:00-22:00");
        workingHours.add("Wed 08:00-22:00");
        workingHours.add("Thu 08:00-22:00");
        workingHours.add("Fri 08:00-22:00");

        String workingHoursString = arrayToString(workingHours,
                "workingHours");


        ArrayList<String> workingHours2 = new ArrayList<String>();
        workingHours2.add("Mon 15:00-20:00");
        workingHours2.add("Tue 15:00-20:00");
        workingHours2.add("Wed 15:00-20:00");
        workingHours2.add("Thu 15:00-20:00");
        workingHours2.add("Fri 15:00-20:00");

        String workingHoursString2 = arrayToString(workingHours2,
                "workingHours");

        ArrayList<String> workingHours3 = new ArrayList<String>();
        workingHours3.add("Mon 10:00-16:00");
        workingHours3.add("Tue 15:00-20:00");
        workingHours3.add("Wed 08:00-22:00");
        workingHours3.add("Thu 15:00-20:00");
        workingHours3.add("Fri 08:00-22:00");

        String workingHoursString3 = arrayToString(workingHours3,
                "workingHours");

        ArrayList<String> workingHours4 = new ArrayList<String>();
        workingHours4.add("Sun 08:00-22:00");
        workingHours4.add("Sat 08:00-22:00");

        String workingHoursString4 = arrayToString(workingHours4,
                "workingHours");



        ContentValues entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Football");
        Uri uri_sport1 = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT, entry);

        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Basketball");
        Uri uri_sport2 = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT, entry);

        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Volleyball");
        Uri uri_sport3 = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT, entry);

        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Tennis");
        Uri uri_sport4 = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT, entry);

        String[] sportIdColumn = { SportCenterSQLiteHelper.COLUMN_ID };
        Cursor cursor1 = activity.getContentResolver().query(Uri.parse(DBContentProvider.CONTENT_URI_BASE.toString() + uri_sport1.toString()), sportIdColumn, null, null,
                null);
        Cursor cursor2 = activity.getContentResolver().query(Uri.parse(DBContentProvider.CONTENT_URI_BASE.toString() + uri_sport2.toString()), sportIdColumn, null, null,
                null);
        Cursor cursor3 = activity.getContentResolver().query(Uri.parse(DBContentProvider.CONTENT_URI_BASE.toString() + uri_sport3.toString()), sportIdColumn, null, null,
                null);
        Cursor cursor4 = activity.getContentResolver().query(Uri.parse(DBContentProvider.CONTENT_URI_BASE.toString() + uri_sport4.toString()), sportIdColumn, null, null,
                null);
        cursor1.moveToFirst();
        cursor2.moveToFirst();
        cursor3.moveToFirst();
        cursor4.moveToFirst();
        ArrayList<Long> sports = new ArrayList<Long>();
        sports.add(cursor1.getLong(0));
        sports.add(cursor2.getLong(0));
        String sportsString = arrayToString(sports,
                "sports");
        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Albatros");
        entry.put(SportCenterSQLiteHelper.COLUMN_ADDRESS, "Novi Sad");
        entry.put(SportCenterSQLiteHelper.COLUMN_IMAGE, R.mipmap.ic_albatros);
        entry.put(SportCenterSQLiteHelper.COLUMN_WORKING_HOURS, workingHoursString);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, sportsString);
        entry.put(SportCenterSQLiteHelper.COLUMN_LAT, 45.253240);
        entry.put(SportCenterSQLiteHelper.COLUMN_LONGG, 19.764570);
        entry.put(SportCenterSQLiteHelper.COLUMN_RATING, 1.0);

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT_CENTER, entry);


        sports = new ArrayList<Long>();
        sports.add(cursor1.getLong(0));
        sports.add(cursor3.getLong(0));
        sports.add(cursor4.getLong(0));
        sportsString = arrayToString(sports,
                "sports");
        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Meridiana");
        entry.put(SportCenterSQLiteHelper.COLUMN_ADDRESS, "Novi Sad");
        entry.put(SportCenterSQLiteHelper.COLUMN_IMAGE, R.mipmap.ic_meridiana);
        entry.put(SportCenterSQLiteHelper.COLUMN_WORKING_HOURS, workingHoursString2);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, sportsString);
        entry.put(SportCenterSQLiteHelper.COLUMN_LAT, 45.254550);
        entry.put(SportCenterSQLiteHelper.COLUMN_LONGG, 19.842580);
        entry.put(SportCenterSQLiteHelper.COLUMN_RATING, 2.0);

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT_CENTER, entry);


        sports = new ArrayList<Long>();
        sports.add(cursor1.getLong(0));
        sports.add(cursor3.getLong(0));
        sports.add(cursor4.getLong(0));
        sportsString = arrayToString(sports,
                "sports");
        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "DUGA");
        entry.put(SportCenterSQLiteHelper.COLUMN_ADDRESS, "Beograd");
        entry.put(SportCenterSQLiteHelper.COLUMN_IMAGE, R.mipmap.ic_duga);
        entry.put(SportCenterSQLiteHelper.COLUMN_WORKING_HOURS, workingHoursString3);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, sportsString);
        entry.put(SportCenterSQLiteHelper.COLUMN_LAT, 44.815070);
        entry.put(SportCenterSQLiteHelper.COLUMN_LONGG, 20.460480);
        entry.put(SportCenterSQLiteHelper.COLUMN_RATING, 3.0);


        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT_CENTER, entry);


        sports = new ArrayList<Long>();
        sports.add(cursor2.getLong(0));
        sportsString = arrayToString(sports,
                "sports");
        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "As");
        entry.put(SportCenterSQLiteHelper.COLUMN_ADDRESS, "Novi Sad");
        entry.put(SportCenterSQLiteHelper.COLUMN_IMAGE, R.mipmap.ic_as);
        entry.put(SportCenterSQLiteHelper.COLUMN_WORKING_HOURS, workingHoursString4);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, sportsString);
        entry.put(SportCenterSQLiteHelper.COLUMN_LAT, 45.230454);
        entry.put(SportCenterSQLiteHelper.COLUMN_LONGG, 19.764570);
        entry.put(SportCenterSQLiteHelper.COLUMN_RATING, 4.0);

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT_CENTER, entry);


        sports = new ArrayList<Long>();
        sports.add(cursor1.getLong(0));
        sports.add(cursor2.getLong(0));
        sports.add(cursor3.getLong(0));
        sportsString = arrayToString(sports,
                "sports");
        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Hattrick");
        entry.put(SportCenterSQLiteHelper.COLUMN_ADDRESS, "Niš");
        entry.put(SportCenterSQLiteHelper.COLUMN_IMAGE, R.mipmap.ic_hattrick);
        entry.put(SportCenterSQLiteHelper.COLUMN_WORKING_HOURS, workingHoursString);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, sportsString);

        entry.put(SportCenterSQLiteHelper.COLUMN_LAT, 43.316872);
        entry.put(SportCenterSQLiteHelper.COLUMN_LONGG, 21.894501);
        entry.put(SportCenterSQLiteHelper.COLUMN_RATING, 5.0);

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT_CENTER, entry);


        sports = new ArrayList<Long>();
        sports.add(cursor2.getLong(0));
        sports.add(cursor3.getLong(0));
        sports.add(cursor4.getLong(0));
        sportsString = arrayToString(sports,
                "sports");
        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_NAME, "Maxbet");
        entry.put(SportCenterSQLiteHelper.COLUMN_ADDRESS, "Vršac");
        entry.put(SportCenterSQLiteHelper.COLUMN_IMAGE, R.mipmap.ic_maxbet);
        entry.put(SportCenterSQLiteHelper.COLUMN_WORKING_HOURS, workingHoursString);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, sportsString);
        entry.put(SportCenterSQLiteHelper.COLUMN_LAT, 45.122170);
        entry.put(SportCenterSQLiteHelper.COLUMN_LONGG, 21.296740);
        entry.put(SportCenterSQLiteHelper.COLUMN_RATING, 1.0);

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_SPORT_CENTER, entry);


        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_ID, 1);
        entry.put(SportCenterSQLiteHelper.COLUMN_FIRSTNAME, "Smiljana");
        entry.put(SportCenterSQLiteHelper.COLUMN_LASTNAME, "Dragoljevic");
        entry.put(SportCenterSQLiteHelper.COLUMN_EMAIL, "smiki@gmail.com");
        entry.put(SportCenterSQLiteHelper.COLUMN_USERNAME, "s");
        entry.put(SportCenterSQLiteHelper.COLUMN_PASSWORD, "s");
        entry.put(SportCenterSQLiteHelper.COLUMN_CITY, "Novi Sad");
        entry.put(SportCenterSQLiteHelper.COLUMN_POINTS, 3);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, "Hattrick");


        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entry);


        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_ID, 2);
        entry.put(SportCenterSQLiteHelper.COLUMN_FIRSTNAME, "Tamara");
        entry.put(SportCenterSQLiteHelper.COLUMN_LASTNAME, "Perlinac");
        entry.put(SportCenterSQLiteHelper.COLUMN_EMAIL, "perlica@gmail.com");
        entry.put(SportCenterSQLiteHelper.COLUMN_USERNAME, "a");
        entry.put(SportCenterSQLiteHelper.COLUMN_PASSWORD, "a");
        entry.put(SportCenterSQLiteHelper.COLUMN_CITY, "Novi Sad");
        entry.put(SportCenterSQLiteHelper.COLUMN_POINTS, 5);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, "Albatros");

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entry);

        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_ID, 3);
        entry.put(SportCenterSQLiteHelper.COLUMN_FIRSTNAME, "Goran");
        entry.put(SportCenterSQLiteHelper.COLUMN_LASTNAME, "Rajic");
        entry.put(SportCenterSQLiteHelper.COLUMN_EMAIL, "goki@gmail.com");
        entry.put(SportCenterSQLiteHelper.COLUMN_USERNAME, "goran");
        entry.put(SportCenterSQLiteHelper.COLUMN_PASSWORD, "goran");
        entry.put(SportCenterSQLiteHelper.COLUMN_CITY, "Novi Sad");
        entry.put(SportCenterSQLiteHelper.COLUMN_POINTS, 7);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, "Maxbet");

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entry);

        entry = new ContentValues();
        entry.put(SportCenterSQLiteHelper.COLUMN_ID, 4);
        entry.put(SportCenterSQLiteHelper.COLUMN_FIRSTNAME, "Marko");
        entry.put(SportCenterSQLiteHelper.COLUMN_LASTNAME, "Vuckovic");
        entry.put(SportCenterSQLiteHelper.COLUMN_EMAIL, "mare@gmail.com");
        entry.put(SportCenterSQLiteHelper.COLUMN_USERNAME, "marko");
        entry.put(SportCenterSQLiteHelper.COLUMN_PASSWORD, "marko");
        entry.put(SportCenterSQLiteHelper.COLUMN_CITY, "Novi Sad");
        entry.put(SportCenterSQLiteHelper.COLUMN_POINTS, 10);
        entry.put(SportCenterSQLiteHelper.COLUMN_SPORTS, "As");

        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entry);

    }
}
