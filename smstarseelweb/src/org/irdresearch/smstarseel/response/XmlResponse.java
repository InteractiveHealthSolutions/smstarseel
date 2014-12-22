/*package org.irdresearch.smstarseel.response;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XmlResponse {
	private Document document;
	private Element root;
	
	public XmlResponse(ResponseCode responseCode, ResponseMessage responseMessage, String customMessage) throws Exception{
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        document = docBuilder.newDocument();

        //create the root element 
        root = document.createElement(SmsTarseelGlobal.XML_RESPONSE_ROOT_TAG);
        document.appendChild(root);
        
        //can be modified to set some attributes and values.....
        addElement(ResponseCode.NAME, responseCode.CODE());
        addElement(ResponseMessage.NAME, responseMessage.MESSAGE() + ((customMessage == null || customMessage == "" )? "" : "\ndetails are :" + customMessage));
	}
	
	public void addElement(String name , String value){
		//create child element, add an attribute, and add to root
        Element child = document.createElement(name);
        //child.setAttribute("name", "value");
        root.appendChild(child);
        
        //add a text element to the child
        Text text = document.createTextNode(value);
        text.setNodeValue(value);
        child.appendChild(text);
	}
	public void addObjectList(String listName, List<Map<String, String>> attributeValuePairMapList){
		//create child element, add an attribute, and add to root
        Element listElement = document.createElement(SmsTarseelGlobal.XML_LIST_NAME);
        //child.setAttribute("name", "value");
        root.appendChild(listElement);
        
        Element listIdentifier = document.createElement(listName);
        listElement.appendChild(listIdentifier);

        for (Map<String, String> map : attributeValuePairMapList)
		{
            Element listItem = document.createElement(SmsTarseelGlobal.XML_LIST_ITEM_TAG_NAME);

            listIdentifier.appendChild(listItem);
            
        	Set<String> k = map.keySet();
        	for (String string : k)
			{
                Element attrib = document.createElement(string);
                listItem.appendChild(attrib);

        		//add a text element to the child
	            Text text = document.createTextNode(string);
	            text.setNodeValue(map.get(string));
	            attrib.appendChild(text);
			}
		}
	}
	public String docToString() throws TransformerException{
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "no");

        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(document);
        trans.transform(source, result);
        return sw.toString();
    }
	
	@Override
	public String toString() {
		try {
			return docToString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return "";
	}
}

*/