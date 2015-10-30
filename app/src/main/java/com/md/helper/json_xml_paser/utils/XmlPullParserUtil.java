package com.md.helper.json_xml_paser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * android内置了pull解析，相对dom解析来说pull更省内存.
 */
public class XmlPullParserUtil {

    public static List<Map<String, String>> pullParser(InputStream in) {
        List<Map<String, String>> activitys = null;
        try {
            //构建pull解析工厂
            XmlPullParserFactory factory;
            factory = XmlPullParserFactory.newInstance();
            //构建pull解析器对象
            XmlPullParser parser = factory.newPullParser();
            //设置解析器的数据源
            parser.setInput(new InputStreamReader(in));
            //获取事件，开始进行解析
            int eventType = parser.getEventType();
            //将要生成的Map对象
            Map activity = null;

            //循环遍历xml文档，直到遍历到文档末尾
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //在xml文档开始的时候构建ArrayList对象。
                    case XmlPullParser.START_DOCUMENT:
                        activitys = new ArrayList<Map<String, String>>();
                        break;
                    //在标签开始时对标签名进行判断
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        //标签名为CD时，构建CD对象
                        if ("activity".equals(name)) {
                            activity = new HashMap();
                            //如果标签为title，则activity肯定不为空，且获取到的文本为activity标题，则将activity的标题属性设置为title标签的文本
                        } else if ("title".equals(name)) {
                            activity.put("title", parser.nextText());
                        } else if ("actionName".equals(name)) {
                            activity.put("actionName", parser.nextText());
                        }
                        break;
                    //标签结束后，判断结束标签是什么。如果cd标签结束，则生成cd对象完成，应该将其添加到ArrayList中
                    case XmlPullParser.END_TAG:
                        if ("activity".equals(parser.getName())) {
                            activitys.add(activity);
                        }
                        break;
                }
                //循环处理结束后，需要将标签设置为下一个标签，避免无限循环
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return activitys;
    }
}