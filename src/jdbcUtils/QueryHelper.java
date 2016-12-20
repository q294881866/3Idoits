package jdbcUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * 用于辅助拼接Sql语句
 * 
 * @author tyg
 * 
 */
public abstract class QueryHelper {
	/** "  , "*/
	protected String dot = " ,";//注意是否需要空格
	/** " select"*/
	protected String select =  "select ";//注意是否需要空格
	/** " from"*/
	protected String from = " from ";
	/**  " where "	 */
	protected String where = " where ";
	/** " AND "*/
	protected String and = " AND ";
	
	protected String fromClause; // FROM子句
	protected String whereClause = ""; // Where子句
	protected String orderByClause = ""; // OrderBy子句



	/**
	 * 拼接Where子句
	 * 
	 * @param condition
	 * @param params
	 */
	public QueryHelper addCondition(String condition) {
		// 拼接
		if (whereClause.length() == 0) {
			whereClause = " WHERE " + condition;
		} else {
			whereClause += " AND " + condition;
		}


		return this;
	}

	/**
	 * 如果第一个参数为true，则拼接Where子句
	 * 
	 * @param append
	 * @param condition
	 * @param params
	 */
	public QueryHelper addCondition(boolean append, String condition) {
		if (append) {
			addCondition(condition);
		}
		return this;
	}

	/**
	 * 拼接OrderBy子句
	 * 
	 * @param propertyName
	 *            参与排序的属性名
	 * @param asc
	 *            true表示升序，false表示降序
	 */
	public QueryHelper addOrderProperty(String propertyName, boolean asc) {
		if (orderByClause.length() == 0) {
			orderByClause = " ORDER BY " + propertyName + (asc ? " ASC" : " DESC");
		} else {
			orderByClause += ", " + propertyName + (asc ? " ASC" : " DESC");
		}
		return this;
	}

	/**
	 * 如果第一个参数为true，则拼接OrderBy子句
	 * 
	 * @param append
	 * @param propertyName
	 * @param asc
	 */
	public QueryHelper addOrderProperty(boolean append, String propertyName, boolean asc) {
		if (append) {
			addOrderProperty(propertyName, asc);
		}
		return this;
	}

	public String getConditions(){
		return fromClause + whereClause + orderByClause; 
	}



}
