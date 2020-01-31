package ftn.proj.sportcenters.activities;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.RequiresApi;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import ftn.proj.sportcenters.R;


public class PreferenceActivity extends android.preference.PreferenceActivity {
	
	@SuppressWarnings("deprecation")

	public WindowManager wm;
	private ListPreference listPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);



		//Toast.makeText(getApplicationContext(), currentFontValue, Toast.LENGTH_SHORT).show();

		/*fontGroup.findIndexOfValue(new RadioGroup.OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				String size = "Medium";
				// This will get the radiobutton that has changed in its check state
				RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
				// This puts the value (true/false) into the variable
				boolean isChecked = checkedRadioButton.isChecked();
				// If the radiobutton that has changed in check state is now checked...
				if (isChecked)
				{
					size = checkedRadioButton.getText().toString();
					Toast.makeText(getApplicationContext(),  checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();

				}
			}
		});
*/


//		addPreferencesFromResource(R.xml.preferences);
		getFragmentManager().beginTransaction().replace(android.R.id.content,
				new PrefsFragment()).commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button

	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@SuppressLint("ValidFragment")
	public  class PrefsFragment extends PreferenceFragment {
		String currentFontValue= "Medium";
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);



			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);

			currentFontValue= "Medium";
			//final ListPreference lp = (ListPreference) findPreference("pref_font_key");
			final ListPreference lp = (ListPreference) this.findPreference(this.getString(R.string.pref_font_key));
			lp.getValue();
			lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@RequiresApi(api = Build.VERSION_CODES.M)
				public boolean onPreferenceChange(Preference preference, Object newValue) {

					int index = lp.findIndexOfValue(newValue.toString());

					if (index != -1) {
						String size = (String) lp.getEntries()[index];
						Toast.makeText(getContext(), size, Toast.LENGTH_LONG).show();
						adjustFontScale(getResources().getConfiguration(),size);
					}
					return true;
				}
			});


		}

		@RequiresApi(api = Build.VERSION_CODES.M)
		public void adjustFontScale(Configuration configuration, String size)
		{
			if(size.equals("Small")){
				configuration.fontScale = (float) 0.8;
			}else if(size.equals("Large")){
				configuration.fontScale = (float) 1.1;
			}else{
				configuration.fontScale = (float) 1.0;
			}

			DisplayMetrics metrics = getResources().getDisplayMetrics();
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(metrics);
			metrics.scaledDensity = configuration.fontScale * metrics.density;
			getContext().getResources().updateConfiguration(configuration, metrics);
		}

	}


}
