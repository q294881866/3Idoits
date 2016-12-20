package jdbcUtils.core;

/**
 * Java异常创建练习
 * example 数据库异常
 * @author Administrator
 *
 */
public class DaoException extends RuntimeException {

	/**
	 * 构造方法又称构造器，不写默认在类加载时注入一个无参构成方法
	 * 默认的构造方法建议每次都写上
	 * 如果有有参数的构造方法，则不默认生成无参构成方法
	 */
	public DaoException() {
		// TODO 这个注释表明这里没有完成 在Task任务栏可以找到
		
	}

	/**
	 * 参考super父类的注释
	 * @param message
	 */
	public DaoException(String message) {
		super(message);
		
	}

	/**
	 * 参考super父类的注释
	 * @param message
	 */
	public DaoException(Throwable cause) {
		super(cause);
	}

	/**
	 * 参考super父类的注释
	 * @param message
	 */
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

}

