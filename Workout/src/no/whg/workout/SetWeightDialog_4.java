package no.whg.workout;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import no.whg.workout.MainActivity;

public class SetWeightDialog_4 extends DialogPreference {

	public NumberPicker np_1;
	public NumberPicker np_2;
	public NumberPicker np_3;
	public NumberPicker np_4;
	public NumberPicker np_5;
	
	public SetWeightDialog_4(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.custom_weightset_dialog);
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			// TAKE INFO FROM NUMBERPICKERS AND PUT THEM IN CALC
			break;
		}
		
		// TODO Auto-generated method stub
		super.onClick(dialog, which);
	}
	
	@Override
	protected View onCreateDialogView() {
		// TODO Auto-generated method stub
		View root = super.onCreateDialogView();

		np_1 = (NumberPicker) root.findViewById(R.id.set_weight_np1);
		np_2 = (NumberPicker) root.findViewById(R.id.set_weight_np2);
		np_3 = (NumberPicker) root.findViewById(R.id.set_weight_np3);
		np_4 = (NumberPicker) root.findViewById(R.id.set_weight_np4);
		np_5 = (NumberPicker) root.findViewById(R.id.set_sets_np);
		
		createDefaultNumberPickers();
		
		return root;
	}
	
	public void createDefaultNumberPickers(){
		double currentWeight;
		String[] values = new String[3];
		values[0] = "1";
		values[1] = "3";
		values[2] = "5";
		
		List<Exercise> 	exercises;
		exercises = MainActivity.SLCalc.getBothSessions();
		
		np_1.setMaxValue(1);
		np_1.setMinValue(0);
		np_2.setMaxValue(9);
		np_2.setMinValue(0);
		np_3.setMaxValue(9);
		np_3.setMinValue(0);
		np_4.setMaxValue(9);
		np_4.setMinValue(0);
		np_5.setMaxValue(3);
		np_5.setMinValue(1);
		
		np_5.setDisplayedValues(values);
		
		currentWeight = exercises.get(3).getCurrentWeight();
		convertSingleIntToMulti(currentWeight);
		
		switch ((int)exercises.get(3).getNumberOfSets()) {
		case 1:
			np_5.setValue(1);
			break;
		case 3:
			np_5.setValue(3);
			break;
		case 5:
			np_5.setValue(5);
			break;
		}
	}
	
	public void convertSingleIntToMulti(double original){
		double newInt = original;
		
		newInt = newInt % 1000;
		newInt = newInt / 100;
		np_2.setValue((int)newInt);
		
		newInt = original;
		newInt = newInt % 100;
		newInt = newInt / 10;
		np_2.setValue((int)newInt);
		
		newInt = original;
		newInt = newInt % 10;
		np_3.setValue((int)newInt);
		
		newInt = original;
		newInt = newInt % 1;
		newInt = newInt * 10;
		np_4.setValue((int)newInt);
	}
}