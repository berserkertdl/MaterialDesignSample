package com.md.helper.json_xml_paser.utils;

import com.md.helper.json_xml_paser.utils.CDEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.*;

/**SAX解析方式类似于pull解析，不过将解析部分又进行了封装，结构更清晰一些*/
public class XmlSAXParserUtil {

	static ArrayList<CDEntity> cds = null;
	public static ArrayList<CDEntity> SAXParser(InputStream in){
		cds = null;
		//构建SAX解析工厂
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//构建解析器对象
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(in, new CDHandler());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cds;
	}
	
	/**继承DefaultHandler并重写其中的5个方法，用来自定义解析方式*/
	static class CDHandler extends DefaultHandler{
		//定义用来存储数据的CD实体类
		CDEntity entity = null;
		//缓存标签名称
		String qName = null;
		//在文档开始时执行该方法，此例需要初始化ArrayList
		@Override
		public void startDocument() throws SAXException {
			cds = new ArrayList<CDEntity>();
		}
		
		//在文档结束时要执行的方法
		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}
		
		//在元素开始时要执行的方法。根据标签名不同执行不同的操作
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if("CD".equals(qName)){
				entity = new CDEntity();
			}else{
				this.qName = qName;
			}
		}
		
		//在元素结束时要执行的方法，这里如果结束的元素是cd，代表该对象解析完成，则直接将entity添加到List中
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if("CD".equals(qName)){
				cds.add(entity);
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if("TITLE".equals(qName)){
				entity.setTitle(new String(ch,start,length));  //将传入的字符数组转成字符串，并设置为entity的参数
			}else if("ARTIST".equals(qName)){
				entity.setArtist(new String(ch,start,length));  
			}else if("COUNTRY".equals(qName)){
				entity.setCountry(new String(ch,start,length)); 
			}else if("COMPANY".equals(qName)){
				entity.setCompany(new String(ch,start,length));  
			}else if("PRICE".equals(qName)){
				entity.setPrice(Float.parseFloat(new String(ch,start,length)));  
			}else if("YEAR".equals(qName)){
				entity.setYear(Integer.parseInt(new String(ch,start,length))); 
			}
			qName = null;
		}
	}

}

