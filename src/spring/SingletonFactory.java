package spring;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


	


import java.util.Vector;

public class SingletonFactory {
	private static SingletonFactory instance = null;  
    private  Vector properties = null;  
    private static  Resource cr = null; 
    private static XmlBeanFactory bf=null; 
	
    public  Vector getProperties() {  
        return properties;  
    }  
  
    private SingletonFactory() {  
    }  
  
    private static synchronized void syncInit(String springXml) {  
        if (instance == null) {  
            instance = new SingletonFactory();  
            cr = new ClassPathResource("applicationContext.xml");
            bf=new XmlBeanFactory(cr); 
        }  
    }  
  
    public  void setProperties(Vector properties) {
		this.properties = properties;
	}

	public static SingletonFactory getInstance(String springXml) {  
        if (instance == null) {  
            syncInit(springXml);  
        }  
        return instance;  
    }  
  
    public void updateProperties() {  
        SingletonFactory shadow = new SingletonFactory();  
        properties = shadow.getProperties();  
    }  
}
