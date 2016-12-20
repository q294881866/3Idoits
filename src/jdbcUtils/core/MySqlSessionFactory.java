package jdbcUtils.core;


public class MySqlSessionFactory {
	
	/**
	 * 设计模式的一种facade
	 * 外观设计模式
	 * @param url
	 * @param user
	 * @param password
	 * @param driver
	 * @return
	 */
	public static SessionFactory getSessionFactory(String url,String user,String password,String driver){
		MyJdbc jdbc = new MyJdbc(url, user, password, driver);
		DataSource dataSource = new DataSource(jdbc);
		return new SessionFactory(dataSource);
	}
	
	
}
