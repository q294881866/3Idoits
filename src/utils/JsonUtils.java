package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {

	/**
	 * 
	 * @param jsonString
	 * 		字符串类型的json
	 * @param cls
	 * 		对象的class
	 * @return
	 * 		返回JavaBean
	 */
	 public static <T> T getBean(String jsonString, Class<T> cls) {
	        T t = null;
	        try {
	            Gson gson = new Gson();
	            t = gson.fromJson(jsonString, cls);
	        } catch (Exception e) {
	            // TODO: handle exception
	        }
	        return t;
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
	public static <T> List<T> getBeans(String jsonString, Class<T> cls) {
	        List<T> list = new ArrayList<T>();
	            Gson gson = new Gson();
	            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {}.getType());//FIXME 这里有错误泛型问题
	        return list;
	    }
	
	/**
	 * 
	 * @param t
	 * 		对象转换为json字符串
	 * @return
	 * 		jsonString
	 */
	public static <T> String bean2JsonString(T t){
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
	public static String getJsonField(String jsonString,String key) throws JSONException{
		org.json.JSONObject json = new org.json.JSONObject(jsonString);
		//以code=100000为例，参考图灵机器人api文档
		return json.getString(key);
	}
}
