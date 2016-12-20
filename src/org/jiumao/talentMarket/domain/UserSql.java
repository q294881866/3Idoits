package org.jiumao.talentMarket.domain;

import jdbcUtils.QueryHelper;

public class UserSql extends QueryHelper{
	
	private String alias = " user ";//注意空格
	private String userName = " `userName` as userName" ;
	private String password = " `password` as password" ;

	public UserSql(String table) {
		super.fromClause = " FROM " + table + " " + alias;
	}
	
	public static String findUserByUserNameAndPassword = //
			"select id as id,userName as userName,password as password,nickName as nickName,email as email,phone as phone,gender as gender from user"
			+ " where  userName = ? and password = ?";
	
					
}
