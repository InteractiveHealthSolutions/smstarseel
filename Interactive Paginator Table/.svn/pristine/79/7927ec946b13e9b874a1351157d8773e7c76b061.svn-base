package com.ihsinformatics.interactivepaginatortable;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ControlsFactory {
	Context context;
	public ControlsFactory(Context context) {
		this.context = context;
	}
	public Button createNewPaginatorButton (int position) {
		Button paginatorButton = new Button(context);
		paginatorButton.setText(position+"");
		
		return paginatorButton;
	}
	
	public TextView getTextView(String text, int fontSize, final OnItemClickListener onItemClickListener) {
		TextView tvColumn = new TextView(context);
		tvColumn.setTextSize(fontSize);
		tvColumn.setText(text);
		/*tvColumn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onItemClickListener.onItemClicked(v, "");
				
			}
		});*/
		
		return tvColumn;
	}
	
	public TextView getHeadingTextView(String text, int fontSize, int fontColor, int layoutWeight, int padding, int gravity) {
		TextView tvColumn = new TextView(context);
		tvColumn.setGravity(gravity);
		tvColumn.setPadding(padding, padding, padding, padding);
		LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		layoutParams.weight = layoutWeight;
		tvColumn.setLayoutParams(layoutParams);
		tvColumn.setTextSize(fontSize);
		tvColumn.setTextColor(fontColor);
		tvColumn.setText(text);
		/*tvColumn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, ((TextView)v).getText(), Toast.LENGTH_SHORT).show();
				
			}
		});*/
		
		return tvColumn;
	}
}
