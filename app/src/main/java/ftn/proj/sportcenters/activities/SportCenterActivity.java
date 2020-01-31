package ftn.proj.sportcenters.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ftn.proj.sportcenters.MainActivity;
import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.adapters.DrawerListAdapter;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.Favorites;
import ftn.proj.sportcenters.model.SportCenter;
import ftn.proj.sportcenters.model.User;

public class SportCenterActivity extends AppCompatActivity {

    private SportCenter sportCenter = new SportCenter();
    private ArrayList<String> mNavItems = new ArrayList<String>();
    private ArrayList<Favorites> listFavorites = new ArrayList<Favorites>();

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private SharedPreferences sharedPreferences;
    private TextView ratingTextView;
    private RatingBar ratingBar;
    ToggleButton mFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        final String loggedUsername = sharedPreferences.getString("username","");
        final long loggedId = sharedPreferences.getLong("id",0L);

        setContentView(R.layout.activity_sport_center);
        Uri todoUri = getIntent().getExtras().getParcelable("sportCenter");
        sportCenter = loadSportCenter(todoUri);// (SportCenter) getIntent().getSerializableExtra("sportCenter");
//        sportCenter = (SportCenter) getIntent().getExtras().get("sportCenter");
        ImageView imageView = (ImageView) findViewById(R.id.Image);
        imageView.setImageResource(sportCenter.getImage());

        TextView nameView = (TextView) findViewById(R.id.Name);
        nameView.setText(sportCenter.getName());
        ratingTextView =  findViewById(R.id.ratingTextView);
        ratingTextView.setText(String.valueOf(sportCenter.getRating()));

        TextView workingHoursView = (TextView) findViewById(R.id.WorkingHours);

         StringBuilder sb = new StringBuilder();
        sb.append("Working hours: \n");
        for(String wh : sportCenter.getWorkingHours()) {
            sb.append(wh);
            sb.append("\n");
        }
        workingHoursView.setText(sb.toString());

        TextView addressView = (TextView) findViewById(R.id.Address);
        addressView.setText(sportCenter.getAddress());

        prepareMenu(mNavItems);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter navDrawerAdapter = new DrawerListAdapter(this, mNavItems);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(navDrawerAdapter);


        ratingBar = findViewById(R.id.Rating);
        ratingBar.setEnabled(true);
        ratingBar.setRating(sportCenter.getRating());

        Button mComment = findViewById(R.id.Comment);
        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SportCenterActivity.this, CommentActivity.class);
                intent.putExtra("sportCenter", sportCenter);
                startActivity(intent);
                finish();
            }
        });

        Button mReservation = findViewById(R.id.Reserve);
        mReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SportCenterActivity.this, ReservationActivity.class);
                intent.putExtra("sportCenter", sportCenter);
                startActivity(intent);
                finish();
            }
        });

        Button mGoogleMap = findViewById(R.id.googleMap);
        mGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SportCenterActivity.this, MapsActivity.class);
                intent.putExtra("sportCenter", sportCenter);
                intent.putExtra("lat", sportCenter.getLat());
                intent.putExtra("longg", sportCenter.getLongg());
                intent.putExtra("name", sportCenter.getName());

                startActivity(intent);
               // finish();
            }
        });



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
               float ratingOld = Float.parseFloat(ratingTextView.getText().toString());
               float ratingNew = (ratingOld + rateValue)/2;

               ContentValues updateValues = new ContentValues();
               String selectionClause = SportCenterSQLiteHelper.COLUMN_ID + " = ? " ;
               String[] selectionArgs = {""+sportCenter.getId()+""};
               int rowsUpdated = 0;

                /*
                 * Sets the updated value and updates the selected words.
                 */
                updateValues.put(SportCenterSQLiteHelper.COLUMN_RATING,ratingNew);

                rowsUpdated = getContentResolver().update(
                        DBContentProvider.CONTENT_URI_SPORT_CENTER,   // the user dictionary content URI
                        updateValues,                      // the columns to update
                        selectionClause,                   // the column to select on
                        selectionArgs                      // the value to compare to
                );

               ratingTextView.setText(String.valueOf(rateValue));
               ratingBar.setRating(rateValue);
               ratingBar.setEnabled(false);
               Log.d("Rating", "your selected value is :"+rateValue);
            }
        });

        mFavorite = findViewById(R.id.Favorite);
        mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkWhatChecked(loggedId, isChecked);
            }
        });

        //ako postoji ulogovani korisnik proveri za njegovo ime id clanka u bazi sa trenutnim, ako se poklapa cekiraj zvezdicu u suprotnom ostavi
        //ako na klik cekira zvezdicu ubaci u bazu u suprotnom obrisi iz baze
       setFavoritesButton(loggedId, loggedUsername);

    }



    private void checkWhatChecked(long loggedId, boolean isChecked){
        if (isChecked) {
            mFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_gold));

            String[] columns = {SportCenterSQLiteHelper.COLUMN_ID,SportCenterSQLiteHelper.COLUMN_USER_ID, SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID};
            Cursor cursor = getContentResolver().query(
                    DBContentProvider.CONTENT_URI_FAVORITES,columns,null,null,
                    null);
            boolean br= false;
            while (cursor.moveToNext()) {
                Favorites f = new Favorites();
                f.setId(cursor.getLong(0));
                f.setUserId(cursor.getLong(1));
                f.setSportCenterId(cursor.getLong(2));

                if(f.getUserId() == loggedId && f.getSportCenterId() == sportCenter.getId()){
                    br = true;

                    break;
                    // znaci da postoji vec
                }

            }if(!br) {
                Uri newUri;
                ContentValues newValues = new ContentValues();
                newValues.put(SportCenterSQLiteHelper.COLUMN_USER_ID, loggedId);
                newValues.put(SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID, sportCenter.getId());

                newUri = getContentResolver().insert(
                        DBContentProvider.CONTENT_URI_FAVORITES,   // the user dictionary content URI
                        newValues                          // the values to insert
                );
            }

        }else {
            mFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_grey));
            // Defines selection criteria for the rows you want to delete
            String selectionClause = SportCenterSQLiteHelper.COLUMN_USER_ID + " = ? and " + SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID + " =  ?" ;

            String[] selectionArgs = { String.valueOf(loggedId), String.valueOf(sportCenter.getId()), };
            Uri newUri;
            int rowsDeleted = 0;

            rowsDeleted = getContentResolver().delete(
                    DBContentProvider.CONTENT_URI_FAVORITES,   // the user dictionary content URI
                    selectionClause,
                    selectionArgs// the column to select on
                    // the value to compare to
            );
        }

    }

    private void setFavoritesButton(long loggedId,String loggedUsername){
        if(loggedUsername !=""){
            mFavorite.setChecked(false);
            mFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_grey));

            loadFavoritesByUser(sportCenter.getId(), loggedId);

            for(int i = 0; i < listFavorites.size(); i++) {
                if(sportCenter.getId() == listFavorites.get(i).getSportCenterId() ){
                    mFavorite.setChecked(true);
                    mFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_gold));
                    break;
                }
            }
        }else{
            mFavorite.setChecked(false);
            mFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_grey));
        }
    }

    private void loadFavoritesByUser(long sportCenterId,long loggedId) {
        listFavorites= new ArrayList<Favorites>();
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

            if(f.getUserId()== loggedId && f.getSportCenterId()== sportCenterId ){
                listFavorites.add(f);
                break;
                // zavrsava ovaj activity, na back ne ide na login opet
            }

        }

    }


    private SportCenter loadSportCenter(Uri todoUri) {

        String[] allColumns = { SportCenterSQLiteHelper.COLUMN_ID,
                SportCenterSQLiteHelper.COLUMN_NAME, SportCenterSQLiteHelper.COLUMN_ADDRESS,
                SportCenterSQLiteHelper.COLUMN_IMAGE, SportCenterSQLiteHelper.COLUMN_WORKING_HOURS,
                SportCenterSQLiteHelper.COLUMN_SPORTS, SportCenterSQLiteHelper.COLUMN_LAT,
                SportCenterSQLiteHelper.COLUMN_LONGG, SportCenterSQLiteHelper.COLUMN_RATING};

        Cursor cursor = getContentResolver().query(todoUri, allColumns, null, null,
                null);

        cursor.moveToFirst();

        SportCenter sc = new SportCenter();
        sc.setId(cursor.getLong(0));
        sc.setName(cursor.getString(1));
        sc.setAddress(cursor.getString(2));
        sc.setImage(cursor.getInt(3));
        sc.setLat(cursor.getFloat(6));
        sc.setLongg(cursor.getFloat(7));
        sc.setRating(cursor.getFloat(8));


        sc.setWorkingHours(stringToArrayList(cursor.getString(4),"workingHours"));
        sc.setSports(stringToArrayList(cursor.getString(5),"sports"));
        return sc;
    }


    private ArrayList<String> stringToArrayList(String someString, String nameJSON){
        JSONObject json = null;
        try {
            json = new JSONObject(someString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray items = json.optJSONArray(nameJSON);
        ArrayList<String> retList = new ArrayList<String>();
        for(int i = 0; i<items.length(); i++){
            try {
                retList.add(items.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  retList;
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
        if(position == 0){
            Intent intent = new Intent(SportCenterActivity.this, ProfileActivity.class);
            startActivity(intent);
            //nema finish uvek hocemo mogucnost vracanja na pocetak app

        }else if(position == 1){//Favorites
            Intent intent = new Intent(SportCenterActivity.this, MainActivity.class);
            intent.putExtra("favorites", mNavItems.get(position));
            startActivity(intent);
            finish();
        }else if(position > 1 && position < mNavItems.size()){
            Intent intent = new Intent(SportCenterActivity.this, MainActivity.class);
            intent.putExtra("sportName", mNavItems.get(position));
            startActivity(intent);
            finish();
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}
