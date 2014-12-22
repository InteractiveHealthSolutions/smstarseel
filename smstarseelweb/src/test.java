/*package org.irdresearch.smstarseel;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.irdresearch.smstarseel.global.RequestParam.LoginResponse;
import org.irdresearch.smstarseel.global.RequestParam.ProjectResponseParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.response.XmlResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class test {
public static void main(String[] args) {
	Set<String> set = new HashSet<String>();
	set.add(null);
	try {
		System.out.println(new XmlResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"").docToString());
	} catch (TransformerException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	XmlResponse xr = null;
	try {
		xr = new XmlResponse(ResponseCode.ERROR,ResponseMessage.ERROR,"");
	} catch (Exception e) {
		e.printStackTrace();
	}
	xr.addElement("el1", "elval1");
	xr.addElement("el2", "elval2");
	xr.addElement("el3", "elval3");
	xr.addElement("el4", "elval4");
	xr.addElement("el5", "elval5");

	try {
		System.out.println(xr.docToString());
	} catch (TransformerException e) {
		e.printStackTrace();
	}
	String xs = "<?xml version=\"1.0\"?>"+
	"<smstarseel>"+
	"<respcd>error</respcd><respmsg>error</respmsg><el1>elval1</el1><el2>elval2</el2><el3>elval3</el3><el4>elval4</el4><el5>elval5</el5>"+
"<maplist>"+
	"<listitem>"+
		"<firstname>yong</firstname>"+
		"<lastname>mook kim</lastname>"+
		"<nickname>mkyong</nickname>"+
		"<salary>100000</salary>"+
	"</listitem>"+
	"<listitem>"+
		"<firstname>low</firstname>"+
		"<lastname>yin fong</lastname>"+
		"<nickname>fong fong</nickname>"+
		"<salary>200000</salary>"+
	"</listitem>"+
"</maplist>"+
"<maplist>"+
"<listitem>"+
	"<firstname>yong</firstname>"+
	"<lastname>mook kim</lastname>"+
	"<nickname>mkyong</nickname>"+
	"<salary>100000</salary>"+
"</listitem>"+
"<listitem>"+
	"<firstname>low</firstname>"+
	"<lastname>yin fong</lastname>"+
	"<nickname>fong fong</nickname>"+
	"<salary>200000</salary>"+
"</listitem>"+
"</maplist>"+
"</smstarseel>";
	
	XmlResponse xml = null;
	try
	{
		xml = new XmlResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
	}
	catch (Exception e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	xml.addElement(LoginResponse.DETAILS.KEY(), "message");
	List<Map<String, String>> prjList = new ArrayList<Map<String,String>>();
	for (int i = 0 ; i <9 ;i++)
	{
		Map<String, String> prjMap = new HashMap<String, String>();
		prjMap.put("firstname", "fname-"+i);
		prjMap.put("nickname", "nname-"+i);
		
		prjList.add(prjMap);
	}
	xml.addObjectList("prjlst",prjList);
	List<Map<String, String>> smsList = new ArrayList<Map<String,String>>();
	for (int i = 0 ; i <9 ;i++)
	{
		Map<String, String> prjMap = new HashMap<String, String>();
		prjMap.put("name", "fnamelname-"+i);
		prjMap.put("cell", "0000cell-"+i);
		
		smsList.add(prjMap);
	}
	xml.addObjectList("smslst",smsList);
	try
	{
		System.out.println(xml.docToString());
		parse(new ByteArrayInputStream(xml.docToString().getBytes()));
	}
	catch (TransformerException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public static void parse(InputStream is) {
	Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(is);
        Element root = dom.getDocumentElement();
		NodeList items = root.getChildNodes();

		for (int i = 0; i < items.getLength(); i++)
		{

			if(!items.item(i).getNodeName().equalsIgnoreCase(SmsTarseelGlobal.XML_LIST_NAME)){
				// TODO only for this as android donot have function getTextContent which disables us to
				// get value as text so for text it will have obviously first child node value as text
				Node aa = items.item(i).getFirstChild();
				String cc = items.item(i).getFirstChild().getNodeValue();
				String text = items.item(i).getFirstChild().getNodeValue();
				String name = items.item(i).getNodeName();
	
				hashtable.put(name, text);
			}
			else
			{
				Element listContainer = (Element) items.item(i).getFirstChild();
				
				ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();

				NodeList listOfRecords = listContainer
						.getElementsByTagName(SmsTarseelGlobal.XML_LIST_ITEM_TAG_NAME);// Read you subchild of root node
				int totalRecords = listOfRecords.getLength();

				for (int i1 = 0; i1 < totalRecords; i1++)
				{
					Map<String, String> sms = new HashMap<String, String>();

					Node record = listOfRecords.item(i1);

					if (record.getNodeType() == Node.ELEMENT_NODE)
					{
						Element recordElement = (Element) record;

						NodeList nodelist = recordElement.getChildNodes();

						for (int j = 0; j < nodelist.getLength(); j++)
						{
							Node node = (Node) nodelist.item(j);

							if (node.getNodeType() == Node.ELEMENT_NODE)
							{
								NodeList textLNList = node.getChildNodes();

								Node txNode = textLNList.item(0);
								if (txNode != null)
								{
									String value = txNode.getNodeValue();
									if (value != null)
										sms.put(node.getNodeName(), value);
									else
										sms.put(node.getNodeName(), "");
								}
								else
								{
									sms.put(node.getNodeName(), "");
								}
							}
						}
						data.add(sms);
					}
				}
				hashtable.put(listContainer.getNodeName(), data);
			}
		}
		System.out.println(hashtable);
	}
	catch (Exception e)
	{
		throw new RuntimeException(e);
	}
}
}
*/