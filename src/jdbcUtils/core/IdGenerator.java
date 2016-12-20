package jdbcUtils.core;
/**
 * 主键从0开始每次+1，需要并发线程安全控制。
 * 
 * @author Administrator
 * 			可以用redis的一个键控制，如<userId,11>,读取之后每次更新11+1
 *
 */
public interface IdGenerator {
	 long getId();
}
