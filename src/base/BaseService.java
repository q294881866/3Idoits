package base;

import java.util.List;


public interface BaseService<T extends BaseBean> {
	/**
	 * 保存实体
	 * 
	 * @param entity
	 */
	int save(Object... parameters) throws Exception ;

	/**
	 * 删除实体
	 * 
	 * @param id
	 */
	int delete(Integer id) throws Exception ;

	/**
	 * 更新实体
	 * 
	 * @param entity
	 */
	int update(String sql,Object... parameters) throws Exception ;
	
	/**
	 * 更新实体通过id
	 * 
	 * @param entity
	 */
	int updateById(Integer id,Object... parameters) throws Exception;

	/**
	 * 按id查询
	 * 
	 * @param integer
	 * @return
	 */
	T getById(Integer integer) throws Exception ;

	/**
	 * 按id查询
	 * 
	 * @param ids
	 * @return
	 */
	List<T> getByIds(Integer[] ids) throws Exception ;

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<T> findAll(int pageNum, int pageSize) throws Exception ;


//	PageBean getPageBean(int pageNum, int pageSize);
	
}
