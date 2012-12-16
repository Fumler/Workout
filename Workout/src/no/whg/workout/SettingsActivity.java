package no.whg.workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import no.whg.workout.MainActivity;

/*
 * @author Inge Dalby
 */

public class SettingsActivity extends Activity {

	public boolean isKG;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        /*
         *  Initiating button elements.
         */
        Button settings_kgBtn 	= (Button) findViewById(R.id.settings_kgBtn);
        Button settings_lbsBtn 	= (Button) findViewById(R.id.settings_lbsBtn);
        Button settings_doneBtn = (Button) findViewById(R.id.settings_doneBtn);
        
        /*
         *  Sets up the buttons so that they can be focused.
         */
        settings_kgBtn.setFocusable(true);
        settings_kgBtn.setFocusableInTouchMode(true);
        settings_lbsBtn.setFocusable(true);
        settings_lbsBtn.setFocusableInTouchMode(true);
        
        /*
         *  Checks for which unit of measurement is active and focuses the correct button.
         */
        if (MainActivity.SLCalc.getWeightUnitKilograms()){
        	settings_kgBtn.requestFocus();
        	setIsKG(true);
        }
        else{
        	settings_lbsBtn.requestFocus();
        	setIsKG(false);
        }
        
        /* 
         * TODO:
         * - Set up a button that resets the application to default state.
         * 
         * - Set up a way for the user to change values in each exercise, reps etc.
         */ 
        
        /*
         *  Saves the settings and exits the activity
         */
        settings_doneBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveSettings();
			}
        });
    }
    
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
     *  Setter for isKG.
     */
    private void setIsKG(boolean value){
    	isKG = value;
    }
    
    /*
     *  Saves the settings and exits the activity.
     */
    private void saveSettings(){
		Toast.makeText(getApplicationContext(), getResources().getString(R.string.set_saved), Toast.LENGTH_SHORT).show();
		
		finish();
    }
}
