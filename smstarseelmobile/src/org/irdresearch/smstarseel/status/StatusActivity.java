package org.irdresearch.smstarseel.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.irdresearch.smstarseel.R;
import org.irdresearch.smstarseel.SMSTarseel;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;

import com.ihsinformatics.interactivepaginatortable.ColumnsException;
import com.ihsinformatics.interactivepaginatortable.PaginatorTable;
import com.ihsinformatics.interactivepaginatortable.PaginatorTableConfig;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends FragmentActivity implements OnItemClickListener {
	private TextView projectname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tarseelstatus);
		LinearLayout llMain = (LinearLayout) findViewById(R.id.statustable);

		String proname = TarseelGlobals.PROJECTS(this);		
		projectname = (TextView)  findViewById(R.id.projectname);
		projectname.setText(proname);
		ArrayList<String[]> List = new ArrayList<String[]>();
		
		Iterator it = TarseelGlobals.getGlobalAttribs(this).entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			List.add(new String[]{pairs.getKey().toString().toUpperCase(), (String) pairs.getValue()});
		}	

		try {
			PaginatorTableConfig tableConfig = new PaginatorTableConfig(List.size(), new String[]{"Variable Name", "Value"}, List)
			.setFontSize(16)
			.setColumnsWeights(2,1)
			.setStripColor1(Color.WHITE)
			.setStripColor2(Color.rgb(174, 220, 249))
			.setFontColor(Color.BLACK);				
			PaginatorTable table = new PaginatorTable(this, tableConfig, null);

			llMain.addView(table);
		} catch (ColumnsException e) {
			e.printStackTrace();
		}



	}

	public void onItemClicked(View v, String text) {
		Toast.makeText(this, "Reas=ched", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
	public void onBackPressed()
	{
		SMSTarseel.startHomeScreen(this);
	}
	protected SmsTarseelRequest createRequest()
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected boolean validateFormData()
	{
		// TODO Auto-generated method stub
		return false;
	}



}
