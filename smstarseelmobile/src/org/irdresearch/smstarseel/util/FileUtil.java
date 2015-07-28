package org.irdresearch.smstarseel.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.irdresearch.smstarseel.constant.TarseelGlobals;

import android.content.Context;

public class FileUtil {

	public static boolean writeLog(String tag, String log){
		try{
			Logger.getLogger("org.apache").error(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date())+"- "+(tag==null?"":tag+": ")+log);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean writeLog(Exception log){
		try{
			Logger.getLogger("org.apache").error(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()),log);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static StringBuilder readLog(Context context){
		return FileUtil.readFromFile(TarseelGlobals.LOG_FILE());
	}
	public static void writeToFile(File file, String textToAppend)
	{
		try
		{
			FileWriter filewriter = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(filewriter);
			out.write(textToAppend + "\n");
			out.flush();
			out.close();
			System.out.println("file written:");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static StringBuilder readFromFile(File file){
		try
		{
			FileInputStream fIn = new FileInputStream(file);

			byte[] b = new byte[(int) file.length()];
			fIn.read(b);

			fIn.close();
			// Transform the chars to a String
			StringBuilder readString = new StringBuilder(new String(b));
			return readString;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return new StringBuilder(ioe.getMessage());
		}
	}
}
