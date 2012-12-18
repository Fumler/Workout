package no.whg.workout;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class ChangeWeightIncrementDialog_1 extends DialogPreference {

	public ChangeWeightIncrementDialog_1(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		super.onClick(dialog, which);
		
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			// 
			break;

		}
	}
	
}

