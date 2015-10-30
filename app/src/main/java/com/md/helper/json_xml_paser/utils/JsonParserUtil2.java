package com.md.helper.json_xml_paser.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**使用JSONObject,JSONArray等来构建json对象。要求android sdk 版本在2.3以上*/
public class JsonParserUtil2 {

	/**将json格式的字符串解析*/
	public static ArrayList<Map<String,String>> readJson(String in) throws JSONException{
		ArrayList<Map<String,String>> activitys = new ArrayList<Map<String,String>>();
		//获取json解析器对象
		JSONTokener tokener = new JSONTokener(in);
		//通过nextValue可以读取整个对象或者一部分对象（从当前位置开始以后的第一个完整对象或完整文本）
		JSONArray jArray = (JSONArray) tokener.nextValue();
		Map<String,String> activity = null;
		//循环遍历数组对象，获取每一个json对象进行解析
		for(int i = 0;i<jArray.length();i++){
			JSONObject jo = (JSONObject) jArray.get(i);
			activity = new HashMap<String,String>();
			//将json对应的key的值取出来，设置为Map<String,String>的属性
			activity.put("title",jo.getString("title"));
			activity.put("actionName", jo.getString("actionName"));
			//解析完成，将entity添加到list中
			activitys.add(activity);
		}
		return activitys;
	}

	/**将ArrayList中的内容写入json对象中
	 * @throws JSONException */
	public static String writeJson(ArrayList<Map<String,String>> cds) throws JSONException{
		String str = null;
		//构建jsonArray对象，用来存储数组
		JSONArray jArray = new JSONArray();
		//用来存放解析出来的entity对象
		JSONObject jo = null;
		//遍历ArrayList，将每一个entity对象生成jsonObject对象
		for(Map<String,String> activity:cds){
			jo = new JSONObject();
			jo.put("title", activity.get("title"));
			jo.put("actionName", activity.get("actionName"));
			//将解析出来的jsonObject对象添加到jsonArray数组中
			jArray.put(jo);
		}
		str = jArray.toString();  //生成json格式字符串
		return str;
	}

}
