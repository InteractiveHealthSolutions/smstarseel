
public class rawcode {
	   /*try {
    // Executes the command.
    Process process = Runtime.getRuntime().exec("/system/bin/mkdir /data/data/tempsettings");
    
    // Reads stdout.
    // NOTE: You can write to stdin of the command using
    //       process.getOutputStream().
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
    int read;
    char[] buffer = new char[4096];
    StringBuffer output = new StringBuffer();
    while ((read = reader.read(buffer)) > 0) {
        output.append(buffer, 0, read);
    }
    reader.close();
    
    // Waits for the command to finish.
    process.waitFor();
    
    System.out.println("output---------:"+output.toString());
} catch (IOException e) {
    throw new RuntimeException(e);
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}
try {
    // Executes the command.
    Process process = Runtime.getRuntime().exec("/system/bin/ls /data/data/");
    
    // Reads stdout.
    // NOTE: You can write to stdin of the command using
    //       process.getOutputStream().
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
    int read;
    char[] buffer = new char[4096];
    StringBuffer output = new StringBuffer();
    while ((read = reader.read(buffer)) > 0) {
        output.append(buffer, 0, read);
    }
    reader.close();
    
    // Waits for the command to finish.
    process.waitFor();
    
    System.out.println("output---------:"+output.toString());
} catch (IOException e) {
    throw new RuntimeException(e);
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}
try {
    // Executes the command.
    Process process = Runtime.getRuntime().exec("/system/bin/cp /data/data/com.android.providers.settings/databases/settings.db /data/data/tempsettings");
    
    // Reads stdout.
    // NOTE: You can write to stdin of the command using
    //       process.getOutputStream().
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
    int read;
    char[] buffer = new char[4096];
    StringBuffer output = new StringBuffer();
    while ((read = reader.read(buffer)) > 0) {
        output.append(buffer, 0, read);
    }
    reader.close();
    
    // Waits for the command to finish.
    process.waitFor();
    
    System.out.println("output---------:"+output.toString());
} catch (IOException e) {
    throw new RuntimeException(e);
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}
try {
    // Executes the command.
    Process process = Runtime.getRuntime().exec("/system/bin/ls /data/data/tempsettings");
    
    // Reads stdout.
    // NOTE: You can write to stdin of the command using
    //       process.getOutputStream().
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
    int read;
    char[] buffer = new char[4096];
    StringBuffer output = new StringBuffer();
    while ((read = reader.read(buffer)) > 0) {
        output.append(buffer, 0, read);
    }
    reader.close();
    
    // Waits for the command to finish.
    process.waitFor();
    
    System.out.println("output---------:"+output.toString());
} catch (IOException e) {
    throw new RuntimeException(e);
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}
try {
    // Executes the command.
    Process process = Runtime.getRuntime().exec("/system/bin/chmod 777 /data/data/com.android.providers.settings/databases/settings.db");
    
    // Reads stdout.
    // NOTE: You can write to stdin of the command using
    //       process.getOutputStream().
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
    int read;
    char[] buffer = new char[4096];
    StringBuffer output = new StringBuffer();
    while ((read = reader.read(buffer)) > 0) {
        output.append(buffer, 0, read);
    }
    reader.close();
    
    // Waits for the command to finish.
    process.waitFor();
    
    System.out.println("output---------:"+output.toString());
} catch (IOException e) {
    throw new RuntimeException(e);
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}


try {
    // Executes the command.
    Process process = Runtime.getRuntime().exec("/system/bin/chmod 777 /data/data/com.android.providers.settings/databases/settings.db");
    
    // Reads stdout.
    // NOTE: You can write to stdin of the command using
    //       process.getOutputStream().
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
    int read;
    char[] buffer = new char[4096];
    StringBuffer output = new StringBuffer();
    while ((read = reader.read(buffer)) > 0) {
        output.append(buffer, 0, read);
    }
    reader.close();
    
    // Waits for the command to finish.
    process.waitFor();
    
    System.out.println("output---------:"+output.toString());
} catch (IOException e) {
    throw new RuntimeException(e);
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}
boolean b = RootTools.isRootAvailable();
boolean bb = RootTools.isAccessGiven();
boolean isbb = RootTools.isBusyboxAvailable();
//RootTools.offerSuperUser(this);
try
{
	@SuppressWarnings("unused")
	Set<String> abc = RootTools.getPath();
	System.out.println(abc);
}
catch (Exception e1)
{
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
try
{
	List<String> ll1 = RootTools.sendShell("adb shell");
	List<String> ll0 = RootTools.sendShell("su");

	List<String> ll = RootTools.sendShell("cd data/data/com.android.providers.settings/databases/");
	List<String> ll2 = RootTools.sendShell("ls");
	System.out.println("hiaaaaaa11111a---------:"+ll1);

	System.out.println("hiaaaaaaa---------:"+ll);
	System.out.println("hiaaaaaa22222a22---------:"+ll2);

}
catch (IOException e)
{
	// TODO Auto-generated catch block
	e.printStackTrace();
}
catch (InterruptedException e)
{
	// TODO Auto-generated catch block
	e.printStackTrace();
}
catch (RootToolsException e)
{
	// TODO Auto-generated catch block
	e.printStackTrace();
}
SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/com.android.providers.settings/databases/settings.db", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

db.beginTransaction();
Cursor a = db.rawQuery("select * from gservices", null);
while (a.moveToNext())
{
	String ss = "";
	for (String cc : a.getColumnNames())
	{
		ss+=cc+":"+a.getString(a.getColumnIndex(cc));
	}
	System.out.println(ss);
}*/
/*TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
String mPhoneNumber = tMgr.getLine1Number();
String imei = tMgr.getDeviceId();
String swV = tMgr.getDeviceSoftwareVersion();
String l1 = tMgr.getLine1Number();
String ccd = tMgr.getNetworkCountryIso();
CellLocation cloc = tMgr.getCellLocation();
String cl = cloc.toString();
List<NeighboringCellInfo> nlist = tMgr.getNeighboringCellInfo();
if(nlist.size() > 0){
String nls = nlist.get(0).toString();
}
String sop = tMgr.getSimSerialNumber();
String sopn = tMgr.getSimOperatorName();
String androidID = android.provider.Settings.System.getString(this.getContentResolver(),android.provider.Settings.System.ANDROID_ID);
System.out.print(1111);*/

//   Button login = (Button) findViewById(R.id.loginbtn);
//   login.setOnClickListener(new LoginListener());
}
