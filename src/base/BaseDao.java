package base;

import java.util.List;

public interface BaseDao<T extends BaseBean> {

	/**
	 * 保存实体
	 * 
	 * @param entity
	 */
	int save(Object... parameters) throws Exception;

	/**
	 * 删除实体
	 * 
	 * @param id
	 * @throws Exception 
	 */
	int delete(Integer id) throws Exception;

	/**
	 * 更新实体
	 * 
	 * @param entity
	 */
	int update(String sql,Object... parameters) throws Exception;
	/**
	 * 更新实体通过id
	 * 
	 * @param entity
	 */
	int updateById(Integer id,Object... parameters) throws Exception;

	/**
	 * 按id查询
	 * 
	 * @param id
	 * @return
	 */
	T getById(Integer id) throws Exception;

	/**
	 * 按id查询
	 * 
	 * @param ids
	 * @return
	 */
	List<T> getByIds(Integer[] ids) throws Exception;

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<T> findAll(int fistResult, int pageSize) throws Exception;
	
	
	/**
	 * 默认的实现是arrayList，如果有频繁的删除操作建议重写
	 * @return
	 * 		返回所有的id用于辅助redis做分页操作
	 * @throws Exception
	 */
	List<Integer> findAllId() throws Exception;
	
	/**
	 * 公共的查询分页信息的方法（最终版）
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param queryHelper
	 *            HQL语句与参数列表
	 * @return
	 */
//	PageBean getPageBean(int pageNum, int pageSize, QueryHelper queryHelper);




}
