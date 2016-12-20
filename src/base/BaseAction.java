package base;


import java.lang.reflect.ParameterizedType;

import spring.SpringSingletonFactory;
import controller.ActionSupport;

public abstract class BaseAction<T extends BaseBean> extends ActionSupport<T> {

	// =============== ModelDriven的支持 ==================
	public BaseAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected BaseService baseService;

	protected void getClazzName() {
		
		String clazzSimpleName = clazz.getSimpleName();
		clazzSimpleName = clazzSimpleName.substring(0, 1).toLowerCase()
				+ clazzSimpleName.substring(1) + "Service";
		baseService = (BaseService<T>) SpringSingletonFactory
				.getBean(clazzSimpleName);
	}

}
