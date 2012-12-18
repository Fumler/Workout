package no.whg.workout;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import no.whg.workout.MainActivity;

/*
 * Custom dialog class used in when user presses the 
 *  reset data preference
 */
public class ResetDataAlertDialog extends DialogPreference {
	
	public ResetDataAlertDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.custom_resetdata_dialog);
		
		setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE: // OK
			MainActivity.SLCalc.resetExercisesToDefault();
			MainActivity.setResetPressed(true);
			break;
		}
		
		// TODO Auto-generated method stub
		super.onClick(dialog, which);
	}

	@Override
	protected View onCreateDialogView() {
		// TODO Auto-generated method stub
		View root = super.onCreateDialogView();		
		return root;
	}
}