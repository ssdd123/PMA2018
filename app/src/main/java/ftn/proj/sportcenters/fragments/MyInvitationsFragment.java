package ftn.proj.sportcenters.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.adapters.MyInvitationsAdapter;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.Invitation;
import ftn.proj.sportcenters.model.Reservation;

public class MyInvitationsFragment extends Fragment {
    private ArrayList<Invitation> myInvitations = new ArrayList<Invitation>();
    private ListView mListView;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private String loggedUsername;
    private long loggedId;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_invitations_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        mListView=view.findViewById(R.id.MyInvitationsList);
        loadItems(myInvitations);

        MyInvitationsAdapter adapter = new MyInvitationsAdapter(this.getContext(), myInvitations);
        mListView.setAdapter(adapter);
    }


    private void loadItems(ArrayList<Invitation> myInvitations) {
        myInvitations = new ArrayList<>();

        String[] columns = {SportCenterSQLiteHelper.COLUMN_ID, SportCenterSQLiteHelper.COLUMN_USER_ID,
                SportCenterSQLiteHelper.COLUMN_RESERVATION_ID, SportCenterSQLiteHelper.COLUMN_ACCEPTED};
        Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_INVITATION,
                columns, null, null,null);

        while (cursor.moveToNext()) {

            getInvitations(cursor);
        }
    }


    private void getInvitations(Cursor cursor){

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loggedId = sharedPreferences.getLong("id",0L);

        Invitation sc = new Invitation();

        sc.setId(cursor.getLong(0));
        sc.setUserId(cursor.getLong(1));
        sc.setReservationId(cursor.getLong(2));
        sc.setAccepted(cursor.getInt(3));

        if(sc.getUserId() == loggedId){
            myInvitations.add(sc);
        }
    }
}
