package jdbcUtils.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyJdbc {

	private String url = "jdbc:mysql://112.73.64.170:3306/test";
	private String user = "root";
	private String password = "peipengfei";
	private String driver = "com.mysql.jdbc.Driver";

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException {

		return DriverManager.getConnection(url, user, password);
	}

	private MyJdbc() {
	}

	MyJdbc(String url, String user, String password, String driver) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.driver = driver;
		try {
			Class.forName(driver);
			System.out.println(driver);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param rs
	 * @param st
	 * @param conn
	 */
	static void free(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
	}
}
