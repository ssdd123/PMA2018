package ftn.proj.sportcenters.tools;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import ftn.proj.sportcenters.R;


public class FragmentTransition
{
	public static void to(Fragment newFragment, FragmentActivity activity)
	{
		to(newFragment, activity, true);
	}
	
	public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack)
	{
		FragmentTransaction transaction = activity.getSupportFragmentManager()
			.beginTransaction()
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
			.replace(R.id.SportCenterList, newFragment);
		if(addToBackstack) transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public static void remove(Fragment fragment, FragmentActivity activity)
	{
		activity.getSupportFragmentManager().popBackStack();
	}
}
