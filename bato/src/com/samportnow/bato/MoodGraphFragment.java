package com.samportnow.bato;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samportnow.bato.database.ThoughtsDataSource;
import com.samportnow.bato.database.dao.ThoughtDao;

public class MoodGraphFragment extends Fragment
{
	private Calendar mChartCalendar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_graph, container, false);
		
		mChartCalendar = Calendar.getInstance();
		
		long ms = mChartCalendar.getTimeInMillis();
		mChartCalendar.setTimeInMillis(ms - (ms % 86400000));

		view.findViewById(R.id.next)
			.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View view)
					{						
						mChartCalendar.add(Calendar.DAY_OF_YEAR, 1);
						refreshChart();
		
					}
				});
		
		view.findViewById(R.id.previous)
			.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						mChartCalendar.add(Calendar.DAY_OF_YEAR, -1);
						refreshChart();
					}
				});

		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		refreshChart();
	}
	
	private void refreshChart()
	{	
		String dateLabel = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(mChartCalendar.getTime());		
		((TextView) getView().findViewById(R.id.date)).setText(dateLabel);
	
		LinearLayout graphLayout = (LinearLayout) getView().findViewById(R.id.graph);
		
		graphLayout.removeAllViews();
		graphLayout.addView(generateChartView());
	}

	private GraphicalView generateChartView()
	{
		long startTimestamp = mChartCalendar.getTimeInMillis();
		long endTimestamp = startTimestamp + 86400000;
		
		ThoughtsDataSource dataSource = new ThoughtsDataSource(getActivity()).open();
		List<ThoughtDao> thoughts = dataSource.getThoughtsBetween(startTimestamp, endTimestamp);
		
		dataSource.close();
		
		XYValueSeries series = new XYValueSeries("Mood by Time");
		
		for (ThoughtDao thought : thoughts)
			series.add((thought.getCreated() % 86400000) / 3600000, thought.getFeeling());
		
		// TODO: for onClick() use SeriesSelection::getPointIndex().

//		if (!calendar.moveToFirst())
//		{
//			Toast.makeText(getActivity(), "No events this day!", Toast.LENGTH_SHORT).show();
//		}
//
//		else
//		{
//			SharedPreferences prefs = getActivity().getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
//			if (prefs.getBoolean("Graph", true) == true)
//			{
//				AlertDialog.Builder builder = new Builder(getActivity());
//				builder.setTitle("Note");
//				builder.setMessage("Click each point to see how what you were doing and what you were thinking are related to how you were feeling");
//				builder.setPositiveButton(android.R.string.ok, null);
//				builder.create();
//				builder.show();
//				prefs.edit().putBoolean("Graph", false).commit();
//			}
//
//		}

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series);
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		double[] limits = new double[] { 1, 24, 0, 7 };
		mRenderer.setPanLimits(limits);
		mRenderer.setPanEnabled(true, false);

		for (int i = 1; i < 26; i++)
		{
			if (i == 1)
			{
				mRenderer.addXTextLabel(i, "12 AM");

			}
			if (i == 4 || i == 7 || i == 10)
			{
				mRenderer.addXTextLabel(i, "" + ((i - 1) + " AM"));
			}

			if (i == 13)
			{
				mRenderer.addXTextLabel(i, "" + (12 + " PM"));
			}

			if (i == 16 || i == 19 || i == 22)
			{
				mRenderer.addXTextLabel(i, "" + ((i - 13) + " PM"));
			}

			if (i == 25)
			{
				mRenderer.addXTextLabel(i, 12 + "AM");
			}

		}

		for (int i = 1; i < 8; i++)
		{
			if (i == 1)
			{
				mRenderer.addYTextLabel(i, "Terrible");
			}

			if (i == 4)
			{
				mRenderer.addYTextLabel(i, "Neutral");
			}

			if (i == 7)
			{
				mRenderer.addYTextLabel(i, "Fantastic");
			}

		}

		mRenderer.setXLabels(0);
		mRenderer.setYLabels(0);
		mRenderer.setLabelsTextSize(10);
		mRenderer.setYLabelsAngle(310);
		mRenderer.setAxesColor(Color.CYAN);
		mRenderer.setXLabelsColor(Color.parseColor("#0099cc"));
		mRenderer.setYLabelsColor(0, Color.parseColor("#0099cc"));
		mRenderer.setYAxisMax(8);
		mRenderer.setYAxisMin(0);
		mRenderer.setXAxisMin(0);
		mRenderer.setXAxisMax(26);
		mRenderer.setPointSize(25);
		mRenderer.setSelectableBuffer(25);
		mRenderer.setClickEnabled(true);
		mRenderer.setXLabelsAngle(45);
		mRenderer.setLabelsTextSize(18);
		mRenderer.setXLabelsPadding(30);
		mRenderer.setTextTypeface(Typeface.DEFAULT_BOLD);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setShowGrid(true);
		mRenderer.setLegendTextSize(20);
		// Customization time for line 1!
		renderer.setColor(Color.GRAY);
		renderer.setLineWidth(3);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);

		// Customization time for line 2!

		final GraphicalView chartView;
		chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);
		
//		chartView.setOnClickListener(new View.OnClickListener()
//		{
//
//			@Override
//			public void onClick(View arg0)
//			{
//				SeriesSelection seriesSelection = chartView.getCurrentSeriesAndPoint();
//				if (seriesSelection != null)
//				{
//
//					mapping = seriesSelection.getXValue();
//					if (mMap.get((float) mapping) != null)
//					{
//						adapter_minutes = mMap.get((float) mapping);
//						Cursor fetchThoughtActivity = mDbHelper.fetchCalendar(Year, Day, adapter_minutes);
//						if (fetchThoughtActivity.moveToFirst())
//						{
//							String thought = fetchThoughtActivity.getString(fetchThoughtActivity.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT));
//							String activity = fetchThoughtActivity.getString(fetchThoughtActivity.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY));
//							AlertDialog.Builder builder = new Builder(getActivity());
//							events = getActivity().getLayoutInflater().inflate(R.layout.dialog_mood, null);
//							builder.setView(events);
//							builder.setTitle("Thought, Activity, and Mood");
//							thoughts = (TextView) events.findViewById(R.id.thought);
//							event = (TextView) events.findViewById(R.id.activity);
//							thoughts.setText(thought);
//							event.setText(activity);
//							builder.setPositiveButton(android.R.string.ok, null);
//							builder.create();
//							builder.show();
//						}
//
//					}
//				}
//			}
//
//		});
		return chartView;
	}

}
