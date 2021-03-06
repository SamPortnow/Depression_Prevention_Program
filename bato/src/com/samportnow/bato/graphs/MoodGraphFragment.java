package com.samportnow.bato.graphs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samportnow.bato.R;
import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.ThoughtDao;

public class MoodGraphFragment extends Fragment
{
	private Calendar mChartCalendar;
	private List<ThoughtDao> mThoughts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_mood_graph, container, false);
		
		mChartCalendar = Calendar.getInstance();
		
		// TODO: GMT and TimeZone are not working properly.
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

	private View generateChartView()
	{
		long startTimestamp = mChartCalendar.getTimeInMillis();
		long endTimestamp = startTimestamp + 86400000;
		
		BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		mThoughts = dataSource.getThoughtsBetween(startTimestamp, endTimestamp);
		
		dataSource.close();
		
		if (mThoughts.isEmpty())
			return getActivity().getLayoutInflater().inflate(R.layout.block_no_thoughts, (ViewGroup) getView(), false);						
		
		Calendar calendar = Calendar.getInstance();
		XYValueSeries series = new XYValueSeries("Mood by Time");
		
		for (ThoughtDao thought : mThoughts)
		{
			calendar.setTimeInMillis(thought.getCreated());
			double xValue = calendar.get(Calendar.HOUR_OF_DAY) + ((double) calendar.get(Calendar.MINUTE) / 60);
			
			series.add(xValue, thought.getFeeling());
		}
		
		XYMultipleSeriesDataset seriesDataset = new XYMultipleSeriesDataset();
		seriesDataset.addSeries(series);
		
		XYMultipleSeriesRenderer multipleSeriesRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
		
		multipleSeriesRenderer.addSeriesRenderer(seriesRenderer);
		
		for (int i = 0; i <= 24; i++)
		{
			if (i % 3 == 0)
				multipleSeriesRenderer.addXTextLabel(i, "" + i);
		}
		
		multipleSeriesRenderer.setXAxisMin(-2);
		multipleSeriesRenderer.setXAxisMax(26);
		
		multipleSeriesRenderer.addYTextLabel(0, "Terrible");
		multipleSeriesRenderer.addYTextLabel(3, "Neutral");
		multipleSeriesRenderer.addYTextLabel(6, "Fantastic");
		
		multipleSeriesRenderer.setYAxisMin(-0.5);
		multipleSeriesRenderer.setYAxisMax(6.5);	

		multipleSeriesRenderer.setXLabels(0);
		multipleSeriesRenderer.setYLabels(0);
		
		multipleSeriesRenderer.setPanEnabled(true, true);
		
		// TODO: move me somewhere better.
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);		

		multipleSeriesRenderer.setLabelsTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, metrics));
		
		multipleSeriesRenderer.setXLabelsAngle(45);
		multipleSeriesRenderer.setYLabelsAngle(310);
		
		multipleSeriesRenderer.setXLabelsColor(Color.BLACK);
		multipleSeriesRenderer.setYLabelsColor(0, Color.BLACK);
		
		multipleSeriesRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));		
		multipleSeriesRenderer.setAxesColor(Color.BLACK);		
		multipleSeriesRenderer.setGridColor(Color.LTGRAY);		
		
		multipleSeriesRenderer.setPointSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics));
		multipleSeriesRenderer.setSelectableBuffer(25);
		multipleSeriesRenderer.setClickEnabled(true);
		
		multipleSeriesRenderer.setTextTypeface(Typeface.SANS_SERIF);

		multipleSeriesRenderer.setShowGrid(true);
		multipleSeriesRenderer.setShowCustomTextGrid(true);		
		multipleSeriesRenderer.setShowLegend(false);
		
		int margin30 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, metrics);
		int margin10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
		
		multipleSeriesRenderer.setMargins(new int[] {margin30, margin30, margin10, margin10});

		seriesRenderer.setColor(Color.GRAY);
		seriesRenderer.setLineWidth(3);
		seriesRenderer.setPointStyle(PointStyle.CIRCLE);
		seriesRenderer.setFillPoints(true);

		final View view = ChartFactory.getLineChartView(getActivity(), seriesDataset, multipleSeriesRenderer);
		
		view.setOnClickListener(
			new View.OnClickListener()
			{				
				@Override
				public void onClick(View v)
				{
					SeriesSelection selection = ((GraphicalView) view).getCurrentSeriesAndPoint();
					
					if (selection == null)
						return;
					
					ThoughtDao thought = mThoughts.get(selection.getPointIndex());
					
					// TODO: make me a prettier layout.
					View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_thought_detail, null);
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(thought.getCreated());								
					
					((TextView) view.findViewById(R.id.activity)).setText(thought.getActivity());
					((TextView) view.findViewById(R.id.thought)).setText(thought.getContent());
					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					
					builder.setView(view);
					builder.setPositiveButton(android.R.string.ok, null);
					
					builder.create().show();
				}
			});
		
		return view;
	}

}
