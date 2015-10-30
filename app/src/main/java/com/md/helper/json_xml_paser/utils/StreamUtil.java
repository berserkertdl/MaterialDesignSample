package com.md.helper.json_xml_paser.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**与流处理相关的工具类*/
public class StreamUtil {

	/**将给定输入流中的内容转移到给定输出流
	 * @throws IOException */
	public static void getInputStream(InputStream in,OutputStream out) throws IOException{
		if(in == null || out == null){
			return;
		}
		//定义缓冲区
		byte[] buf = new byte[1024];
		int len = -1;
		while((len = in.read(buf)) != -1){  //循环读取出入流中的内容，并写入输出流，直到输入流末尾
			out.write(buf,0,len);
		}
		out.flush();
	}

	/**将输入流中的内容生成字符串
	 * @throws IOException */
	public static String getString(InputStream in) throws IOException{
		String str = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		getInputStream(in, out);
		str = new String(out.toByteArray());
		in.close();
		return str;
	}
}
