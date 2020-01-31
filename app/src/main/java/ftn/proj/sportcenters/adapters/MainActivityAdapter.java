package ftn.proj.sportcenters.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.model.SportCenter;

public class MainActivityAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<SportCenter> mSportCenters;

    int [] imageIds;
    String[] images;

    Resources resources;
    public MainActivityAdapter(Context mContext, ArrayList<SportCenter> mSportCenters) {
        this.mContext = mContext;
        this.mSportCenters = mSportCenters;


    }

    @Override
    public int getCount() {
        return mSportCenters.size();
    }

    @Override
    public Object getItem(int position) {
        return mSportCenters.get(position);
    }



    @Override
    public long getItemId(int position) {
        return mSportCenters.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_list_item, null);
        }
        else {
            view = convertView;
        }

        TextView nameView = (TextView) view.findViewById(R.id.Name);
        TextView cityView = (TextView) view.findViewById(R.id.City);
        ImageView imageView = (ImageView) view.findViewById(R.id.Image);
        TextView ratingView = (TextView) view.findViewById(R.id.Rating);

        nameView.setText( mSportCenters.get(position).getName() );
        cityView.setText( mSportCenters.get(position).getAddress() );
        ratingView.setText(String.valueOf(mSportCenters.get(position).getRating()));

        imageView.setImageResource(mSportCenters.get(position).getImage());



        return view;
    }
}
