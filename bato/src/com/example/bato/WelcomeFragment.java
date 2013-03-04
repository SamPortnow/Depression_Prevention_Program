package com.example.bato;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class WelcomeFragment extends DialogFragment
{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new Builder(getActivity());
		
		builder.setTitle("Welcome!");
		builder.setMessage(R.string.welcome_fragment_message);		
		builder.setPositiveButton(android.R.string.ok, null);
		
		return builder.create();
	}
}
