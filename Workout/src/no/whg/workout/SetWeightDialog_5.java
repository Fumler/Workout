package no.whg.workout;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import no.whg.workout.MainActivity;

public class SetWeightDialog_5 extends DialogPreference {

	public NumberPicker np_1;
	public NumberPicker np_2;
	public NumberPicker np_3;
	public NumberPicker np_4;
	public NumberPicker np_5;
	public NumberPicker np_6;
	
	public SetWeightDialog_5(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.custom_weightset_dialog);
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			int np_6_value = np_6.getValue();
			
			if (np_6_value == 1)
				np_6_value = 1;
			else if (np_6_value == 2)
				np_6_value = 3;
			else if (np_6_value == 3)
				np_6_value = 5;
			
			MainActivity.SLCalc.getBothSessions().get(3).setCurrentWeight(convertMultiIntToSingle());
			MainActivity.SLCalc.getBothSessions().get(3).setNumberOfSets(np_6_value);
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
		np_5 = (NumberPicker) root.findViewById(R.id.set_weight_np5);
		np_6 = (NumberPicker) root.findViewById(R.id.set_sets_np);
		
		createDefaultNumberPickers();
		
		return root;
	}
	
	public void createDefaultNumberPickers(){
		double currentWeight;
		String[] values = new String[3];
		values[0] = "1";
		values[1] = "3";
		values[2] = "5";
		
		String[] values2 = new String[2];
		values2[0] = "0";
		values2[1] = "5";
		
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
		np_5.setMaxValue(2);
		np_5.setMinValue(1);
		np_6.setMaxValue(3);
		np_6.setMinValue(1);
		
		np_5.setDisplayedValues(values2);
		
		np_6.setDisplayedValues(values);
		
		currentWeight = exercises.get(3).getCurrentWeight();
		convertSingleIntToMulti(currentWeight);
		
		switch ((int)exercises.get(3).getNumberOfSets()) {
		case 1:
			np_6.setValue(1);
			break;
		case 3:
			np_6.setValue(3);
			break;
		case 5:
			np_6.setValue(5);
			break;
		}
	}
	
	public void convertSingleIntToMulti(double original){
		double newInt = original;
		
		newInt = newInt % 1000;
		newInt = newInt / 100;
		np_1.setValue((int)newInt);
		
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
		
		newInt = original;
		newInt = newInt % 0.1;
		newInt = newInt * 100;
		
		if ((int)newInt > 5)
			np_5.setValue(0);
		else
			np_5.setValue(5);
	}
	
	public double convertMultiIntToSingle(){
		double finishedMerge = 0.0d;
		double np_5_value = 0.0d;
		
		// Sets the correct fraction according to the chosen value
		if (np_5.getValue() > 0)
			np_5_value = 0.05;
		else
			np_5_value = 0.00;
		
		finishedMerge += (np_5_value);
		finishedMerge += ((double)(np_4.getValue()) / 10);
		finishedMerge += (np_3.getValue());
		finishedMerge += (np_2.getValue() * 10);
		finishedMerge += (np_1.getValue() * 100);
		
		return finishedMerge;
	}
}