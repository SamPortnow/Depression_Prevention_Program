package com.samportnow.bato;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.samportnow.bato.coping.blowfish.Blowfish;
import com.samportnow.bato.coping.floating.FloatActivity;
import com.samportnow.bato.coping.rollingball.Rolling;
import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.ThoughtDao;

public class CopingFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_coping, container, false);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = getView();		
		
		Button breath_learn = (Button) view.findViewById(R.id.breathing_learn);
		breath_learn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
			      Builder builder = new AlertDialog.Builder(getActivity());
			      builder.setTitle("Slow breathing");
			      builder.setMessage("Slow breathing can be helpful. Breathe along with the blowfish in this activity.");
			      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) 
			           {
			        	  dialog.dismiss();
			           }
			       });
			      AlertDialog dialog = builder.create();
			      dialog.show();				
			}
			
		});
		Button breath_play = (Button) view.findViewById(R.id.breathing_play);
		Button rolling_learn = (Button) view.findViewById(R.id.rolling_learn);
		rolling_learn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
			      Builder builder = new AlertDialog.Builder(getActivity());
			      builder.setTitle("Rolling ball");
			      builder.setMessage("Sometimes it\'s helpful to just take your mind off things. Focus on capturing the rings with the rolling ball by tilting your phone in this activity.");
			      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) 
			           {
			        	  dialog.dismiss();
			           }
			       });
			      AlertDialog dialog = builder.create();
			      dialog.show();				
			}
			
		});
		Button rolling_play = (Button) view.findViewById(R.id.rolling_play);
		Button float_learn = (Button) view.findViewById(R.id.float_learn);
		float_learn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
			      Builder builder = new AlertDialog.Builder(getActivity());
			      builder.setTitle("Floating thoughts");
			      builder.setMessage("Sometimes it is helpful to try to look at your thoughts without judging them. In this activity, just watch your thoughts float by");
			      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) 
			           {
			        	  dialog.dismiss();
			           }
			       });
			      AlertDialog dialog = builder.create();
			      dialog.show();				
			}
			
		});
		Button float_play = (Button) view.findViewById(R.id.float_play);
		breath_play.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(getActivity(), Blowfish.class);
				startActivity(intent);

			}
			
		});
		
		
		rolling_play.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(getActivity(), Rolling.class);
				startActivity(intent);
			}
			
		});
		
		float_play.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
				ThoughtDao thought = dataSource.getRandomThought();		
				dataSource.close();
				if (thought != null)
				{
					Intent intent = new Intent(getActivity(), FloatActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(getActivity().getApplicationContext(), "Whoops! You need some thoughts before you use activity!", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		}
		
	
}
	
	

