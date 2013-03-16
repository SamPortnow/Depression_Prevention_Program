package com.example.bato;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class WelcomeFragment extends DialogFragment
{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new Builder(getActivity());
		
		builder.setTitle("Welcome!");
		builder.setMessage(R.string.welcome_fragment_message);		
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) 
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);            
                fragmentTransaction.replace(R.id.fragment_container, new Setup());
                fragmentTransaction.commit();
            }
        });
		return builder.create();
	}

}
