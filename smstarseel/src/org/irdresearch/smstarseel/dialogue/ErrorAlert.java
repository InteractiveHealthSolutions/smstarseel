package org.irdresearch.smstarseel.dialogue;

import org.irdresearch.smstarseel.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ErrorAlert {
	public static void showError(Context context, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//user shouldnot be able to cancel it with back button
		builder.setCancelable(false);
		builder.setIcon(R.drawable.dialog_error);
		builder.setMessage(message);
		builder.setNegativeButton("OK", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//do nothing
				dialog.dismiss();
			}
		});
		builder.setTitle("Error");
		builder.create().show();
	}

}
