package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
/**
 * 
 * @author Administrator
 * 		利用HttpClient进行post请求的工具类
 * @param <T>
 *	 		T == SSLClient.java https类型
 * 		T == Httpclient.java   http类型
 */
public class  MyHttpClientUtils {
	
	public static final String UTF_8 = "utf-8";
	
	private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    
	
	private MyHttpClientUtils() {
		super();
	}
	
	
	public static String doPost(String url,Map<String,String> map,String charset){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String result = null;
		try{
			//设置参数
			if(map!=null)
				setList(map, charset, httpPost);
			HttpResponse response = httpClient.execute(httpPost);
			return result(charset, response);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public static String doHttpsPost(String url,Map<String,String> map,String charset){
		HttpClient httpsClient = new SSLClient();
		HttpPost	httpPost = new HttpPost(url);
		String result = null;
		try{
			//设置参数
			if(map!=null){
				setList(map, charset, httpPost);
			}
			HttpResponse response = httpsClient.execute(httpPost);
			return result(charset, response);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 为post请求设置参数
	 * @param map
	 * @param charset
	 * @param httpPost
	 * @throws UnsupportedEncodingException
	 */
	private static void setList(Map<String, String> map, String charset, HttpPost httpPost)
			throws UnsupportedEncodingException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,String> elem = (Entry<String, String>) iterator.next();
			list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
		}
		if(list.size() > 0){
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
			httpPost.setEntity(entity);
		}
	}


	
	public static String doHttpsGet(String url,Map<String,String> map,String charset){
		HttpClient httpsClient = new SSLClient();
		HttpGet httpGet = new HttpGet(url);
		String result = null;
		try{
			if(null!=map){
				HttpParams params = httpsClient.getParams();  
				//设置参数
				Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String,String> elem = (Entry<String, String>) iterator.next();
					params.setParameter(elem.getKey(), elem.getValue());  
				}
				httpGet.setParams(params);
			}
			HttpResponse response = httpsClient.execute(httpGet);
			return result(charset, response);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public static String doGet(String url,Map<String,String> map,String charset){
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		String result = null;
		try{
			if(null!=map){
				HttpParams params = httpClient.getParams();  
				//设置参数
				Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String,String> elem = (Entry<String, String>) iterator.next();
					params.setParameter(elem.getKey(), elem.getValue());  
				}
				httpGet.setParams(params);
			}
			HttpResponse response = httpClient.execute(httpGet);
			return result(charset, response);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}




	public static String httpPostWithJSON(String url, String json,String charset) throws Exception {
        
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        
        // 将JSON进行UTF-8编码,以便传输中文
        StringEntity se = new StringEntity(json,charset);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
        httpPost.setEntity(se);
        HttpResponse response = httpClient.execute(httpPost);
        return result(charset, response);
    }
	
	public static String httpsPostWithJSON(String url, String json,String charset) throws Exception {
        
        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        
        // 将JSON进行UTF-8编码,以便传输中文
        StringEntity se = new StringEntity(json,charset);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
        httpPost.setEntity(se);
        HttpResponse response = httpClient.execute(httpPost);
        return result(charset, response);
    }
/*	public static void main(String[] args) {
		Map m = new HashMap<String, String>();
		m.put("wd", "美女");
				
	String string =	new HttpClientUtils().doPost("http://www.baidu.com", m, "utf-8");
	System.out.println(string);
	}*/


	private static String result(String charset,
			HttpResponse response) throws IOException {
		String result = null;
		if(response != null){
			HttpEntity resEntity = response.getEntity();
			if(resEntity != null){
				result = EntityUtils.toString(resEntity,charset);
			}
		}
		return result;
	}
}
