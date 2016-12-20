package test;

import jdbcUtils.DatabaseFields;

public class Test4DBfields {

	public static void main(String[] args) {
//		System.out.println(DatabaseFields.getUUID());
//		System.out.println(DatabaseFields.getTime());
//		System.out.println(Long.MAX_VALUE);
		
		//同时启动1000个线程，去进行i++计算，看看实际结果  
        for (int i = 0; i < 1000; i++) {  
            new Thread(new Runnable() {  
                @Override  
                public void run() {  
                	System.out.println(DatabaseFields.getTime());  
                }  
            }).start();  
        }  
	}
}
