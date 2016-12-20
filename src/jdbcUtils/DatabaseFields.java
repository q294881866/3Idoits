package jdbcUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.MyStringUtils;

/**
 * 数据库字段
 * @author Administrator
 *
 */
public class DatabaseFields {
	
	private static long id = 0;
	
	public static void main(String[] args) {
		System.out.println(Type.UNKNOWNSEX);
	}
	
	/**
	 * 获取唯一标识符
	 *  ***GUID是一个128位长的数字，一般用16进制表示。算法的
	 * 核心思想是结合机器的网卡、当地时间、一个随即数来生成GUID。
	 * 从理论上讲，如果一台机器每秒产生10000000个GUID，
	 * 则可以保证（概率意义上）3240年不重复。
	 * @author Administrator
	 * @return
	 * 		A randomly generated UUID
	 */
	public static String getUUID(){
		return java.util.UUID.randomUUID().toString();
	}
	
	/**
	 * 生成一个唯一的主键
	 * @return
	 * 		整形的主键
	 */
	public static synchronized long getId(){
		return id++;
	}
	
	/**
	 * 获取系统当前时间毫秒
	 * @return
	 * 			获得的是自1970-1-01 00:00:00.000 到当前时刻的时间距离,类型为long
	 */
	public static long getTime(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 将毫秒转成需要的格式
	 * @param time
	 * 			时间毫秒 long
	 *  @param format
	 *  			yyyy-MM-dd、yyyyMMdd 、yyyy年MM月dd日 HH时mm分ss秒 E
	 * @return
	 * 			 
	 */
	public String refFormatNowDate(long time,String format) {
		  Date nowTime = new Date(time);
		  SimpleDateFormat sdFormatter = new SimpleDateFormat(format);
		  return sdFormatter.format(nowTime);
	}
	
	/**
	 * 数据字典
	 * @author Administrator
	 * @return 
	 *			返回int类型
	 */
    public enum Type {

    	/**男*/
    	MAN(1), 
    	/**女生*/
    	WOMEN(3), 
    	/**不暴露性别*/
    	UNKNOWNSEX(2),
    	/**大学生*/
    	DEGREE_UNIVERSITY(2),
    	/**大专生*/
    	DEGREE_DAZHUAN(1),
    	/**大专以下学历*/
    	DEGREE_UNDER_DAZHUAN_OR_NO(0),
    	/**硕士*/
    	DEGREE_MASTER(3),
    	/**博士生*/
    	DEGREE_DOCTOR(4),
    	/**科学家，研究员*/
    	DEGREE_SCIENTIST(5),
    	/**科学家，各国最高研究院院士*/
    	DEGREE_UP_SCIENTIST(6),
    	/**工作经验没有*/
    	WORK_AGE_NO(0),
    	/**工作1-2年*/
    	WORK_AGE_1_2(1),
    	/**工作3_5年*/
    	WORK_AGE_3_5(2),
    	/**工作6_7年*/
    	WORK_AGE_6_7(3),
    	/**工作8_10年*/
    	WORK_AGE_8_10(4),
    	/**工作11_15年*/
    	WORK_AGE_11_15(5),
    	/**工作15年以上*/
    	WORK_AGE_OVER_15(6),
    	/**是*/
    	YES(1),
    	/**否*/
    	NO(0);

        // 定义私有变量
        private int nCode;

        // 构造函数，枚举类型只能为私有
        private Type(int _nCode) {
            this.nCode = _nCode;
        }

        @Override
        public String toString() {
            return ""+this.nCode;
        }

    }

    /**
     * 程序部署的时候设置id
     * @param id
     */
	public static void setId(long id) {
		DatabaseFields.id = id;
	}
    
    

}
