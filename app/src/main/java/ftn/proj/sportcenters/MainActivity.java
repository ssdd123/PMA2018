package ftn.proj.sportcenters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;

import ftn.proj.sportcenters.activities.AboutActivity;
import ftn.proj.sportcenters.activities.LoginActivity;
import ftn.proj.sportcenters.activities.ProfileActivity;
import ftn.proj.sportcenters.activities.SettingsActivity;
import ftn.proj.sportcenters.activities.SportCenterActivity;
import ftn.proj.sportcenters.adapters.DrawerListAdapter;
import ftn.proj.sportcenters.adapters.MainActivityAdapter;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.Favorites;
import ftn.proj.sportcenters.model.SportCenter;

public class MainActivity extends AppCompatActivity {
    //gmail for firebase:
    //Tim92019Tim92019@gmail.com
    //deDE92019*

    private ArrayList<SportCenter> mSportCenters = new ArrayList<SportCenter>();
    private ArrayList<SportCenter> mSportCentersTemp = new ArrayList<SportCenter>();

    private ListView mListView;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private String loggedUsername;
    private long loggedId;
    private ArrayList<String> mNavItems = new ArrayList<String>();
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;


   /*
   OLD NOTIFICATION
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    TextView btnDate ;
    final Calendar myCalendar = Calendar.getInstance () ;
*/
    private String selectedSport="Home";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loggedUsername = sharedPreferences.getString("username","");
        loggedId = sharedPreferences.getLong("id",0L);

        setContentView(R.layout.activity_main);
        mListView=findViewById(R.id.SportCenterList);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(getIntent().getExtras().containsKey("sportName")){
                loadItemsBySport(getIntent().getExtras().getString("sportName"));
            }
            else if(getIntent().getExtras().containsKey("favorites")) {
                loadItemsByFavorites();
            }
            else{
                loadItems(mSportCenters);
                setNewMainActivityAdapter();
            }
        }
        else{
            loadItems(mSportCenters);
            setNewMainActivityAdapter();
        }


        prepareMenu(mNavItems);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter navDrawerAdapter = new DrawerListAdapter(this, mNavItems);

        // set a custom shadow that overlays the main content when the drawer opens
      //  mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(navDrawerAdapter);
    }

    private void setNewMainActivityAdapter() {
        MainActivityAdapter adapter = new MainActivityAdapter(this,mSportCenters);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view,
                                                                     int position, long id) {
                                                 Uri todoUri = Uri.parse(DBContentProvider.CONTENT_URI_SPORT_CENTER + "/" + id);
                                                 Intent intent = new Intent(MainActivity.this, SportCenterActivity.class);
                                                 intent.putExtra("sportCenter", todoUri);
                                                 startActivity(intent);
                                                // finish(); // dozvoljavamo back opciju
                                             }
                                         }
        );
    }

    private void loadItems(ArrayList<SportCenter> mSportCenters) {
        mSportCenters = new ArrayList<>();
        String[] columns = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_NAME,
                SportCenterSQLiteHelper.COLUMN_ADDRESS, SportCenterSQLiteHelper.COLUMN_IMAGE,
                SportCenterSQLiteHelper.COLUMN_LAT, SportCenterSQLiteHelper.COLUMN_LONGG, SportCenterSQLiteHelper.COLUMN_RATING};
        Cursor cursor = getContentResolver().query(DBContentProvider.CONTENT_URI_SPORT_CENTER,
                columns, null, null,null);
        if(cursor.getCount()==0) {
            DatabaseTool.initDB(this);
            cursor = getContentResolver().query(DBContentProvider.CONTENT_URI_SPORT_CENTER,
                    columns,null, null,null);
        }
        while (cursor.moveToNext()) {
            createSportCenter(cursor);
        }
    }

    private void createSportCenter(Cursor cursor){
        SportCenter sc = new SportCenter();
        sc.setId(cursor.getLong(0));
        sc.setName(cursor.getString(1));
        sc.setAddress(cursor.getString(2));
        sc.setImage(cursor.getInt(3));
        sc.setLat(cursor.getFloat(4));
        sc.setLongg(cursor.getFloat(5));
        sc.setRating(cursor.getFloat(6));
        mSportCenters.add(sc);
    }


    private void loadItemsByFavorites(){
        mSportCenters= new ArrayList<SportCenter>();
        ArrayList<Favorites> listFavorites= new ArrayList<Favorites>();
        String[] columns1 = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_USER_ID,
                SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID};
        Cursor cursor = getContentResolver().query(
                DBContentProvider.CONTENT_URI_FAVORITES,columns1,null,null,
                    null);
            // String[] selectionArgs = { String.valueOf(loggedId), String.valueOf(sportCenter.getId()), };
        while (cursor.moveToNext()) {

            Favorites f = new Favorites();
            f.setId(cursor.getLong(0));
            f.setUserId(cursor.getLong(1));
            f.setSportCenterId(cursor.getLong(2));

            if(f.getUserId()== loggedId ){
                listFavorites.add(f);

                String[] columnsSport = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_NAME,
                        SportCenterSQLiteHelper.COLUMN_ADDRESS, SportCenterSQLiteHelper.COLUMN_IMAGE,
                        SportCenterSQLiteHelper.COLUMN_LAT, SportCenterSQLiteHelper.COLUMN_LONGG, SportCenterSQLiteHelper.COLUMN_RATING};
                Cursor cursorSport = getContentResolver().query(DBContentProvider.CONTENT_URI_SPORT_CENTER,
                        columnsSport, SportCenterSQLiteHelper.COLUMN_ID + " = ?", new String[] {""+f.getSportCenterId()+""},null);
                // columnsSport, SportCenterSQLiteHelper.COLUMN_ID + " = "+f.getSportCenterId()+"", null,null);
                cursorSport.moveToFirst();
                createSportCenter(cursorSport);

            }

        }
        setNewMainActivityAdapter();

    }
    private void loadItemsBySport(String sportName) {
        mSportCenters= new ArrayList<SportCenter>();
        String[] columns1 = {SportCenterSQLiteHelper.COLUMN_ID};
        Cursor cursor1 = getContentResolver().query(DBContentProvider.CONTENT_URI_SPORT,
                columns1, SportCenterSQLiteHelper.COLUMN_NAME + " like ?", new String[] {sportName+"%"},null);
        cursor1.moveToFirst();
        String[] columns2 = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_NAME,
                SportCenterSQLiteHelper.COLUMN_ADDRESS, SportCenterSQLiteHelper.COLUMN_IMAGE,
                SportCenterSQLiteHelper.COLUMN_LAT, SportCenterSQLiteHelper.COLUMN_LONGG, SportCenterSQLiteHelper.COLUMN_RATING};
        Cursor cursor2 = getContentResolver().query(DBContentProvider.CONTENT_URI_SPORT_CENTER,
                columns2, SportCenterSQLiteHelper.COLUMN_SPORTS + " like ?", new String[] {"%"+cursor1.getInt(0)+"%"},null);

        while (cursor2.moveToNext()) {
            createSportCenter(cursor2);
        }
        setNewMainActivityAdapter();
    }

    ArrayAdapter<String> adapterFilter;
    //SEARCH
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_itemdetail, menu);

        MenuItem item = menu.findItem(R.id.search_centers);
        SearchView searchView = (SearchView)item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText=newText.toLowerCase();
               // adapterFilter.getFilter().filter(newText);
                mSportCentersTemp= new ArrayList<SportCenter>();
                mSportCenters= new ArrayList<SportCenter>();

                    String[] columns2 = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_NAME,
                            SportCenterSQLiteHelper.COLUMN_ADDRESS, SportCenterSQLiteHelper.COLUMN_IMAGE,
                            SportCenterSQLiteHelper.COLUMN_LAT, SportCenterSQLiteHelper.COLUMN_LONGG, SportCenterSQLiteHelper.COLUMN_RATING};
                    Cursor cursor2 = getContentResolver().query(DBContentProvider.CONTENT_URI_SPORT_CENTER,
                            columns2, SportCenterSQLiteHelper.COLUMN_NAME + " like ?", new String[]{"%" + newText + "%"}, null);

                    while (cursor2.moveToNext()) {
                        createSportCenter(cursor2);
                    }



                setNewMainActivityAdapter();

                return false;
            }
        });
//END SEARCH
        return true;
    }

    private void prepareMenu(ArrayList<String> mNavItems ){

        mNavItems.add("Profile");
        mNavItems.add("Favorites");
        String[] column = {
                SportCenterSQLiteHelper.COLUMN_NAME};
        Cursor cursor = getContentResolver().query(
                DBContentProvider.CONTENT_URI_SPORT,column,null,null,
                null);
        while (cursor.moveToNext()) {
            mNavItems.add(cursor.getString(0));
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }
    private void selectItemFromDrawer(int position) {

        if(position == 0) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        }else if(position == 1){//Favorites
            loadItemsByFavorites();

        }else if(position > 1 && position < mNavItems.size()){
            loadItemsBySport(mNavItems.get(position));
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);

                return true;


            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intentLogout);
                return true;

            case R.id.action_profile:
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                return true;

            case R.id.action_about:
                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                return true;

            case R.id.action_refresh:
//                Intent intent = getIntent();
//                loadItems(mSportCenters);
//                setNewMainActivityAdapter();
//                finish();
//                startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//NOTIFICATION START
/*
private void scheduleNotification (Notification notification , long delay) {
    Intent notificationIntent = new Intent( this, MyNotificationPublisher.class ) ;
    notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID , 1 ) ;
    notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION , notification) ;
    PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
    AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
    assert alarmManager != null;
    alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , 117664 , pendingIntent) ;
}
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view , int year , int monthOfYear , int dayOfMonth) {
            myCalendar .set(Calendar. YEAR , year) ;
            myCalendar .set(Calendar. MONTH , monthOfYear) ;
            myCalendar .set(Calendar. DAY_OF_MONTH , dayOfMonth) ;
            updateLabel() ;
        }
    } ;
    public void setDate (View view) {
        new DatePickerDialog(
                MainActivity. this, date ,

                myCalendar .get(Calendar. YEAR ) ,
                myCalendar .get(Calendar. MONTH ) ,
                myCalendar .get(Calendar. DAY_OF_MONTH )
        ).show() ;
    }

    private void updateLabel () {
        btnDate = findViewById(R.id.btnDate) ;
        String myFormat = "dd/MM/yy HH:mm:ss" ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale.getDefault()) ;
        Date date = myCalendar.getTime() ;
        btnDate.setText(sdf.format(date)) ;
        String newTime = (btnDate.getText().toString()).substring((btnDate.getText().toString()).length()- 5);
        newTime = newTime +"11:01";

        scheduleNotification(getNotification( newTime) , date.getTime()) ;
    }

 */
}










    /*
    izmene za firebase
    build.gradle (project-level)

    Add Firebase Gradle buildscript dependency
        classpath 'com.google.gms:google-services:4.2.0'



app/build.gradle

    Add Firebase plugin for Gradle
        apply plugin: 'com.google.gms.google-services'

    build.gradle will include these new dependencies:
        compile 'com.google.firebase:firebase-database:16.0.4'

     */
