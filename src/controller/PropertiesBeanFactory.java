package controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesBeanFactory implements BeanFactory {
	Map beans = new HashMap();
	
	public PropertiesBeanFactory(){
		this("beans.properties");
	}	
	
	public PropertiesBeanFactory(String configurationFile){
		
		try {
			Properties props = new Properties();
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configurationFile));
			
			
			Set set = props.entrySet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String)entry.getKey(); 
				String className = (String)entry.getValue(); 
				Class clz = Class.forName(className);
				Object bean = clz.newInstance(); 
				beans.put(key, bean);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public Object getBean(String name){
		return beans.get(name);
	}
}
