package ftn.proj.sportcenters.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.activities.ProfileActivity;
import ftn.proj.sportcenters.activities.ReservationActivity;
import ftn.proj.sportcenters.adapters.MyReservationsAdapter;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.Reservation;
import ftn.proj.sportcenters.model.SportCenter;
import android.database.Cursor;
import android.net.Uri;

public class MyReservationsFragment extends Fragment {

    private ArrayList<Reservation> myReservations = new ArrayList<Reservation>();
    private ListView mListView;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private String loggedUsername;
    private long loggedId;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_reservations_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

       // Button btnOtkazi = (Button)findViewById(R.id.btnOtkazi);


        mListView=view.findViewById(R.id.MyReservationsList);
        loadItems(myReservations);

        MyReservationsAdapter adapter = new MyReservationsAdapter(this.getContext(), myReservations);
        mListView.setAdapter(adapter);

    }

    private void loadItems(ArrayList<Reservation> myReservations) {
        myReservations = new ArrayList<>();

        String[] columns = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_USER_ID,
                SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID, SportCenterSQLiteHelper.COLUMN_SPORT_NAME,
                SportCenterSQLiteHelper.COLUMN_PRICE, SportCenterSQLiteHelper.COLUMN_DATE, SportCenterSQLiteHelper.COLUMN_PERIOD,
                SportCenterSQLiteHelper.COLUMN_EVENT_ID};
        Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_RESERVATION,
                columns, null, null,null);

        while (cursor.moveToNext()) {

            getReservation(cursor);
        }
    }


    private void getReservation(Cursor cursor){

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loggedId = sharedPreferences.getLong("id",0L);

        Reservation sc = new Reservation();

        sc.setId(cursor.getLong(0));
        sc.setUserId(cursor.getLong(1));
        sc.setSportCenterId(cursor.getLong(2));
        sc.setSportName(cursor.getString(3));
        sc.setPrice(cursor.getFloat(4));
        sc.setDate(cursor.getString(5));
        sc.setPeriod(cursor.getString(6));
        sc.setEventId(cursor.getLong(7));
        if(sc.getUserId() == loggedId){
            myReservations.add(sc);
        }
    }
}
