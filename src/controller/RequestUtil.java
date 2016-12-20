package controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

public class RequestUtil {
	
	/**
	 * 将request中的所有参数设置到entityClass类型的对象上
	 * @param entityClass
	 * @param request
	 * @return
	 */
	public static Object copyParam(Class entityClass,HttpServletRequest request){
		
		try {
			Object entity = entityClass.newInstance();
			
			//把请求中的参数取出
			Map allParams = request.getParameterMap();
			Set entries = allParams.entrySet();
			for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String name = (String)entry.getKey();
				Object[] value = (Object[])entry.getValue();
				
				if(value != null){
					if(value.length == 1){
						BeanUtils.copyProperty(entity, name, value[0]);
					}else{
						BeanUtils.copyProperty(entity, name, value);
					}
				}
			}
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
