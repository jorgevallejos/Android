package be.cegeka.android.dwaaldetectie.view.listeners;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogDismissListener implements AlertDialog.OnClickListener
{
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		dialog.dismiss();
	}
}