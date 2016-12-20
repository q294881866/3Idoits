package spring;

import java.lang.reflect.Method;

public class MyAdvice implements Advice {
	long beginTime;
	long endTime;
	
	@Override
	public void beforeMethod(Method method) {
		beginTime = System.currentTimeMillis();
		System.out.println( "beginTime = "+beginTime);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void afterMethod(Method method) {
		endTime = System.currentTimeMillis();
		System.out.println( "endTime = "+endTime);
		System.out.println(method.getName() + " running time of " + (endTime - beginTime));
	}

}
