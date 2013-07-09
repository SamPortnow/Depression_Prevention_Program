package com.samportnow.bato;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class WelcomeFragment extends DialogFragment implements OnShowListener, OnClickListener
{
	EditText mUsernameEditText = null;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new Builder(getActivity());
		
		builder.setTitle(R.string.welcome_title);
		builder.setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome, null));		
		builder.setPositiveButton(android.R.string.ok, this);
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(this);
		
		return dialog;
	}

	@Override
	public void onShow(DialogInterface dialog)
	{
		AlertDialog alertDialog = (AlertDialog) dialog;
		
		final Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		
		mUsernameEditText = (EditText) alertDialog.findViewById(R.id.setup_username);
		mUsernameEditText.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s)
			{								
				okButton.setEnabled(mUsernameEditText.getText().toString().length() > 0);				 
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			
			}
		});
		
		okButton.setEnabled(mUsernameEditText.getText().toString().length() > 0);
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		if (which == DialogInterface.BUTTON_POSITIVE)
		{		
			SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
			preferences.edit().putString("username", mUsernameEditText.getText().toString()).commit();
		}
	}
}
