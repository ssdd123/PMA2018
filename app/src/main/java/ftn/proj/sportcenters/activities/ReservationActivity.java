package ftn.proj.sportcenters.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import ftn.proj.sportcenters.MainActivity;
import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.Reservation;
import ftn.proj.sportcenters.model.Sport;
import ftn.proj.sportcenters.model.SportCenter;
import ftn.proj.sportcenters.notification.AlarmReceiver;
import ftn.proj.sportcenters.notification.MyNotificationPublisher;

public class ReservationActivity extends AppCompatActivity {

    private ArrayList<SportCenter> mReservation = new ArrayList<SportCenter>();
    private ListView mListView;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private String loggedUsername;
    private long loggedId;
    private SportCenter sportCenter = new SportCenter();
    Spinner sportSpiner;
    Spinner startSpinner;
    CalendarView calendarView;
    String curDate ="24.12.2019.";
    Button mReservationBtn;
    ArrayList<Reservation> allReservation = new ArrayList<>();
    final int callbackId = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel(); // za verzije 8 i iznad
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loggedUsername = sharedPreferences.getString("username","");
        loggedId = sharedPreferences.getLong("id",0L);
        checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

        setContentView(R.layout.activity_reservation);
        sportCenter = (SportCenter) getIntent().getSerializableExtra("sportCenter");

        TextView nameView = (TextView) findViewById(R.id.Name);
        nameView.setText(sportCenter.getName());

        sportSpiner = (Spinner) findViewById(R.id.sportSpinner);
        addItemsOnSportSpinner();
        calendarView = (CalendarView) findViewById(R.id.Calendar);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        calendarView.setMinDate(calendar.getTimeInMillis());
        startSpinner = (Spinner) findViewById(R.id.startSpinner);
        addItemsOnStartSpinner(day);
        mReservationBtn = findViewById(R.id.btnRezervisi);
        mReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertReservation();

                Intent intent = new Intent(ReservationActivity.this, ProfileActivity.class);
                intent.putExtra("sportCenter", sportCenter);
                startActivity(intent);
                finish();
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,  int year, int month,
                                            int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //1 -> SUN; 2->MON; etc.

                curDate =dayOfMonth +"." + (month+1) + "." + year + ".";
                addItemsOnStartSpinner(dayOfWeek);
            }
        });


    }

    private static final String CHANNEL_ID = "com.singhajit.notificationDemo.channelId";

    private void createNotification(){

        Intent intent = new Intent(this, MyNotificationPublisher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_sport)
                .setContentTitle("Sport centers")
                .setContentText("Don't forget about sport event!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setTimeoutAfter(1211)

               ;

        //Intent notificationIntent = new Intent( this, MyNotificationPublisher.class ) ;
//        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
//        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
//        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        boolean alarm = (PendingIntent.getBroadcast(this, 0, new Intent("ALARM"), PendingIntent.FLAG_NO_CREATE) == null);

        if(alarm){
            Intent itAlarm = new Intent("ALARM");
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,0,itAlarm,0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 3);
            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),60000, pendingIntent2);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
                // Set the intent that will fire when the user taps the notification

       /*NOTIFIKACIJE DRUGI POKUSAJ
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        setSingleExactAlarm(alarmManager, cal, broadcast);
        */
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @SuppressLint("NewApi")
    private void setSingleExactAlarm(AlarmManager alarmManager,Calendar cal, PendingIntent broadcast) {

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        }
    }

    private void addPointsToUser(){
        //get old nbr of points
        String[] columns = { SportCenterSQLiteHelper.COLUMN_ID,
                SportCenterSQLiteHelper.COLUMN_POINTS};
        String selectionClausePoints = SportCenterSQLiteHelper.COLUMN_ID + " = ? " ;
        String[] selectionArgsPoints = {""+loggedId+""};
        Cursor cursorPoints = getContentResolver().query( DBContentProvider.CONTENT_URI_USER, columns, selectionClausePoints, selectionArgsPoints,
                null);

        cursorPoints.moveToFirst();
        int oldPointsValue =  cursorPoints.getInt(1);

        //set new value of points

        ContentValues updateValues = new ContentValues();
        String selectionClause = SportCenterSQLiteHelper.COLUMN_ID + " = ? " ;
        String[] selectionArgs = {""+loggedId+""};
        int rowsUpdated = 0;

        /*
         * Sets the updated value and updates the selected words.
         */
        updateValues.put(SportCenterSQLiteHelper.COLUMN_POINTS,oldPointsValue+1);

        rowsUpdated = getContentResolver().update(
                DBContentProvider.CONTENT_URI_USER,   // the user dictionary content URI
                updateValues,                      // the columns to update
                selectionClause,                   // the column to select on
                selectionArgs                      // the value to compare to
        );

    }

    private void insertReservation(){

        String[] columns = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_USER_ID,
                SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID, SportCenterSQLiteHelper.COLUMN_SPORT_NAME,
                SportCenterSQLiteHelper.COLUMN_PRICE, SportCenterSQLiteHelper.COLUMN_DATE, SportCenterSQLiteHelper.COLUMN_PERIOD,
                SportCenterSQLiteHelper.COLUMN_EVENT_ID};
        Cursor cursor = getContentResolver().query(DBContentProvider.CONTENT_URI_RESERVATION,
                columns, null, null,null);

        while (cursor.moveToNext()) {
            getReservation(cursor);
        }
        //provera da li se ponovila rezervacija
        int random = new Random().nextInt((50 - 10) + 1) + 10;
        double scale = 100.0;
        boolean alreadyTaken = false;
        for(Reservation r : allReservation){
            if(r.getSportCenterId() == sportCenter.getId() && r.getSportName().equals(sportSpiner.getSelectedItem().toString())
            && r.getDate().equals(curDate) && r.getPeriod().equals( startSpinner.getSelectedItem().toString())){
                alreadyTaken = true;
            }
        }

        if(!alreadyTaken) {
            long calendarEventId = calendarIntegration(  sportSpiner.getSelectedItem().toString(),random * scale, curDate,startSpinner.getSelectedItem().toString());

            addPointsToUser();
            createNotification();

            Uri newUri;
                    ContentValues newValues = new ContentValues();
            newValues.put(SportCenterSQLiteHelper.COLUMN_USER_ID, loggedId);
            newValues.put(SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID, sportCenter.getId());
            newValues.put(SportCenterSQLiteHelper.COLUMN_SPORT_NAME, sportSpiner.getSelectedItem().toString());
            newValues.put(SportCenterSQLiteHelper.COLUMN_PRICE, random * scale);
            newValues.put(SportCenterSQLiteHelper.COLUMN_DATE, curDate);
            newValues.put(SportCenterSQLiteHelper.COLUMN_PERIOD, startSpinner.getSelectedItem().toString());
            newValues.put(SportCenterSQLiteHelper.COLUMN_EVENT_ID, calendarEventId);


            newUri = getContentResolver().insert(
                    DBContentProvider.CONTENT_URI_RESERVATION,   // the user dictionary content URI
                    newValues                          // the values to insert
            );

          //  Toast.makeText(this, "Successfully reserved! " ,Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Can't reserve. Set new date!  " ,Toast.LENGTH_SHORT).show();

        }

    }
    private void getReservation(Cursor cursor){
        Reservation sc = new Reservation();
        sc.setId(cursor.getLong(0));
        sc.setUserId(cursor.getLong(1));
        sc.setSportCenterId(cursor.getLong(2));
        sc.setSportName(cursor.getString(3));
        sc.setPrice(cursor.getFloat(4));
        sc.setDate(cursor.getString(5));
        sc.setPeriod(cursor.getString(6));
        sc.setEventId(cursor.getLong(7));
        allReservation.add(sc);
    }

    private ArrayList<String> stringToList(String someString, String nameJSON){
        JSONObject json = null;
        try {
            json = new JSONObject(someString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray items = json.optJSONArray(nameJSON);
        ArrayList<String> workingHours = new ArrayList<String>();
        for(int i = 0; i<items.length(); i++){
            try {
                workingHours.add(items.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return workingHours;
    }

    //SportSpiner
    public void addItemsOnSportSpinner() {
        List<Sport> listSport = new ArrayList<Sport>();
        List<String> listSportName = new ArrayList<String>();

            String[] column = {
                    SportCenterSQLiteHelper.COLUMN_ID,SportCenterSQLiteHelper.COLUMN_NAME};
            Cursor cursor = getContentResolver().query(
                    DBContentProvider.CONTENT_URI_SPORT,column, null, null,
                    null);

            while (cursor.moveToNext()) {
                Sport sport = new Sport();
                sport.setId(cursor.getLong(0));
                sport.setName(cursor.getString(1));
                listSport.add(sport);
            }

        for(String sId : sportCenter.getSports()){// 1,2,4
            for(Sport ssss : listSport ){
                if(sId.equals(String.valueOf(ssss.getId()))){
                    listSportName.add(ssss.getName());
                    break;
                }
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,listSportName );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpiner.setAdapter(dataAdapter);

    }

    public void addItemsOnStartSpinner(int dayOfWeek) {
        List<String> listPeriod = new ArrayList<String>();
        String dayOfWeekStr="";
        mReservationBtn = findViewById(R.id.btnRezervisi);
        mReservationBtn.setEnabled(false);
        if (dayOfWeek==1){
            dayOfWeekStr="Sun";
        } else if(dayOfWeek==2){
            dayOfWeekStr="Mon";

        }
        else if(dayOfWeek==3){
            dayOfWeekStr="Tue";

        }
        else if(dayOfWeek==4){
            dayOfWeekStr="Wed";

        } else if(dayOfWeek==5){
            dayOfWeekStr="Thu";

        } else if(dayOfWeek==6){
            dayOfWeekStr="Fri";

        } else if(dayOfWeek==7){
            dayOfWeekStr="Sat";

        }else{
            dayOfWeekStr="Mon";
        }
        for(String wh : sportCenter.getWorkingHours()){ // wh = "Mon 8:00-22:00"
          //  String substr=wh.substring(wh.indexOf(":") + 1);
            if(dayOfWeekStr.equals(wh.substring(0,3))) {
                String[] split = wh.split("-"); //Tue 08:00-22:00 "Tue 08:00"
                int startAt = Integer.parseInt(split[0].substring(split[0].length() - 5, split[0].length() - 3));
                int endAt = Integer.parseInt(split[1].substring(0, 2));
                int br = startAt;

                while (br != endAt) {
                    listPeriod.add(br + "h-" + (br +1) +"h");
                    br = br + 1;
                }
                               mReservationBtn.setEnabled(true);
                mReservationBtn.setBackgroundColor(Color.parseColor("#1606ff"));

                break;
            }else{
                mReservationBtn.setEnabled(false);
                mReservationBtn.setBackgroundColor(Color.parseColor("#9CA0A0"));
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,listPeriod );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(dataAdapter);

    }

    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }

    //@RequiresApi(api = Build.VERSION_CODES.N)
    private long calendarIntegration( String sportName,double price,String thatDate,String period){
        //INTEGRACIJA KALENDARA

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;

        Calendar  beginTime = Calendar.getInstance();
        String datum[] = thatDate.split("\\.");
        String periodBezH = period.replace("h", "");
        String sati[] = periodBezH.split("-");
        //beginTime.set();Integer.parseInt(et.getText().toString());
        beginTime.set(Integer.parseInt(datum[2]), Integer.parseInt(datum[1])-1, Integer.parseInt(datum[0]), Integer.parseInt(sati[0]), 0);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2019, 11, 18, 8, 45);
        endMillis = endTime.getTimeInMillis();



        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DURATION, "+P1H");
        // values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, sportName);
        values.put(CalendarContract.Events.DESCRIPTION,sportName+" Price:" + price);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.HAS_ALARM, 1);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Toast.makeText(this, "Alarm is set in calendar. " ,Toast.LENGTH_SHORT).show();

        return eventID;

    }


}
