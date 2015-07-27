package org.irdresearch.smstarseel.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.os.Environment;
import android.util.Log;

public class AppListHandler extends DefaultHandler {
	private static final String TAG = "parser";
	private HashMap<String, Boolean> appList;
	private long fileSize;

	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		if (name == "app") {
			appList.put(atts.getValue(0), true);
		}
	}

	public void endElement(String uri, String name, String qName)
			throws SAXException {
	}

	public void characters(char ch[], int start, int length) {
		String chars = (new String(ch).substring(start, start + length));
		Log.d(TAG, "chars :" + chars);
	}

	/**
	 * @param path
	 *            - path to the xml file on the SD card.
	 */
	public void parseXML(String path) {
		try {
			appList = new HashMap<String, Boolean>();
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/download/" + path);

			fileSize = file.length();
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(this);
			xr.parse(new InputSource(new FileInputStream(file)));
		} catch (IOException e) {
			appList = null;
			Log.e(TAG, e.toString());
		} catch (SAXException e) {
			appList = null;
			Log.e(TAG, e.toString());
		} catch (ParserConfigurationException e) {
			appList = null;
			Log.e(TAG, e.toString());
		}

	}

	public boolean parseSuccess() {
		return appList != null;
	}

	public long getFileSize() {
		return fileSize;
	}

	public HashMap<String, Boolean> getAppList() {
		return appList;
	}

}

