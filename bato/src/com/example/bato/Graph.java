package com.example.bato;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Graph extends Fragment
{
	Context mContext;
	private CalendarDbAdapter mDbHelper;
	long Day;
	long Year;
	float Hour;
    ArrayList<Float> minutes = new ArrayList<Float>();
    ArrayList<Integer> mood = new ArrayList<Integer>();
    Calendar cal = Calendar.getInstance();
    long Add;
    GraphicalView chartView;
    LinearLayout layout;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		mContext = this.getActivity();
	    mDbHelper=new CalendarDbAdapter(mContext);
	    mDbHelper.open();
	    View view = inflater.inflate(R.layout.activity_graph, container, false);
		Button next = (Button) view.findViewById(R.id.next);
		Button previous = (Button) view.findViewById(R.id.previous);
	    Day = cal.get(Calendar.DAY_OF_YEAR);
	    Year = cal.get(Calendar.YEAR);
	    chartView = generate(Day, Year);
		layout = (LinearLayout) view.findViewById(R.id.graph);	
		layout.addView(chartView);

	    next.setOnClickListener(new OnClickListener()
	    {

			@Override
			public void onClick(View arg0) 
			{
				Day++;
				if (Day > 365)
				{
					Day = 0;
					Year++;
				}
				layout.removeView(chartView);
				chartView = generate(Day, Year);
		    	layout.addView(chartView);
			}
	    });
	    previous.setOnClickListener(new OnClickListener()
	    {

			public void onClick(View arg0) 
			{
		    	Day--;
				if (Day < 0)
				{
					Day = 0;
					Year--;
				}
				layout.removeView(chartView);
		    	chartView = generate(Day, Year);
		    	layout.addView(chartView);
			}
	    	
	    });
	    
	    return view;

		}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mDbHelper.close();
	}
	
	public GraphicalView generate(long Day, long Year)
	{
	    Cursor calendar = mDbHelper.fetchDay(Year, Day);
	    Calendar cal = Calendar.getInstance();  
	    cal.set(Calendar.YEAR, (int)Year);  
	    cal.set(Calendar.DAY_OF_YEAR, (int)Day);  
	    Log.e("Day is",""+Day);
	    Date date = cal.getTime();  
	    String sDate = new SimpleDateFormat("MM/dd/yyyy").format(date);  
	    minutes.clear();
	    mood.clear();
	    if (calendar.moveToFirst())
	    {
	    	while (calendar.moveToNext())
	    	{
	    		Hour = Float.valueOf((calendar.getInt(calendar.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_MINUTES))))/60;
	    		minutes.add(Hour);
	    		mood.add(calendar.getInt(calendar.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_FEELING)));
	    	}
	    }
	    
	    else
	    {
	    	Toast.makeText(mContext, "No events this day!", Toast.LENGTH_SHORT).show();
	    }
		
		XYValueSeries series = new XYValueSeries("Mood by Time"); 
		if (minutes.size() > 0)
		{
			for( int i = 0; i < minutes.size(); i++)
			{
				series.add(minutes.get(i), mood.get(i));
			}
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
		XYSeriesRenderer renderer = new XYSeriesRenderer();// This will be used to customize line 1
		mRenderer.addSeriesRenderer(renderer);
		double[] limits = new double [] {0, 23, 0, 7};
		mRenderer.setPanLimits(limits);
		mRenderer.setPanEnabled(true, false);
		for (int i = 0; i < 23; i++)
		{
			if (i == 0)
			{
				mRenderer.addXTextLabel(i, ""+(i+12)+" AM");

			}
			if (i > 0 && i < 12)
			{
				mRenderer.addXTextLabel(i, ""+(i+" AM"));
			}
			
			if (i == 12)
			{
				mRenderer.addXTextLabel(i, ""+(i +" PM"));
			}
			
			if (i > 12 && i < 24)
			{
				mRenderer.addXTextLabel(i, ""+((i -12 ) +" PM"));
			}
		}
		
		for (int i = 0; i < 7; i++)
		{
			if (i == 0)
			{
			mRenderer.addYTextLabel(i, "Terrible");
			}
			
			if (i == 3)
			{
				mRenderer.addYTextLabel(i, "Neutral");
			}
			
			if (i == 6)
			{
				mRenderer.addYTextLabel(i, "Fantastic");
			}
			
		}
		mRenderer.setYAxisMax(7);
		mRenderer.setYAxisMin(0);
		mRenderer.setXAxisMax(23);
		mRenderer.setPointSize(5);
		mRenderer.setChartTitle(sDate);
		// Customization time for line 1!
		renderer.setColor(Color.BLUE);
		renderer.setLineWidth(3);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		
		// Customization time for line 2!

		GraphicalView chartView;
		
		chartView = ChartFactory.getLineChartView(mContext, dataset, mRenderer);
		return chartView;
	
	}
	
	
}

