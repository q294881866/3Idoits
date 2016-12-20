package jdbcUtils.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 配合redis分页实现，<br>
 * 用Java的ArrayList缓存数据库的id这样可以轻松实现分页<br>
 * 每个数据库的不同表id用不同的实例来缓存<br>
 * 要求每次服务器重启时，执行。4字节的int类型500w条是20MB
 * 
 * @author Administrator
 *
 */
public class IdCache4Java {
	

}

