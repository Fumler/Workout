package no.whg.workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import no.whg.workout.MainActivity;
import no.whg.workout.ResetDataAlertDialog;

/*
 * @author Inge Dalby
 */

public class SettingsActivity extends Activity {
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId() == R.id.menu_settings) {
    		startActivity(new Intent(this, SettingsActivity.class));
    	}
    	
    	if(item.getItemId() == R.id.menu_guide) {
    		startActivity(new Intent(this, GuideActivity.class));
    	}
    	
    	if(item.getItemId() == R.id.menu_music) {
    		/*
    		 *  Start the music player
    		 */
    	}
    	
    	if(item.getItemId() == android.R.id.home) {
    		Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
    	}
        return true;
    }
    
    /*
     * Setting up and managing the PreferenceFragment
     */
    public static class SettingsFragment extends PreferenceFragment{
    	ListPreference 			weight;
    	ResetDataAlertDialog 	alertDialog;
    	boolean 				isKG;
    	
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
            
            weight = (ListPreference) findPreference("settings_kgLbs");
            alertDialog = (ResetDataAlertDialog) findPreference("settings_data_id");
            
            setWeightValueFromCalc();
        }
        
        /*
         * (non-Javadoc)
         * @see android.app.Fragment#onResume()
         */
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			setWeightValueFromCalc();
		}

		/*
		 * (non-Javadoc)
		 * @see android.app.Fragment#onPause()
		 */
		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();

			if (weight.getValue().equals("10"))
				isKG = true;
			else if (weight.getValue().equals("20"))
				isKG = false;
			
			if (MainActivity.SLCalc.getWeightUnitKilograms() != isKG)
				MainActivity.SLCalc.changeWeightUnit();
		}
		
		/*
		 * Sets the correct "focus" on the weight ListPreference
		 */
		public void setWeightValueFromCalc() {
            if (MainActivity.SLCalc.getWeightUnitKilograms())
            	weight.setValue("10");
            else
            	weight.setValue("20");
		}
    }
}
