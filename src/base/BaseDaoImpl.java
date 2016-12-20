package base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import jdbcUtils.core.MySqlSessionFactory;
import jdbcUtils.core.SessionFactory;

@SuppressWarnings("unchecked")
public  class BaseDaoImpl<T extends BaseBean> implements BaseDao<T> {

	/**
	 * MySQL写服务器连接工程
	 */
	protected static SessionFactory mySqlWriterSessionFactory = MySqlSessionFactory
			.getSessionFactory("jdbc:mysql://127.0.0.1:3306/foolish", "root",
					"", "com.mysql.jdbc.Driver");

	private Class<T> clazz;
	private Class<?> SqlClazz ; 
	protected T model;
	int resultset = -1;//设置数据库更新为失败

	public BaseDaoImpl() {
		// 使用反射技术得到T的真实类型
		ParameterizedType pt = (ParameterizedType) this.getClass()
				.getGenericSuperclass(); // 获取当前new的对象的 泛型的父类 类型
		this.clazz = (Class<T>) pt.getActualTypeArguments()[0]; // 获取第一个类型参数的真实类型
		System.out.println("clazz ---> " + clazz);
		try {
			System.err.println(clazz.toString().replace("class ", ""));
			SqlClazz = Class.forName(clazz.toString().replace("class ", "")+"Sql");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int save(Object... parameters) throws Exception {
		Field field = SqlClazz.getField("save");
		System.out.println((String)field.get(SqlClazz));
		resultset = mySqlWriterSessionFactory.update((String)field.get(SqlClazz),parameters);
		
		return resultset;
	}

	public int update(String sql,Object... parameters) throws Exception {
		return mySqlWriterSessionFactory.update(sql, parameters);
	}

	public int delete(Integer id) throws Exception {
		Field field = SqlClazz.getField("deleteById");
		System.out.println((String)field.get(SqlClazz));
		resultset = mySqlWriterSessionFactory.update((String)field.get(SqlClazz), id);
		
		return resultset;
	}

	public T getById(Integer id) throws Exception {
		Field field = SqlClazz.getField("getById");
		System.out.println((String)field.get(SqlClazz));
		model = (T) mySqlWriterSessionFactory.getObject((String)field.get(SqlClazz), clazz, id);
		
		return model;
	}

	public List getByIds(Integer[] ids) throws Exception {
		Field field = SqlClazz.getField("getByIds");
		System.out.println((String)field.get(SqlClazz));
		List list = mySqlWriterSessionFactory.getObjects((String)field.get(SqlClazz), clazz, ids);
		
		return list;
	}

	public List findAll(int fistResult, int pageSize) throws Exception {
		Field field = SqlClazz.getField("findAll");
		System.err.println(field);
		List<Object> list =mySqlWriterSessionFactory.getObjects((String)field.get(SqlClazz), clazz, fistResult,pageSize);
		
		return list;
	}

	@Override
	public int updateById(Integer id, Object... parameters) throws Exception {
		Connection con = mySqlWriterSessionFactory.getSession();
		Field field = SqlClazz.getField("updateById");
		System.out.println((String) field.get(SqlClazz));
		PreparedStatement ps = con.prepareStatement((String) field
				.get(SqlClazz));
		for (int i = 0; i < parameters.length; i++) {
			ps.setObject(i + 1, parameters[i]);
		}
		ps.setInt(1, id);
		int rs = ps.executeUpdate();
		// 关闭数据库
		mySqlWriterSessionFactory.close();
		return rs;
	}

	@Override
	public List<Integer> findAllId() throws Exception {
		List<Integer> integers = new ArrayList<>();
		Connection con = mySqlWriterSessionFactory.getSession();
		Field field = SqlClazz.getField("findAllId");
		System.out.println((String) field.get(SqlClazz));
		PreparedStatement ps = con.prepareStatement((String) field
				.get(SqlClazz));
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			integers.add(rs.getInt(1));
			
		}
		// 关闭数据库
		mySqlWriterSessionFactory.close();
		return integers;
	}



	// 公共的查询分页信息的方法（最终版）
	/*public PageBean getPageBean(int pageNum, int pageSize,
			QueryHelper queryHelper) {
		System.out
				.println("-------> DaoSupportImpl.getPageBean( int pageNum, int pageSize, QueryHelper queryHelper )");

		// 参数列表
		List<Object> parameters = queryHelper.getParameters();

		// 查询本页的数据列表
		Query listQuery = getSession().createQuery(
				queryHelper.getListQueryHql()); // 创建查询对象
		if (parameters != null) { // 设置参数
			for (int i = 0; i < parameters.size(); i++) {
				listQuery.setParameter(i, parameters.get(i));
			}
		}
		listQuery.setFirstResult((pageNum - 1) * pageSize);
		listQuery.setMaxResults(pageSize);
		List list = listQuery.list(); // 执行查询

		// 查询总记录数量
		Query countQuery = getSession().createQuery(
				queryHelper.getCountQueryHql());
		if (parameters != null) { // 设置参数
			for (int i = 0; i < parameters.size(); i++) {
				countQuery.setParameter(i, parameters.get(i));
			}
		}
		Long count = (Long) countQuery.uniqueResult(); // 执行查询

		System.out.println(list + "paging list");
		return new PageBean(pageNum, pageSize, count.intValue(), list);
	}*/
//	public static void main(String[] args) {
//		try {
//			Field field = EmployeeSql.class.getField("findUserByUserNameAndPassword");
//			System.out.println((String)field.get(EmployeeSql.class));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
