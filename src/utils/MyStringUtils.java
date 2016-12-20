package utils;

import java.io.UnsupportedEncodingException;

public class MyStringUtils {

	/**
	 * 把字符串转换成二进制
	 * @param string
	 * 			要转换的字符串
	 * @return
	 * 			二进制的字符串
	 */
	public static long String2BinaryByCharAt(String string) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			sb.append(string.charAt(i));
		}
		
		return Long.parseLong(sb.toString());
		
	}
	
	
	public static String iso2utf8(String urlParam) throws UnsupportedEncodingException{
		return new String(urlParam.getBytes("ISO-8859-1"),"utf-8").trim();
	}
}
