package no.whg.workout;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import no.whg.workout.MainActivity;

public class ChangeWeightIncrementDialog_5 extends DialogPreference {

	public NumberPicker np_1;
	double currentIncrement = 0;
	
	public ChangeWeightIncrementDialog_5(Context context, AttributeSet attrs) {
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
			
			if (np_1.getValue() == 0)
				currentIncrement = 2.5;
			else
				currentIncrement = 5.0;
			
			MainActivity.SLCalc.getBothSessions().get(3).setWeightIncrement(currentIncrement);
			break;
		}
	}

	@Override
	protected View onCreateDialogView() {
		// TODO Auto-generated method stub
		View root = super.onCreateDialogView();
		
		np_1 = (NumberPicker) root.findViewById(R.id.cwid_np1);
		
		createDefaultNumberPickers();
		
		return root;
	}
	
	public void createDefaultNumberPickers(){
		String[] values = new String[2];
		
		List<Exercise> 	exercises;
		exercises = MainActivity.SLCalc.getBothSessions();
		
		values[0] = "2.5";
		values[1] = "5.0";
		
		np_1.setMaxValue(1);
		np_1.setMinValue(0);
		
		np_1.setDisplayedValues(values);
		
		currentIncrement = exercises.get(3).getWeightIncrement();
		
		if (currentIncrement == 2.5)
			np_1.setValue(0);
		else
			np_1.setValue(1);
	}
}

