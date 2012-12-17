package no.whg.workout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ThreeStateCheckbox extends Button {
	
	public ThreeStateCheckbox(Context context) {
		super(context);
		
		_state = 0;
		
	}
	
	public ThreeStateCheckbox(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		_state = 0;
		
	}
	
	public ThreeStateCheckbox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		_state = 0;
		
	}

	private int _state;
	
	private static final int[] UNCHECKED = { R.attr.unchecked };
	
	private static final int[] CHECKED = { R.attr.checked };
	
	private static final int[] CROSSED = { R.attr.crossed };
	
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		
		final int[] drawableState = super.onCreateDrawableState(extraSpace +3);
		
		if(_state == 0) {
			mergeDrawableStates(drawableState, UNCHECKED);
			
		} else if(_state == 1) {
			mergeDrawableStates(drawableState, CHECKED);
		} else if(_state == 2) {
			mergeDrawableStates(drawableState, CROSSED);
		}
		return drawableState;
	}
	
	@Override
	public boolean performClick() {
		nextState();
		
		return super.performClick();
	}
	
	public void setState(int state) {
		if((state > -1) && (state < 3)) {
			_state = state;
			setButtonText();
		}
	}
	
	public int getState() {
		return _state;
	}
	
	public void nextState() {
		_state++;
		
		if(_state > 2) {
			_state = 0;
			
		}
		
		setButtonText();
	}
	
	public void previousState() {
		_state--;
		
		if(_state < 0) {
			_state = 2;
		}
		setButtonText();
	}
	
	private void setButtonText()
	{
		switch(_state) {
		case 0:
			this.setBackgroundResource(R.drawable.unchecked);
			break;
		case 1: 
			this.setBackgroundResource(R.drawable.checked);
			break;
		case 2:
			this.setBackgroundResource(R.drawable.crossed);
		default: 
			
			break;	
		}
	}
	
}
