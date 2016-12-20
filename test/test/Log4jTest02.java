package test;

import org.apache.log4j.Logger;

public class Log4jTest02 {

 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//配置变量
		System.setProperty("LOG_DIR", "d:/temp/log");
		
		Logger logger = Logger.getLogger(Log4jTest02.class);		
		
		logger.debug("这是一段DEBUG信息");
		logger.info("这是一段INFO信息");
		logger.warn("这是一段WARN信息");
		logger.error("这是一段ERROR信息");
		logger.fatal("这是一段FATAL信息");
	}

}
