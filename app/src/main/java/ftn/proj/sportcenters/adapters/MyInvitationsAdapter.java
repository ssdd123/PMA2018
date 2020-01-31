package ftn.proj.sportcenters.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.activities.ProfileActivity;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.Invitation;

public class MyInvitationsAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Invitation> mInvitations;

    public MyInvitationsAdapter(Context mContext, ArrayList<Invitation> mInvitations) {
        this.mContext = mContext;
        this.mInvitations = mInvitations;
    }

    @Override
    public int getCount() {
        return mInvitations.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mInvitations.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_invitation_item, null);
        }
        else {
            view = convertView;
        }

        TextView nameView = (TextView) view.findViewById(R.id.SportCenterName);
        TextView friendView = (TextView) view.findViewById(R.id.FriendName);
        TextView dateView = (TextView) view.findViewById(R.id.ReservationDate);
        TextView periodView = (TextView) view.findViewById(R.id.Period);
        TextView sportNameView = (TextView) view.findViewById(R.id.SportName);
        //get reservation
        String[] columns = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_USER_ID,
                SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID, SportCenterSQLiteHelper.COLUMN_SPORT_NAME,
                SportCenterSQLiteHelper.COLUMN_PRICE, SportCenterSQLiteHelper.COLUMN_DATE, SportCenterSQLiteHelper.COLUMN_PERIOD};
        Cursor cursor = mContext.getContentResolver().query(
                Uri.parse(DBContentProvider.CONTENT_URI_RESERVATION + "/" +
                        mInvitations.get(position).getReservationId()),columns,null,null,
                null);

        cursor.moveToFirst();
        long friendId = cursor.getLong(1);
        long sportCenterId = cursor.getLong(2);

        dateView.setText(cursor.getString(5));
        periodView.setText(cursor.getString(6));

        String[] columnSportCenter = {
                SportCenterSQLiteHelper.COLUMN_NAME};
        Cursor cursorSportCenter = mContext.getContentResolver().query(
                Uri.parse(DBContentProvider.CONTENT_URI_SPORT_CENTER + "/" +
                        sportCenterId),columnSportCenter,null,null,
                null);

        cursorSportCenter.moveToFirst();

         //nameView.setText( mReservations.get(position).getSportCenterName()); //zamenjeno

        String[] columnUser = {
                SportCenterSQLiteHelper.COLUMN_USERNAME};
        Cursor cursorUser = mContext.getContentResolver().query(
                Uri.parse(DBContentProvider.CONTENT_URI_USER + "/" +
                        friendId),columnUser,null,null,
                null);

        cursorUser.moveToFirst();

        sportNameView.setText(cursor.getString(3));
        nameView.setText( cursorSportCenter.getString(0) );// naziv sportskog centra
        friendView.setText( cursorUser.getString(0) );// naziv  korisnika koji je poslao poziv


        Button btnPrihvati = view.findViewById(R.id.btnPrihvati);
        btnPrihvati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Brisemo rezervaciju
                String selectionClause = SportCenterSQLiteHelper.COLUMN_ID + " = ?" ;

                String[] selectionArgs = { String.valueOf(mInvitations.get(position).getId()) };
                Uri newUri;
                int rowsDeleted = 0;

                rowsDeleted = mContext.getContentResolver().delete(
                        DBContentProvider.CONTENT_URI_INVITATION,   // the user dictionary content URI
                        selectionClause,
                        selectionArgs// the column to select on
                        // the value to compare to
                );
                Toast.makeText(mContext,"Invitation accepted. Good luck!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, ProfileActivity.class);
                mContext.startActivity(intent);
                ((ProfileActivity)mContext).finish();
            }
        });

        Button btnOdbij =  view.findViewById(R.id.btnOdbij);
        btnOdbij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Brisemo rezervaciju
                String selectionClause = SportCenterSQLiteHelper.COLUMN_ID + " = ?" ;

                String[] selectionArgs = { String.valueOf(mInvitations.get(position).getId()) };
                Uri newUri;
                int rowsDeleted = 0;

                rowsDeleted = mContext.getContentResolver().delete(
                        DBContentProvider.CONTENT_URI_INVITATION,   // the user dictionary content URI
                        selectionClause,
                        selectionArgs// the column to select on
                        // the value to compare to
                );
                Toast.makeText(mContext,"Invitation canceled.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, ProfileActivity.class);
                mContext.startActivity(intent);
                ((ProfileActivity)mContext).finish();
            }
        });


        return view;
    }
}


        /* STARI KOD

        String[] column = {
                SportCenterSQLiteHelper.COLUMN_SPORT_CENTER_ID, SportCenterSQLiteHelper.COLUMN_DATE,
        SportCenterSQLiteHelper.COLUMN_PERIOD};
        String[] column2 = {
                SportCenterSQLiteHelper.COLUMN_NAME};
        String[] column3 = {
                SportCenterSQLiteHelper.COLUMN_USERNAME};
        //sport_center_id of reservation
        Cursor cursor1 = mContext.getContentResolver().query(
                Uri.parse(DBContentProvider.CONTENT_URI_RESERVATION + "/" +
                mInvitations.get(position).getReservationId()),column,null,null,
                null);
        cursor1.moveToFirst();
        //sport_center_name of sport_center
        Cursor cursor2 = mContext.getContentResolver().query(
                Uri.parse(DBContentProvider.CONTENT_URI_SPORT_CENTER + "/" +
                        cursor1.getLong(0)),column2,null,null,
                null);
        nameView.setText( cursor2.getString(0));

        Cursor cursor3 = mContext.getContentResolver().query(
                Uri.parse(DBContentProvider.CONTENT_URI_USER + "/" +
                        mInvitations.get(position).getUserId()),column3,null,null,
                null);

        cursor2.moveToFirst();
        cursor3.moveToFirst();
        friendView.setText( cursor3.getString(0) );
        dateView.setText( cursor1.getString(1) );
        periodView.setText( cursor1.getString(2) );
*/
