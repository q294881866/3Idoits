package org.jiumao.redis;


public class SingletonFactory {
	private static SingletonFactory instance = null;  
  
    private SingletonFactory() {  
    }  
  
    private static synchronized void syncInit() {  
        if (instance == null) {  
            instance = new SingletonFactory();  
        }  
    }  
  

	public static SingletonFactory getInstance() {  
        if (instance == null) {  
            syncInit();  
        }  
        return instance;  
    }  
  
}
