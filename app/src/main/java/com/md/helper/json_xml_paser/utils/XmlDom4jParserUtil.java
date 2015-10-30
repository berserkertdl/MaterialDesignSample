package com.md.helper.json_xml_paser.utils;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**通过dom4j方法解析xml文件。dom4j是一种行业标准，效率比较高，常用于服务端xml解析
 * 该方案里的好多类名（Document、Elemen等与dom方案中一致，不过要注意包含的类名是在dom4j包下，不要用错）
 * android默认是不包含dom4j解析方案，使用这种方式需要包含dom4j-1.6.1.jar
 * */
public class XmlDom4jParserUtil {
	public static ArrayList<CDEntity> DomParse(InputStream in){
		ArrayList<CDEntity> cds = new ArrayList<CDEntity>();
		//构建解析器对象
		SAXReader reader = new SAXReader();
		//通过解析器将输入流转化为document对象
		Document doc;
		Element root = null;
		try {
			doc = reader.read(in);
			//获取xml文档的跟元素
			root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		//定义生成的cd对象
		CDEntity entity = null;
		//通过遍历器遍历所有子元素
		for(Iterator<Element> i = root.elementIterator();i.hasNext();){  //循环初始化是获取元素遍历器，循环条件是遍历器中还有下一个元素
			Element element = i.next();  //获取下一个元素（循环变量）
			entity = new CDEntity();
			for(Iterator<Element> j = element.elementIterator();j.hasNext();){
				Element cdElement = j.next();
				//根据cd标签的自标签不同的名字，将text填入不同的变量
				if("TITLE".equals(cdElement.getName())){
					entity.setTitle(cdElement.getText());
				}else if("ARTIST".equals(cdElement.getName())){
					entity.setArtist(cdElement.getText());
				}else if("COUNTRY".equals(cdElement.getName())){
					entity.setCountry(cdElement.getText());
				}else if("COMPANY".equals(cdElement.getName())){
					entity.setCompany(cdElement.getText());
				}else if("PRICE".equals(cdElement.getName())){
					entity.setPrice(Float.parseFloat(cdElement.getText()));
				}else if("YEAR".equals(cdElement.getName())){
					entity.setYear(Integer.parseInt(cdElement.getText()));
				}
			}
			cds.add(entity);
		}
		return cds;
	}
}
