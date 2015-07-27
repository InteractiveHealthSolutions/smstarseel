package com.ihsinformatics.interactivepaginatortable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class TablePage extends Fragment {
	
	public static final String LAYOUT_ID = "layout_id";
	
	private View rootView;
	private PaginatorTableConfig tableConfig;
	boolean isEven;
	ControlsFactory controlsFactory;
	OnItemClickListener onItemClickListener;
	LinearLayout tableLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		isEven = true;
		controlsFactory = new ControlsFactory(getActivity());
		tableConfig = (PaginatorTableConfig) getArguments().getSerializable("data");
		onItemClickListener = (OnItemClickListener) getArguments().getSerializable("on_item_click_listener");
		ArrayList<String[]> data = (ArrayList<String[]>) getArguments().getSerializable("rows_data");
		
		tableLayout = (LinearLayout) rootView.findViewById(R.id.llMain);
		
		
		for(int i=0; i<data.size(); i++) {
			String[] row = data.get(i);
			tableLayout.addView(buildNewRow(row));
			
		}
		
		this.rootView = rootView;
		return rootView;
	}
	
	private LinearLayout buildNewRow(String[] values) {
		LinearLayout tableRow = new LinearLayout(getActivity());
		tableRow.setOrientation(LinearLayout.HORIZONTAL);
		try {
			tableRow.setWeightSum(tableConfig.getWeightSum());
		} catch (ColumnsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(isEven) {
			tableRow.setBackgroundColor(tableConfig.getStripColor1());
			isEven = false;
		} else {
			tableRow.setBackgroundColor(tableConfig.getStripColor2());
			isEven = true;
		}
		
		tableRow.setPadding(3, 3, 3, 3);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		for(int i=0; i<values.length; i++) {
			TextView tv = controlsFactory.getTextView(values[i], tableConfig.getFontSize(), onItemClickListener);
			tv.setTextColor(tableConfig.getFontColor());
			tv.setTypeface(Typeface.MONOSPACE);
			tv.setPadding(5, 0, 5, 0);
			tableRow.addView(tv,
					new LayoutParams(0, LayoutParams.WRAP_CONTENT, tableConfig.getColumnsWeights()[i]));
			
			
		}
		
		return tableRow;
		
	}
	
	public View getRootView() {
		
		return rootView;
	}
	
	
}
