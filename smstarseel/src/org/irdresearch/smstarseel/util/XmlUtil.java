package org.irdresearch.smstarseel.util;
/*package org.irdresearch.smstarseel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {
	public static Hashtable<String, Object> parse(InputStream is) {
		Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			// String str =
			// "<smstarseel><respcd>error</respcd><respmsg>error</respmsg><el1>elval1</el1><el2>elval2</el2><el3>elval3</el3><el4>elval4</el4><el5>elval5</el5></smstarseel>";
			// Document dom = builder.parse(new ByteArrayInputStream(str.getBytes()));

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
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return hashtable;
	}
}

*/