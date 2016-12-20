package jdbcUtils.core;

/**
 * 桥接模式
 * 对应不同的数据源提供不同的数据库连接
 */
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SessionFactory {
	private DataSource dataSource;
	private Connection connection;

	private SessionFactory() {
	}

	public SessionFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getSession() {
		try {
			this.connection = dataSource.getConnection();
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把连接放回去
	 */
	public void close() {
		dataSource.free(connection);
	}
	

	/**
	 * 开启事务 并设置事务隔离级别 level one of the following Connection constants:
	 * Connection.TRANSACTION_READ_UNCOMMITTED,
	 * Connection.TRANSACTION_READ_COMMITTED,
	 * Connection.TRANSACTION_REPEATABLE_READ, or
	 * Connection.TRANSACTION_SERIALIZABLE. (Note that
	 * Connection.TRANSACTION_NONE cannot be used because it specifies that
	 * transactions are not supported.)
	 * 
	 * @param level
	 */
	public void startTransaction(int level) {
		try {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(level);
		} catch (SQLException e) {
			new DaoException("事务开启时异常", e);
		}
	}

	/**
	 * 提交事务
	 */
	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			new DaoException("事务提交异常", e);
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */

	public List<Object> getObjects(String sql, Class clazz,Object... parameters)
			throws  Exception {
		if (null == sql) {
			throw new RuntimeException("sql is null");
		}
		Connection conn =  getSession();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement(sql);
		
		if (null!=parameters) {
			//设置参数
			for (int i = 0; i < parameters.length; i++) {
				ps.setObject(i+1, parameters[i]);
			}
		}
		rs = ps.executeQuery();

		List<Object> objects = new ArrayList<Object>();
		Method[] ms = clazz.getMethods();
		while (rs.next()) {
				
			objects.add(getBean(clazz, rs));
		}
		return objects;
	}

	private Object getBean(Class clazz, ResultSet rs)
			throws IntrospectionException, InstantiationException,
			IllegalAccessException, SQLException, InvocationTargetException {
		if (null == clazz&&null == rs) {
			return null;
		}
		String[] colNames = getColNames(rs);
		

		Object object = clazz.newInstance();
		if (rs.next()) {
			for (int i = 0; i < colNames.length; i++) {
				String colName = colNames[i];
				PropertyDescriptor pd2 = new PropertyDescriptor(colName,clazz);
				Method methodSetX = pd2.getWriteMethod();
				System.err.println(colName+"="+rs.getObject(colName));
				methodSetX.invoke(object,rs.getObject(colName));
			}
		}
		close();//关闭连接
		return object;
	}

	private String[] getColNames(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		String[] colNames = new String[count];
		for (int i = 1; i <= count; i++) {
			colNames[i - 1] = rsmd.getColumnLabel(i);
		}
		return colNames;
	}

	public Object getObject(String sql, Class clazz,Object... parameters) throws SQLException,
			Exception, IllegalAccessException, InvocationTargetException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn =  getSession();
		ps = conn.prepareStatement(sql);
		if (null!=parameters) {
			//设置参数
			for (int i = 0; i < parameters.length; i++) {
				ps.setObject(i+1, parameters[i]);
			}
		}
		rs = ps.executeQuery();
		
		return getBean(clazz, rs);
	}

	public int update(String sql,Object... parameters) throws Exception{
		Connection conn = getSession();
		PreparedStatement ps = null;
		ps = conn.prepareStatement(sql);
		if (null!=parameters) {
			//设置参数
			for (int i = 0; i < parameters.length; i++) {
				ps.setObject(i+1, parameters[i]);
			}
		}
		return ps.executeUpdate();
	}
}
