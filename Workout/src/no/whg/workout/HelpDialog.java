package no.whg.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class HelpDialog extends DialogFragment {
	public int fragment;
	private String string;
	
	public HelpDialog(int fragment){
		this.fragment = fragment;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        switch(fragment){
	        case 0: string = getString(R.string.dialog_help0);
	        		break;
	        case 1: string = getString(R.string.dialog_help1);
	        		break;
	        case 2: string = getString(R.string.dialog_help2);
	        		break;
	        case 3: string = getString(R.string.dialog_help3);
	        		break;
        }
        
        builder.setMessage(string)
               .setNegativeButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
