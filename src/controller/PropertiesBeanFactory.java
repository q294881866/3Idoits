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
		//��ȡ�����ļ����õ�����DAO��ʵ������
		try {
			Properties props = new Properties();
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configurationFile));
			
			//��������ļ�����ʼ�����е�DAO����
			Set set = props.entrySet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String)entry.getKey(); //DAO�����
				String className = (String)entry.getValue(); //ȫ·������
				Class clz = Class.forName(className);
				Object bean = clz.newInstance(); //Ԥ�ȴ����õ�DAO����
				beans.put(key, bean); //����DAO����
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public Object getBean(String name){
		return beans.get(name);
	}
}
