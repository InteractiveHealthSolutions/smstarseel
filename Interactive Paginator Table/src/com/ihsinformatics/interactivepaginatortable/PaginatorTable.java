package com.ihsinformatics.interactivepaginatortable;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaginatorTable extends LinearLayout {

	OnItemClickListener onItemClickListener;
	ViewPager viewPager;
	LinearLayout llpaginatorButtons;
	List<Button> paginatorButtons;
	List<TablePage> fragments;
	LinearLayout llheading;
	HorizontalScrollView hcvPaginatorButtons;
	
	Context context;
	
	public PaginatorTable(Context context) {
		super(context);
	}
	
	public PaginatorTable(Context context, PaginatorTableConfig tableConfig, OnItemClickListener onItemClickListener) {
		this(context);
		this.onItemClickListener = onItemClickListener;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    
		addView(layoutInflater.inflate(R.layout.layout_custom_table, this, false));
		
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.context = context;
		
		viewPager = (ViewPager) findViewById(R.id.vpData);
		hcvPaginatorButtons = (HorizontalScrollView) findViewById(R.id.scPaginatorButtons);
		llpaginatorButtons = (LinearLayout) findViewById(R.id.llPaginatorButtons);
		llheading = (LinearLayout) findViewById(R.id.llHeading);
		
		init(tableConfig);
		
	}
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	
	public void init(PaginatorTableConfig tableConfig) {
		ControlsFactory controlsFactory = new ControlsFactory(context);
		
		paginatorButtons = new ArrayList<Button>();
		
		int numberOfRowsPerPage = tableConfig.getNumberOfRowsPerPage();
		int pageTransformationStyle = tableConfig.getPageTransformationStyle();
		String[] columnHeading = tableConfig.getColumnHeadings();
		ArrayList<String[]> data = tableConfig.getData();
		int[] columnWeights = tableConfig.getColumnsWeights();
		
		
		try {
			llheading.setWeightSum(tableConfig.getWeightSum());
		} catch (ColumnsException e) {
			llheading.setWeightSum(columnHeading.length);
			e.printStackTrace();
		}
		llheading.setBackgroundColor(tableConfig.getHeadingRowColor());
		TextView tvTemp;
		for(int i =0; i<columnHeading.length;  i++) {
			tvTemp = controlsFactory.getHeadingTextView(columnHeading[i], tableConfig.getHeadingFontSize(), tableConfig.getHeadingFontColor(), columnWeights[i], 3, Gravity.CENTER);
			tvTemp.setTypeface(Typeface.MONOSPACE);
			llheading.addView(tvTemp);
		}
		
		if(pageTransformationStyle == TRANSFORMATION_STYLE_DEPTH_PAGE) {
			viewPager.setPageTransformer(true, new DepthPageTransformer());
		} else if(pageTransformationStyle == TRANSFORMATION_STYLE_ZOOM_OUT) {
			viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		}
		
		fragments = new ArrayList<TablePage>();
		// ControlsFactory paginatorButtonFactory = new ControlsFactory(context);
		
		for(int i=0; i<data.size(); i+=numberOfRowsPerPage) {
			Button paginatorButton = controlsFactory.createNewPaginatorButton((i/numberOfRowsPerPage)+1);
			paginatorButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					viewPager.setCurrentItem(Integer.parseInt(((Button)v).getText().toString())-1);
				}
			});
			paginatorButtons.add(paginatorButton);
			llpaginatorButtons.addView(paginatorButton);
			
			
			ArrayList<String[]> tempData = new ArrayList<String[]>();
			
			if (i+numberOfRowsPerPage < data.size()) {
				tempData.addAll(data.subList(i, i + (numberOfRowsPerPage)));
			} else {
				tempData.addAll(data.subList(i, data.size()));
			}
			
			Bundle bundle = new Bundle();
			bundle.putSerializable("rows_data", tempData);
			bundle.putSerializable("data", tableConfig);
			bundle.putSerializable("on_item_click_listener", onItemClickListener);
			
			TablePage tempFragment = new TablePage();
			tempFragment.setArguments(bundle);
			fragments.add(tempFragment);
			
		}
		if(paginatorButtons.size()<=1) {
			hcvPaginatorButtons.setVisibility(View.GONE);
		}
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
				for(Button paginatorButton:paginatorButtons) {
					if(arg0 == paginatorButtons.indexOf(paginatorButton)) {
						paginatorButton.setBackgroundColor(Color.GRAY);
						hcvPaginatorButtons.scrollTo((int)paginatorButton.getX(), 0);
					} else {
						paginatorButton.setBackgroundColor(Color.LTGRAY);
					}
				}
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		viewPager.setAdapter(new SectionsPagerAdapter(((FragmentActivity)context).getSupportFragmentManager()));
		
		
	}
	
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			return null;
		}
	}
	
	public static final int TRANSFORMATION_STYLE_ZOOM_OUT = 8;
	public static final int TRANSFORMATION_STYLE_DEPTH_PAGE = 4;
	public static final int TRANSFORMATION_STYLE_DEFAULT = 0;

}
