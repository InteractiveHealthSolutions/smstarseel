package org.irdresearch.smstarseel.dialogue;

import org.irdresearch.smstarseel.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ConfirmPopup {
	
	public ConfirmPopup(Context context, String message, OnClickListener onOkClickedListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//user shouldnot be able to cancel it with back button
		builder.setCancelable(false);
		builder.setIcon(R.drawable.question_mark);
		builder.setMessage(message);
		builder.setNegativeButton("Cancel", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//do nothing
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("Ok", onOkClickedListener);
		builder.setTitle("Alert");
		builder.create().show();
	}
}
