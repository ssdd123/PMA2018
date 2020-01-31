package ftn.proj.sportcenters.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import ftn.proj.sportcenters.activities.CurrentLocationActivity;
import ftn.proj.sportcenters.activities.MapsActivity;
import ftn.proj.sportcenters.activities.SportCenterActivity;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.activities.MyProfileActivity;
import ftn.proj.sportcenters.activities.ProfileActivity;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.dialog.UserDialog;

import ftn.proj.sportcenters.fragments.MyReservationsFragment;
import ftn.proj.sportcenters.model.Invitation;
import ftn.proj.sportcenters.model.Reservation;
import ftn.proj.sportcenters.model.SportCenter;

public class MyReservationsAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Reservation> mReservations;

    public MyReservationsAdapter(Context mContext, ArrayList<Reservation> mReservations) {
        this.mContext = mContext;
        this.mReservations = mReservations;
    }

    @Override
    public int getCount() {
        return mReservations.size();
    }

    @Override
    public Object getItem(int position) {
        return mReservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mReservations.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_reservation_item, null);
        }
        else {
            view = convertView;
        }

        TextView nameView = (TextView) view.findViewById(R.id.SportCenterName);
        TextView priceView = (TextView) view.findViewById(R.id.Price);
        TextView dateView = (TextView) view.findViewById(R.id.ReservationDate);
        TextView periodView = (TextView) view.findViewById(R.id.Period);
        TextView SportNameView = (TextView) view.findViewById(R.id.SportName);

        final TextView idView = (TextView) view.findViewById(R.id.MyReservationId);

        String[] column = {
                SportCenterSQLiteHelper.COLUMN_NAME};
        Cursor cursor = mContext.getContentResolver().query(
                Uri.parse(DBContentProvider.CONTENT_URI_SPORT_CENTER + "/" +
                        mReservations.get(position).getSportCenterId()),column,null,null,
                null);

        cursor.moveToFirst();
        nameView.setText( cursor.getString(0) );
        //nameView.setText( mReservations.get(position).getSportCenterName()); //zamenjeno
        dateView.setText( mReservations.get(position).getDate().toString() );
        periodView.setText( mReservations.get(position).getPeriod() );
        idView.setText( String.valueOf(mReservations.get(position).getId()) );
        SportNameView.setText(mReservations.get(position).getSportName());


        int pointsValue = findPointsOfUser(position);
        if(pointsValue>10){
            priceView.setText( String.valueOf(mReservations.get(position).getPrice()) + " + 10% off " );

        }else if(pointsValue>15){
            priceView.setText( String.valueOf(mReservations.get(position).getPrice())+ "+ 15% off " );

        }else if(pointsValue>20){
            priceView.setText( String.valueOf(mReservations.get(position).getPrice()) + "+ 20% off " );

        }else if(pointsValue>25){
            priceView.setText( String.valueOf(mReservations.get(position).getPrice())+ "+ 25% off " );

        }else{
            priceView.setText( String.valueOf(mReservations.get(position).getPrice()) );

        }

        Button mPozovi = view.findViewById(R.id.btnPozovi);
        Button btnOtkazi =  view.findViewById(R.id.btnOtkazi);
        btnOtkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //brisemo pozive vezane za ovu rezervaciju
                String selectionClauseInvitation = SportCenterSQLiteHelper.COLUMN_RESERVATION_ID + " = ?" ;

                String[] selectionArgsInvitation = { String.valueOf(mReservations.get(position).getId()) };

                Uri newUriInvitation;
                int rowsDeletedInvitation = 0;

                rowsDeletedInvitation = mContext.getContentResolver().delete(
                        DBContentProvider.CONTENT_URI_INVITATION,   // the user dictionary content URI
                        selectionClauseInvitation,
                        selectionArgsInvitation// the column to select on
                        // the value to compare to
                );
                //brisemo dogadjaj u kalendaru
                String DEBUG_TAG = "ReservationCancel";

                long eventID = mReservations.get(position).getEventId();

                ContentResolver cr = mContext.getContentResolver();
                Uri deleteUri = null;
                deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                int rows = cr.delete(deleteUri, null, null);
                Log.i(DEBUG_TAG, "Rows deleted: " + rows);

                // Brisemo rezervaciju
                String selectionClause = SportCenterSQLiteHelper.COLUMN_ID + " = ?" ;
                String[] selectionArgs = { String.valueOf(mReservations.get(position).getId()) };

                Uri newUri;
                int rowsDeleted = 0;

                rowsDeleted = mContext.getContentResolver().delete(
                        DBContentProvider.CONTENT_URI_RESERVATION,   // the user dictionary content URI
                        selectionClause,
                        selectionArgs// the column to select on
                        // the value to compare to
                );
                Toast.makeText(mContext,"Reservation canceled.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ProfileActivity.class);
                mContext.startActivity(intent);
                ((ProfileActivity)mContext).finish();
            }
        });


        Button btnGoogleMaps = (Button) view.findViewById(R.id.btnGoogleMaps);
        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, CurrentLocationActivity.class);
                mContext.startActivity(intent);
                SportCenter sportCenter = findSportCenter(position);
                intent.putExtra("sportCenter", sportCenter);
                intent.putExtra("lat", sportCenter.getLat());
                intent.putExtra("longg", sportCenter.getLongg());
                intent.putExtra("name", sportCenter.getName());

                mContext.startActivity(intent);
            }
        });

        mPozovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyReservationsFragment fragment2 = new MyReservationsFragment();
                  Bundle arguments = new Bundle();
                arguments.putSerializable("reservation",mReservations.get(position));
             //   fragment.setArguments(arguments);
                ProfileActivity activity = (ProfileActivity) mContext;

                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

                new UserDialog(mReservations.get(position)).show(activity.getSupportFragmentManager(),"userdialog");

            }
        });
        return view;
    }

    private SportCenter findSportCenter (int position){
        SportCenter sportCenter = new SportCenter();
        long sporCenterId = mReservations.get(position).getSportCenterId();

        String[] columnsSport = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_NAME,
                SportCenterSQLiteHelper.COLUMN_ADDRESS, SportCenterSQLiteHelper.COLUMN_IMAGE,
                SportCenterSQLiteHelper.COLUMN_LAT, SportCenterSQLiteHelper.COLUMN_LONGG, SportCenterSQLiteHelper.COLUMN_RATING};
        Cursor cursorSport = mContext.getContentResolver().query(DBContentProvider.CONTENT_URI_SPORT_CENTER,
                columnsSport, SportCenterSQLiteHelper.COLUMN_ID + " = ?", new String[] {""+sporCenterId+""},null);
        // columnsSport, SportCenterSQLiteHelper.COLUMN_ID + " = "+f.getSportCenterId()+"", null,null);
        cursorSport.moveToFirst();
        sportCenter = createSportCenter(cursorSport);
        return sportCenter;
    }

    private SportCenter createSportCenter(Cursor cursor){
        SportCenter sc = new SportCenter();
        sc.setId(cursor.getLong(0));
        sc.setName(cursor.getString(1));
        sc.setAddress(cursor.getString(2));
        sc.setImage(cursor.getInt(3));
        sc.setLat(cursor.getFloat(4));
        sc.setLongg(cursor.getFloat(5));
        sc.setRating(cursor.getFloat(6));
        return sc;
    }

    private int findPointsOfUser (int position){
        int points = 0;
        SportCenter sportCenter = new SportCenter();
        long userId = mReservations.get(position).getUserId();
        String[] columnsPoints = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_POINTS};
        Cursor cursorPoints= mContext.getContentResolver().query(DBContentProvider.CONTENT_URI_USER,
                columnsPoints, SportCenterSQLiteHelper.COLUMN_ID + " = ?", new String[] {""+userId+""},null);

        cursorPoints.moveToFirst();
        points = cursorPoints.getInt(1);

        return points;
    }
}
/* STARI KOD
                fragmentTransaction.replace(R.id.MyReservationsFragmentId, fragment).
                        replace(R.id.MyInvitationsFragmentId,fragment).commit();
               fragmentTransaction.replace(R.id.MyReservationsFragmentId, fragment).commit();
                fragmentTransaction.remove(activity.getSupportFragmentManager().findFragmentById(R.id.MyReservationsFragmentId)).commit();

                fragmentTransaction.add(R.id.MyInvitationsFragmentId,fragment).commit();
                fragmentTransaction.addToBackStack("fragMyInvitationsFragmentId");
                fragmentTransaction.attach(fragment).commit();
*/