package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 本项目采用Google的 gson开源工具包<br>
 * 来处理json对象，特点小巧方便，善于处理复杂对象<br>
 * 善于处理小数据,本人对Google推崇<br>
 * @author Administrator
 * 		接收http请求
 *
 */
public abstract class ActionSupport<T> {
	
	protected Class<T> clazz;
	protected T model;
	protected String requestString ;
	protected org.json.JSONObject json  ;
	public ActionSupport() {
		
	}


	protected HttpServletRequest request;
	protected HttpServletResponse response;
	public void setRequestAndResponse(HttpServletRequest req,HttpServletResponse res) {
		this.request = req;
		this.response = res;
		//获取传入的字符流
		getStringBuffer();
	}
	
	/**
	 * 获取request流对象
	 * @return 
	 * 		返回string 字符串
	 */
	private String getStringBuffer() {
        try {  
        	
        	  //处理接收消息  
        	System.out.println("request=="+request);
    		StringBuffer sb = new StringBuffer();
    		InputStream is = request.getInputStream();
    		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
    		BufferedReader br = new BufferedReader(isr);
    		String s = "";
    		while ((s = br.readLine()) != null) {
    			sb.append(s);
    		}
    		requestString = sb.toString();	//次即为接收到微信端发送过来的xml数据
    		System.out.println("requestString=="+requestString);
    		br.close();
            
           return requestString;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return null;  
	} 
	
	/**
	 * 给请求返回json格式的字符串
	 * @param json
	 */
	protected void responseJsonResult(String json) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(json);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param requestString
	 * 		字符串类型的json
	 * @param cls
	 * 		对象的class
	 * @return
	 * 		返回JavaBean
	 */
	 public   T getBean() {
		 if (null == requestString) 
			 return null;
	        try {
	            Gson gson = new Gson();
	            model = (T) gson.fromJson(requestString, clazz);
	            System.out.println("model == "+model);
	        } catch (Exception e) {
	            // TODO: handle exception
	        }
	        return model;
	    }
	 
		/**
		 * 
		 * @param requestString
		 * 		字符串类型的json
		 * @param cls
		 * 		对象的class
		 * @return
		 * 		返回JavaBean
		 */
		 @SuppressWarnings("unchecked")
		public   Object getBean(Class clazz) {
			 if (null == requestString) 
				 return null;
		        try {
		            Gson gson = new Gson();
		            return gson.fromJson(requestString, clazz);
		        } catch (Exception e) {
		            // TODO: handle exception
		        }
		        return null;
		    }
		 

	 /**
		 * 
		 * @param jsonString
		 * 		字符串类型的json
		 * @param cls
		 * 		对象的class
		 * @return
		 * 		返回list集合的bean
		 */
	public   List<T> getBeans() {
		 if (null == requestString) 
			 return null;
	        List<T> list = new ArrayList<T>();
	            Gson gson = new Gson();
	            list = gson.fromJson(requestString, new TypeToken<List<T>>() {}.getType());
	        return list;
	    }
	
	/**
	 * 获取键值对
	 * @return
	 * //FIXME 这个方法有错误
	 */
	@Deprecated
	public  List<Map<String, Object>> listKeyMaps() {
	        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	            Gson gson = new Gson();
	            list = gson.fromJson(requestString,
	                    new TypeToken<List<Map<String, Object>>>() {
	                    }.getType());
	        return list;
	    }
	
	/**
	 * 
	 * @param t
	 * 		对象转换为json字符串
	 * @return
	 * 		jsonString
	 */
	public   String bean2JsonString(T t){
		return new Gson()//
			.toJson(t);
	}
	
	/**
	 * 
	 * @param key
	 * 		json中的字段
	 * @return
	 * @throws JSONException 
	 */
	public  String getJsonField(String key) throws JSONException{
		
		//以code=100000为例，key为code
		return json.getString(key);
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}
}
