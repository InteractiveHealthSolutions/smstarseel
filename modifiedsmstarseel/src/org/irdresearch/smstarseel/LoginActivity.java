package org.irdresearch.smstarseel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.irdresearch.smstarseel.call.CallLogReader;
import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.constant.TarseelGlobals.COMM_MODE;
import org.irdresearch.smstarseel.constant.TarseelGlobals.ErrorMessages;
import org.irdresearch.smstarseel.dialogue.ConfirmPopup;
import org.irdresearch.smstarseel.dialogue.ErrorAlert;
import org.irdresearch.smstarseel.form.SmsTarseelForm;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.LoginRequest;
import org.irdresearch.smstarseel.global.RequestParam.LoginResponse;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.sms.SmsCollector;
import org.irdresearch.smstarseel.sms.SmsDispenser;
import org.irdresearch.smstarseel.util.FileUtil;
import org.irdresearch.smstarseel.validator.UserValidator;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
public class LoginActivity extends SmsTarseelForm  {

	private EditText username ;
	private EditText password ;
	private EditText serverurl;
	private Spinner commtype;
	private EditText commport;
	private TextView servertv;
	private TextView nametv;
	private Button login;
	private Button relogin;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        username = (EditText) findViewById(R.id.usernametxt);
        password = (EditText) findViewById(R.id.passwordtxt);
        serverurl = (EditText) findViewById(R.id.serverurledtxt);
        //TODO:Commented Comm. type code 
        commport = (EditText) findViewById(R.id.commportedtxt);
      
        commtype = (Spinner)  findViewById(R.id.commtypeddp);
        servertv = (TextView) findViewById(R.id.textView5);
        nametv= (TextView) findViewById(R.id.textView1);
        //serverurl.setScroller(new Scroller(getBaseContext())); 
        //serverurl.setMaxLines(1); 
        //serverurl.setVerticalScrollBarEnabled(true); 
        //serverurl.setMovementMethod(new ScrollingMovementMethod()); 
        
       login= (Button)findViewById(R.id.loginbtn);
       relogin= (Button)findViewById(R.id.reloginbtn);
       
      
      
       
		ArrayList<String> arrl = new ArrayList<String>();
		for (COMM_MODE string : TarseelGlobals.COMM_MODE.values()) {
			arrl.add(string.name());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrl);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		commtype.setAdapter(adapter);
        
       /* String selectedcommtype = TarseelGlobals.getPreference(this, TarseelGlobals.COMM_TYPE_PREF_NAME, null);
        for (COMM_MODE com : TarseelGlobals.COMM_MODE.values()) {
        	RadioButton btn = new RadioButton(this);
        	btn.setText(com.name());
        	//TODO  delete for socket
        	btn.setChecked(true);
        	btn.setSelected(true);
        	if(selectedcommtype != null && !selectedcommtype.trim().equals("")){
        		btn.setChecked(true);
        		btn.setSelected(true);
        	}
        	commtype.addView(btn, LayoutParams.WRAP_CONTENT);
		}*/
        
		
		///////////////////Move it to start if possible
		/*if(radioButton.getText().toString().equalsIgnoreCase(COMM_MODE.USB.name()))
		{
			((TableRow) findViewById(R.id.serverurltblrw)).setVisibility(TableRow.INVISIBLE);
			((TableRow) findViewById(R.id.commporttblrw)).setVisibility(TableRow.VISIBLE);
		}
		else */
		if(commtype.getSelectedItem().toString().equalsIgnoreCase(COMM_MODE.WIFI.name()))
		{
			//((TableRow) findViewById(R.id.commporttblrw)).setVisibility(TableRow.INVISIBLE);
			((EditText) findViewById(R.id.serverurledtxt)).setVisibility(EditText.VISIBLE);
		}
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
    	
    	if(TarseelGlobals.ENABLE_UNLOCK 
    			|| SmsDispenser.getInstance().isServiceStarted(this)
    			|| SmsCollector.getInstance().isServiceStarted(this)
    			|| CallLogReader.getInstance().isServiceStarted(this))
    	{
    		int timeout = 60;
        	try{
        	timeout = Integer.parseInt(TarseelGlobals.getPreference(this, TarseelGlobals.COMM_PORT_NUM_PREF_NAME, "60"));
        	}
        	catch (Exception e) {
    			e.printStackTrace();
    			 FileUtil.writeLog(null, "Finding session timeout threw exception");
    		     FileUtil.writeLog(e);
    		}
        	
        	Date lastunlockdate = null;
        	try{
        		String lastunlockdatestr = TarseelGlobals.getPreference(this, TarseelGlobals.LAST_UNLOCK_TIME_PREF_NAME, null);
        		lastunlockdate = lastunlockdatestr == null ? null : TarseelGlobals.DEFAULT_DT_FRMT_SDF.parse(lastunlockdatestr);
            }
            catch (Exception e) {
        		e.printStackTrace();
        		FileUtil.writeLog(null, "Finding last unlock date threw exception");
        		FileUtil.writeLog(e);
        	}
        	
			if (lastunlockdate != null
					&& lastunlockdate.after(new Date(new Date().getTime()
							- (1000 * timeout)))) {
				performunlock();
				return;
			}

			((Button) findViewById(R.id.unlockscrbtn))
					.setVisibility(Button.VISIBLE);
			serverurl.setVisibility(EditText.INVISIBLE);
			servertv.setVisibility(TextView.INVISIBLE);
			nametv.setText("Unlock Project");
			relogin.setVisibility(Button.VISIBLE);
			login.setVisibility(Button.INVISIBLE);
		} else {
			((Button) findViewById(R.id.unlockscrbtn))
					.setVisibility(Button.INVISIBLE);
			serverurl.setVisibility(EditText.VISIBLE);
			servertv.setVisibility(TextView.VISIBLE);
			nametv.setText("Login Details");
			login.setVisibility(Button.VISIBLE);
			relogin.setVisibility(Button.INVISIBLE);
			// button alignment dynamically
		}
    	username.setText("");
    	password.setText("");
    	String url = TarseelGlobals.getPreference(this, TarseelGlobals.SERVER_URL_PREF_NAME, "http://199.172.1.171:8181/smstarseelweb/smstarseel");
    	serverurl.setText(url);
    	String portnum = TarseelGlobals.getPreference(this, TarseelGlobals.COMM_PORT_NUM_PREF_NAME, "0000");
    	commport.setText(portnum);
    }

    @Override
    public void onBackPressed()
    {
		new ConfirmPopup(this, "Do you want to exit ?", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();// TODO
			}
		});
    }
    public void unlockClickHandler(View view){
    	if(validateFormData()){
    		if(!username.getText().toString().trim().equals(TarseelGlobals.LOGGEDIN_USERNAME(this))
    				|| !password.getText().toString().trim().equals(TarseelGlobals.LOGGEDIN_PASSWORD(this))){
    			ErrorAlert.showError(this, ErrorMessages.LOGGEDIN_USER_CREDENTIALS_NOT_MATCHES.MESSAGE());
    		}
    		else{
    			performunlock();
    		}
    	}
    }
    
    public void performunlock(){
    	TarseelGlobals.ENABLE_UNLOCK = true;
		TarseelGlobals.writePreference(this, TarseelGlobals.LAST_UNLOCK_TIME_PREF_NAME, TarseelGlobals.DEFAULT_DT_FRMT_SDF.format(new Date()));
		SMSTarseel.startHomeScreen(this);
    }
    private void doLogin(){
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	
    	StrictMode.setThreadPolicy(policy);
    	if(validateFormData()){
        	try
			{
        		TarseelGlobals.writePreference(this, TarseelGlobals.COMM_TYPE_PREF_NAME, commtype.getSelectedItem().toString().trim());
        		TarseelGlobals.writePreference(this, TarseelGlobals.SERVER_URL_PREF_NAME, serverurl.getText().toString().trim());
        		TarseelGlobals.writePreference(this, TarseelGlobals.COMM_PORT_NUM_PREF_NAME, commport.getText().toString().trim());
        		
        		JSONObject resp = HttpSender.sendLargeText(this, createRequest());
				if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE())){
					ErrorAlert.showError(this, (String)resp.get(ResponseMessage.NAME));
				}
				else if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.SUCCESS.CODE()))
				{
					
					
					TarseelGlobals.setLOGGEDIN_USERNAME(this, username.getText().toString().trim());
					TarseelGlobals.setLOGGEDIN_PASSWORD(this, password.getText().toString().trim());
					TarseelGlobals.addTo_CONSOLE_BUFFER(null ,(String)resp.get(LoginResponse.DETAILS.KEY()));
					
					performLogin();
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				ErrorAlert.showError(this, "ERROR: "+e.getMessage()+"\n"+ErrorMessages.APPLICATION_CONNECTION_ERROR);
			}
			catch (ClientProtocolException e)
			{
				e.printStackTrace();
				ErrorAlert.showError(this, "ERROR: "+e.getMessage()+"\n"+ErrorMessages.CONNECTION_ERROR);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				ErrorAlert.showError(this, "ERROR: "+e.getMessage()+"\n"+ErrorMessages.CONNECTION_ERROR);
			}
        	catch (Exception e) {
        		e.printStackTrace();
				ErrorAlert.showError(this, "ERROR: "+e.getMessage()+"\n"+ErrorMessages.CONNECTION_ERROR);
			}
    	}
    }
    private void performLogin(){
    	
    	TarseelGlobals.ENABLE_UNLOCK = true;
		TarseelGlobals.writePreference(this, TarseelGlobals.LAST_UNLOCK_TIME_PREF_NAME, TarseelGlobals.DEFAULT_DT_FRMT_SDF.format(new Date()));
		SMSTarseel.startRegisterDeviceProjectForm(this,true);
    }
    public void loginClickHandler(final View view){
    	final Activity ac=this;
    	if(TarseelGlobals.ENABLE_UNLOCK 
    			&& TarseelGlobals.LOGGEDIN_USERNAME(this) != null 
    			&& !TarseelGlobals.LOGGEDIN_USERNAME(this).trim().equals("") ){
    		new ConfirmPopup(this, "A user is already logged in? You may want to unlock screen. Are you sure to send new login rquest to server?", 
				new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						SmsDispenser.getInstance().shutdownService(ac);								
						SmsCollector.getInstance().shutdownService(ac);							
						CallLogReader.getInstance().shutdownService(ac);
						doLogin();
					}
				});
    	}
    	else{
    		doLogin();
    	}
	}

	@Override
	protected SmsTarseelRequest createRequest()
	{
		SmsTarseelRequest req = null ;
		try
		{
			req = new SmsTarseelRequest(this, App_Service.LOGIN);
			req.addParam(LoginRequest.USERNAME.KEY(), username.getText().toString().trim());
			req.addParam(LoginRequest.PASSWORD.KEY(), password.getText().toString().trim());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return req;
	}

	@Override
	protected boolean validateFormData()
	{
		String unameVal = username.getText().toString().trim();
		String pwdVal = password.getText().toString().trim();
		String urlVal = serverurl.getText().toString().trim();
	//	String portnumVal = commport.getText().toString().trim();

		ErrorMessages uvalid = UserValidator.validateUsername(unameVal);
		ErrorMessages pvalid = UserValidator.validatePassword(pwdVal);
		
    	if(!uvalid.equals(ErrorMessages.NO_ERROR)){
    		ErrorAlert.showError(this, uvalid.MESSAGE());
    		return false;
    	}
    	else if(!pvalid.equals(ErrorMessages.NO_ERROR)){
    		ErrorAlert.showError(this, pvalid.MESSAGE());
    		return false;
    	}
    	
		if(commtype.getSelectedItem() == null){
			ErrorAlert.showError(this, ErrorMessages.COMM_TYPE_MISSING.MESSAGE());
    		return false;
		}
		
		/*if(radioButton.getText().toString().equalsIgnoreCase(COMM_MODE.USB.name())
				&& portnumVal.equals("")){
			ErrorAlert.showError(this, ErrorMessages.INVALID_COMMPORT.MESSAGE());
    		return false;
		}
		else */if(commtype.getSelectedItem().toString().equalsIgnoreCase(COMM_MODE.WIFI.name())
				&& urlVal.equals("")){
			ErrorAlert.showError(this, ErrorMessages.INVALID_SERVERURL.MESSAGE());
    		return false;
		}
    	
    	return true;
	}
}
