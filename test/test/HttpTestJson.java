package test;

import utils.MyHttpClientUtils;

public class HttpTestJson {

	public static void main(String[] args) {
		
		String json="{\"id\":2}";
//		String json="{\"id\":2,\"userName\":\"zhangsan\",\"password\":\"zhangsan\"}";
		try {
			String result = MyHttpClientUtils.httpPostWithJSON("http://127.0.0.1:8080/3Platforms/people/user_saveUserInfo", json, MyHttpClientUtils.UTF_8);
			System.out.println("result == "+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
