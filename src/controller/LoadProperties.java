package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

/**
 * 加载actions.properties配置文件
 * action名与类一一对应
 * @author Administrator
 *
 */
class LoadProperties {
	private static InputStream ips ;
	private static Properties props;
	private static Map<String, Class>  nameWithClass ;
	static {
		ips = LoadProperties.class.getResourceAsStream("actions.properties");
		System.out.println("init actions.properties");
		props = new Properties();
		try {
			props.load(ips);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//加载类
		loadClass() ;
	}
	
	private LoadProperties() {
		
	}
	
	public static Object getBean(String name){
			try {
				return nameWithClass.get(name).newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
	
	/**
	 * 加载类
	 * class.forName
	 */
	private static void loadClass() {
		Iterator<Entry<Object, Object>> it = props.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			String key = (String) entry.getKey();
			Class value = null;
			try {
				value = Class.forName((String) entry.getValue());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//加载类
			nameWithClass = new HashMap<String, Class>();
			nameWithClass.put(key, value);

		}
	}
}
