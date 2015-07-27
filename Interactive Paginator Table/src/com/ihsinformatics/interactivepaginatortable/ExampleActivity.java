package com.ihsinformatics.interactivepaginatortable;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ExampleActivity extends FragmentActivity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout llMain = (LinearLayout) findViewById(R.id.llMain);
		
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"Aslam", "Karachi"});
		list.add(new String[]{"Shehzad", "Islamabad"});
		list.add(new String[]{"Rameez", "Lahore"});
		list.add(new String[]{"Waqas", "Karachi"});
		list.add(new String[]{"Adil", "Multan"});
		list.add(new String[]{"Ubaid", "Karachi"});
		list.add(new String[]{"Asif", "Islamabad"});
		list.add(new String[]{"Waseem", "Karachi"});
		list.add(new String[]{"Junaid", "Karachi"});
		list.add(new String[]{"Liaquat", "Nawabshah"});
		list.add(new String[]{"Zubair", "Karachi"});
		list.add(new String[]{"Talha", "Mirpurkhas"});
		list.add(new String[]{"Hamza", "Karachi"});
		list.add(new String[]{"Hasan", "Lahore"});
		list.add(new String[]{"Jhangir", "Lahore"});
		list.add(new String[]{"Taha", "Multan"});
		list.add(new String[]{"Farooq", "Karachi"});
		list.add(new String[]{"Shabbir", "Sialkot"});
		list.add(new String[]{"Mujeeb", "Karachi"});
		list.add(new String[]{"Nadeem", "Lahore"});
		list.add(new String[]{"Zeeeshan", "Karachi"});
		list.add(new String[]{"Nauman", "Karachi"});
		list.add(new String[]{"Aslam", "Karachi"});
		list.add(new String[]{"Shehzad", "Islamabad"});
		list.add(new String[]{"Rameez", "Lahore"});
		list.add(new String[]{"Waqas", "Karachi"});
		list.add(new String[]{"Adil", "Multan"});
		list.add(new String[]{"Ubaid", "Karachi"});
		list.add(new String[]{"Asif", "Islamabad"});
		list.add(new String[]{"Waseem", "Karachi"});
		list.add(new String[]{"Junaid", "Karachi"});
		list.add(new String[]{"Liaquat", "Nawabshah"});
		list.add(new String[]{"Zubair", "Karachi"});
		list.add(new String[]{"Talha", "Mirpurkhas"});
		list.add(new String[]{"Hamza", "Karachi"});
		list.add(new String[]{"Hasan", "Lahore"});
		list.add(new String[]{"Jhangir", "Lahore"});
		list.add(new String[]{"Taha", "Multan"});
		list.add(new String[]{"Farooq", "Karachi"});
		list.add(new String[]{"Shabbir", "Sialkot"});
		list.add(new String[]{"Mujeeb", "Karachi"});
		list.add(new String[]{"Nadeem", "Lahore"});
		list.add(new String[]{"Zeeeshan", "Karachi"});
		list.add(new String[]{"Nauman", "Karachi"});
		list.add(new String[]{"Aslam", "Karachi"});
		list.add(new String[]{"Shehzad", "Islamabad"});
		list.add(new String[]{"Rameez", "Lahore"});
		list.add(new String[]{"Waqas", "Karachi"});
		list.add(new String[]{"Adil", "Multan"});
		list.add(new String[]{"Ubaid", "Karachi"});
		list.add(new String[]{"Asif", "Islamabad"});
		list.add(new String[]{"Waseem", "Karachi"});
		list.add(new String[]{"Junaid", "Karachi"});
		list.add(new String[]{"Liaquat", "Nawabshah"});
		list.add(new String[]{"Zubair", "Karachi"});
		list.add(new String[]{"Talha", "Mirpurkhas"});
		list.add(new String[]{"Hamza", "Karachi"});
		list.add(new String[]{"Hasan", "Lahore"});
		list.add(new String[]{"Jhangir", "Lahore"});
		list.add(new String[]{"Taha", "Multan"});
		list.add(new String[]{"Farooq", "Karachi"});
		list.add(new String[]{"Shabbir", "Sialkot"});
		list.add(new String[]{"Mujeeb", "Karachi"});
		list.add(new String[]{"Nadeem", "Lahore"});
		list.add(new String[]{"Zeeeshan", "Karachi"});
		list.add(new String[]{"Nauman", "Karachi"});
		
		try {
			PaginatorTableConfig tableConfig = new PaginatorTableConfig(list.size(), new String[]{"Name", "Address"}, list)
				.setFontSize(22)
				.setColumnsWeights(2,1)
				.setStripColor1(Color.BLUE)
				.setStripColor2(Color.YELLOW);
				
				
			PaginatorTable table = new PaginatorTable(this, tableConfig, null);
			
			llMain.addView(table);
		} catch (ColumnsException e) {
			e.printStackTrace();
		}
	
		
		
	}

	@Override
	public void onItemClicked(View v, String text) {
		Toast.makeText(this, "Reas=ched", Toast.LENGTH_SHORT).show();
		
	}

	

}
