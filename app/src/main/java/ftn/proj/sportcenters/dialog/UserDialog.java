package ftn.proj.sportcenters.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ftn.proj.sportcenters.MainActivity;
import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.Reservation;
import ftn.proj.sportcenters.model.SportCenter;
import ftn.proj.sportcenters.model.User;

public class UserDialog  extends DialogFragment {

    private ArrayList<User> users;
    private ArrayList<String> usersName;
    private List<String> list;
    private long reservationId;


    public UserDialog(Reservation reservation) {
        reservationId = reservation.getId();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        list = new ArrayList<String>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        users = getUsers(sharedPreferences);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite friends");
        for(User u : users){
            usersName.add(u.getUsername());
        }

        String[] arrayUsersName = usersName.toArray(new String[usersName.size()]);


        builder.setMultiChoiceItems(arrayUsersName, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                //

                String[] arrayUsersName = usersName.toArray(new String[usersName.size()]);
                if(b){
                    list.add(arrayUsersName[i]);
                }else if(list.contains(arrayUsersName[i])){
                    list.remove(arrayUsersName[i]);

                }//dodacemo u bazu invitation pozivi za korisnike koji su cekirani na osnovu username(unique)




            }
        });



        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //find selected user
                if(list!= null){
                    for(String selectedUsername : list){

                        String[] columns = {SportCenterSQLiteHelper.COLUMN_ID,SportCenterSQLiteHelper.COLUMN_USERNAME, SportCenterSQLiteHelper.COLUMN_PASSWORD,
                                SportCenterSQLiteHelper.COLUMN_POINTS, SportCenterSQLiteHelper.COLUMN_SPORTS,SportCenterSQLiteHelper.COLUMN_FIRSTNAME};

                        Cursor cursorUser = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_USER,
                                columns, SportCenterSQLiteHelper.COLUMN_USERNAME + " = ?", new String[] {""+selectedUsername+""},null);
                        // columnsSport, SportCenterSQLiteHelper.COLUMN_ID + " = "+f.getSportCenterId()+"", null,null);
                        cursorUser.moveToFirst();
                        User selectedUser = createUser(cursorUser);
                        Uri newUri;
                        ContentValues newValues = new ContentValues();
                        newValues.put(SportCenterSQLiteHelper.COLUMN_USER_ID, selectedUser.getId());
                        newValues.put(SportCenterSQLiteHelper.COLUMN_RESERVATION_ID, reservationId);
                        newValues.put(SportCenterSQLiteHelper.COLUMN_ACCEPTED, false);


                        newUri = getActivity().getContentResolver().insert(
                                DBContentProvider.CONTENT_URI_INVITATION,   // the user dictionary content URI
                                newValues                          // the values to insert
                        );

                    }
                    Toast.makeText(getActivity(), "Wait for friends.",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "No one was called.",Toast.LENGTH_SHORT).show();

                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Never go alone.",Toast.LENGTH_SHORT).show();

            }
        });
        return builder.create();
    }


    private User createUser(Cursor cursor){
        User sc = new User();
        sc.setId(cursor.getLong(0));
        sc.setUsername(cursor.getString(1));
        sc.setPassword(cursor.getString(2));
        sc.setPoints(cursor.getInt(3));
        sc.setSports(cursor.getString(4));
        sc.setFirstname(cursor.getString(5));
        return sc;
    }

    private  ArrayList<User> getUsers( SharedPreferences sharedPreferences){
        users = new ArrayList<>();
        usersName = new ArrayList<>();
        String loggedUsername =  sharedPreferences.getString("username","");
        long loggedId = sharedPreferences.getLong("id",0L);

        String[] columns = {SportCenterSQLiteHelper.COLUMN_ID,SportCenterSQLiteHelper.COLUMN_USERNAME, SportCenterSQLiteHelper.COLUMN_PASSWORD,
                SportCenterSQLiteHelper.COLUMN_POINTS, SportCenterSQLiteHelper.COLUMN_SPORTS,SportCenterSQLiteHelper.COLUMN_FIRSTNAME};
        Cursor cursor = getActivity().getContentResolver().query(
                DBContentProvider.CONTENT_URI_USER, columns, null, null,
                null);

        if(cursor.getCount()==0) {
            DatabaseTool.initDB(getActivity());
            cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_USER,
                    columns,null, null,null);
        }

        while (cursor.moveToNext()) {
            User sc = new User();
            sc.setId(cursor.getLong(0));
            sc.setUsername(cursor.getString(1));
            sc.setPassword(cursor.getString(2));
            sc.setPoints(cursor.getInt(3));
            sc.setSports(cursor.getString(4));
            sc.setFirstname(cursor.getString(5));

            if(loggedId != sc.getId()) {// da izbegnemoo svoje ime u listi
                users.add(sc);
           //     usersName.add(sc.getUsername());
            }
        }

        return  users;
    }
}
