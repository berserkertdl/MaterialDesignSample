package com.md.helper.json_xml_paser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//通过w3c发布的Dom标准解析xml文件
public class XmlDomParser {

	public static ArrayList<CDEntity> DomParse(InputStream in){
		ArrayList<CDEntity> cds = null;
		try {
			//获取xml解析器工厂对象
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//获取xml解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();
			//将xml文件的输入流解析为document对象
			Document doc = builder.parse(in);
			//获取xml中所有需要的子节点（解析CD标签，所以按CD查找子标签）
			NodeList list = doc.getElementsByTagName("CD");
			CDEntity entity = null;
			if(list == null || list.getLength()<=0){
				return cds;
			}
			cds = new ArrayList<CDEntity>();
			for(int i = 0;i<list.getLength();i++){
				Element element = (Element) list.item(i);
				//获取名为title的第一个元素的内容
				String title = element.getElementsByTagName("TITLE").item(0).getTextContent();
				String artist = element.getElementsByTagName("ARTIST").item(0).getTextContent();
				String country = element.getElementsByTagName("COUNTRY").item(0).getTextContent();
				String company = element.getElementsByTagName("COMPANY").item(0).getTextContent();
				float price = Float.parseFloat(element.getElementsByTagName("PRICE").item(0).getTextContent());
				int year = Integer.parseInt(element.getElementsByTagName("YEAR").item(0).getTextContent());
				entity = new CDEntity(title, artist, country, company, price, year);
				cds.add(entity);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cds;
	}
}


