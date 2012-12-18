package no.whg.workout;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class ChangeWeightIncrementDialog_1 extends DialogPreference {

	public NumberPicker np_1;
	
	public ChangeWeightIncrementDialog_1(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setDialogLayoutResource(R.layout.custom_weightincrement_dialog);
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		super.onClick(dialog, which);
		
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			// send shit
			break;
		}
	}

	@Override
	protected View onCreateDialogView() {
		// TODO Auto-generated method stub
		View root = super.onCreateDialogView();
		
		return root;
	}
}

