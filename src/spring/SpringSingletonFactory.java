package spring;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;



public class SpringSingletonFactory {
    private static  Resource cr = null; 
    private static XmlBeanFactory bf=null; 
	
    Logger logger = Logger.getLogger(SpringSingletonFactory.class);
    private SpringSingletonFactory() {  
    	logger.debug("初始化 applicationContext.xml");
    }  
  
    private static synchronized void syncInit() {  
        if (cr == null) {  
        	
            cr = new ClassPathResource("applicationContext.xml");
            bf=new XmlBeanFactory(cr); 
        }  
    }  
  

	
	public static Object getBean(String beanName){
		if (bf == null) {  
            syncInit();  
        }  
		Logger.getLogger(SpringSingletonFactory.class).info("spring获取bean情况=="+beanName);
		return bf.getBean(beanName);
	}
  
	
}
