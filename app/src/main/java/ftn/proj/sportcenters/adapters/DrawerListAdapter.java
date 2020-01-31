package ftn.proj.sportcenters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ftn.proj.sportcenters.R;

public class DrawerListAdapter extends BaseAdapter {
	 
    Context mContext;
    ArrayList<String> mNavItems;
 
    public DrawerListAdapter(Context context, ArrayList<String> navItems) {
        mContext = context;
        mNavItems = navItems;
    }
 
    @Override
    public int getCount() {
        return mNavItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
 
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_list_item, null);
        }
        else {
            view = convertView;
        }
 
        TextView navItemView = (TextView) view.findViewById(R.id.NavItem);
        navItemView.setText( mNavItems.get(position) );
 
        return view;
    }
}
