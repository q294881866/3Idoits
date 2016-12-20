package base;

import java.io.Serializable;

/**
 * 
 * @author ppf@jiumao.org
 *
 */
public class BaseBean implements Serializable{


	/**
	 * 设置Sql语句需要的参数，并符合Sql占位符？的顺序
	 * @param objects
	 * 			按默认顺序排，如果需要可以自定义Sql参数顺序
	 */
//	void setParameters(Object[] objects);
	/**
	 * 获取Sql需要的参数<br>
	 * 并设置默认参数顺序<br>
	 * 例如：
	 * <blockquote>id,name,password<br>
	 * 则参数按此顺序输出:<br>objects[0]=id,objects[1]=name,objects[2]=password
	 * </blockquote>
	 * @return
	 */
//	Object[] getParameters();
}
