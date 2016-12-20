package jdbcUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import utils.DateTrans;



public class DBUtil {

	public static Log log = LogFactory.getLog(DBUtil.class);
	private static Connection conn;

	/**
	 * 给这个类复制Connect
	 * 这个不需要关闭，在Sessionfactory对象中指的是这个对象，close就可以释放连接
	 * @param connection
	 */
	public static void setConnection(Connection connection){
		conn = connection;
	}
	
	private static Connection getConnection() throws SQLException {
		return conn;

	}

	/**
	 * 
	 * @param rs
	 *            结果
	 * @return 后得到的是 String[]
	 */
	public static String[] getStrs(String sql) {
		String[] strs = null;
		Connection conn = null;

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			log.info("sql:" + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			strs = RSUtil.getStrs(rs);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行DBUtil方法异常");
		} finally {
			releaseDB(rs, ps, conn);
		}
		return strs;
	}

	/**
	 * 查询CLOB字段并转化为字符串
	 * 
	 * @param sql
	 *            查询SQL
	 * @return String，如果未查询到结果集则返回null
	 */
	public static String getClobToStr(String sql, List<SqlParam> in_params) {
		String str = null;
		Connection conn = null;

		ResultSet rs = null;
		PreparedStatement ps = null;

		Reader inStream = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			log.info("sql:" + sql);
			if (in_params != null) {
				for (int i = 0; i < in_params.size(); i++) {
					SqlParam param = (SqlParam) in_params.get(i);
					int type = param.getParam_type();
					String value = param.getParam_value();
					switch (type) {
					case SqlTypes.CHAR:
						ps.setString(i + 1, value);
						break;
					case SqlTypes.VARCHAR:
						ps.setString(i + 1, value);
						break;
					case SqlTypes.INTEGER:
						ps.setInt(i + 1, Integer.parseInt(value));
						break;
					case SqlTypes.DOUBLE:
						ps.setDouble(i + 1, Double.parseDouble(value));
						break;
					case SqlTypes.BIGINT:
						ps.setLong(i + 1, Long.parseLong(value));
						break;
					default:
						throw new RuntimeException(
								"###: SqlParam参数异常，目前只支持String,number两大类数据类型");

					}
				}
			}

			rs = ps.executeQuery();
			if (rs.next()) {
				Clob clob = rs.getClob(1);
				inStream = clob.getCharacterStream();
				char[] c = new char[(int) clob.length()];
				inStream.read(c);
				str = new String(c);
				inStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行DBUtil方法异常");
		} finally {
			releaseDB(rs, ps, conn);
		}

		return str;
	}

	/**
	 * 
	 * @param rs
	 *            二维结果
	 * @return list.get() 后得到的是 String[]
	 */
	public static List<String[]> getStrsList(String sql) {
		List<String[]> ls = new ArrayList<String[]>();
		Connection conn = null;

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection2();
			log.info("sql:" + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ls = RSUtil.getStrsList(rs);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行DBUtil方法异常");
		} finally {
			releaseDB(rs, ps, conn);
		}
		return ls;
	}

	/**
	 * 
	 * @param rs
	 *            结果
	 * @return long 后得到的是 count(1)
	 */
	public static long getCount(String sql) {
		long cnt = -1;

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			if (log.isInfoEnabled()) {
				log.info("sql:" + sql);
			}

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			cnt = RSUtil.getCount(rs);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行DBUtil方法异常");
		} finally {
			releaseDB(rs, ps, conn);
		}
		return cnt;
	}

	public static List<String[]> getStrsListLabel(String sql) {
		List<String[]> ls = new ArrayList<String[]>();

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			log.info("sql:" + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ls = RSUtil.getStrsListLabel(rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行DBUtil方法异常");
		} finally {
			releaseDB(rs, ps, conn);
		}
		return ls;
	}

	public static void releaseDB(ResultSet rs, PreparedStatement ps,
			Connection conn) {
		try {
			if (null != rs)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != ps)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != conn)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行DBUtil方法异常");
		}
	}

	/* 资源没有释放！！！！！！ */
	public static ResultSet getResultSet(Connection conn, String sql)
			throws SQLException {
		return conn.prepareStatement(sql).executeQuery();
	}

	/**
	 * execute sql like :insert, update, delete with auto commit
	 * 
	 */
	public static int update(String sql) throws SQLException {
		Connection conn = null;
		int i = -1;
		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			log.info(sql);
			i = conn.prepareStatement(sql).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (null != conn)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("执行DBUtil方法异常");
			}
		}
		return i;
	}

	/**
	 * execute sqls like :insert, update, delete with transaction
	 */
	public static int update(String[] sqls) throws SQLException {
		Connection conn = null;
		int i = -1;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);

			for (int j = 0; j < sqls.length; j++) {
				log.info(sqls[j]);
				i = conn.prepareStatement(sqls[j]).executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (null != conn)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("执行DBUtil方法异常");
			}
		}
		return i;
	}

	public static long getSeq(String sequence_name) {
		return getOraSeq(sequence_name);
	}

	public static String getFormatSeq(String sequenceName) {
		long seq = DBUtil.getOraSeq(sequenceName);
		return DateTrans.getDateFormat() + seq;
	}

	public static long getOraSeq(String sequence_name) {
		long seql = 0l;
		String getseq = "select " + sequence_name + ".nextval  from dual";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(getseq);
			rs = ps.executeQuery();
			while (rs.next()) {
				seql = rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行DBUtil方法异常");
		} finally {
			try {
				if (null != rs)
					rs.close();
				if (null != ps)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return seql;
	}

	public static boolean writeExcel2File(OutputStream outstream, List list) {
		int datarows = list.size();
		int datacols = ((String[]) list.get(0)).length;
		String[][] data = new String[datarows][datacols];
		for (int i = 0; i < datarows; i++) {
			data[i] = (String[]) list.get(i);
		}
		try {
			log.debug("begin writeExcel2FileSimple");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			sheet.setDefaultColumnWidth((short) 11);

			HSSFFont font2 = workbook.createFont();
			font2.setFontHeightInPoints((short) 10);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			HSSFFont font4 = workbook.createFont();
			font4.setFontHeightInPoints((short) 9);
			font4.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

			HSSFCellStyle cellStyle2 = workbook.createCellStyle(); // data head
			HSSFCellStyle cellStyle4 = workbook.createCellStyle(); // data

			cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle2.setWrapText(true);
			cellStyle2.setFont(font2);

			cellStyle4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle4.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle4.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle4.setFont(font4);
			// cellStyle4.setDataFormat(HSSFDataFormat.getFormat("(#,##0.00_);[Red](#,##0.00)"));
			cellStyle4.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("(#,##0.00_);[Red](#,##0.00)"));

			HSSFRow row;
			HSSFCell cell;

			// data head
			row = sheet.createRow((short) 1);
			row.setHeightInPoints(30);
			for (int j = 0; j < datacols; j++) {
				cell = row.createCell((short) j);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellStyle(cellStyle2);
				cell.setCellValue(data[0][j]);
			}
			// data left & data
			for (int i = 1; i < datarows; i++) {
				row = sheet.createRow((short) (i + 1));
				row.setHeightInPoints(15);
				for (int j = 0; j < datacols; j++) {
					cell = row.createCell((short) j);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellStyle(cellStyle4);
					cell.setCellValue(data[i][j]);
				}
			}

			workbook.write(outstream);

			log.debug("end writeExcel2FileSimple");
		} catch (Exception ex) {
			ex.printStackTrace();

			return false;
		}
		return true;
	}

	/**
	 * 文件名：一个文件、分页导出
	 * 这是一个文件、分页导出最重要的一个方法
	 * author linxy 2010-03-18 带表头  所有数据从一个文件导出  每个sheet50000条记录 超过50000就新增sheet
	 * 
	 * @param outstream
	 * @param resultSet
	 * @param headList
	 * @return
	 * @throws SQLException
	 */
	public static boolean writeExcelFile_OnlyOneFile(String filepath,
			ResultSet resultSet, List<String> headList) throws Exception {
		int datacols = resultSet.getMetaData().getColumnCount();
		log.debug("begin writeExcelFile By 林小应 2010-03-18");
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 10);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont font4 = workbook.createFont();
		font4.setFontHeightInPoints((short) 9);
		font4.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		HSSFCellStyle cellStyle2 = workbook.createCellStyle(); // data head
		HSSFCellStyle cellStyle4 = workbook.createCellStyle(); // data

		cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle2.setWrapText(true);
		cellStyle2.setFont(font2);

		cellStyle4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle4.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle4.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle4.setFont(font4);
		cellStyle4.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("(#,##0.00_);[Red](#,##0.00)"));

		HSSFSheet sheet = null;
		HSSFRow row;
		HSSFCell cell;

		// resultSet.first();
		int cur = 0;
		while (resultSet.next()) {
			if (cur % 50000 == 0) {
				sheet = workbook.createSheet();
				sheet.setDefaultColumnWidth((short) 11);
				cur = 0;
			}
			if (cur == 0 && (null != headList && headList.size() >= datacols)) {// 表头
				row = sheet.createRow((short) 0);
				row.setHeightInPoints(30);
				for (int j = 0; j < datacols; j++) {
					cell = row.createCell((short) j);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellStyle(cellStyle2);
					cell.setCellValue(headList.get(j));
				}
				cur++;
			}
			// data left & data
			row = sheet.createRow((short) (cur));
			row.setHeightInPoints(15);
			for (int j = 0; j < datacols; j++) {
				cell = row.createCell((short) j);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellStyle(cellStyle4);
				cell.setCellValue(resultSet.getString(j + 1));
			}
			cur++;
		}
		resultSet.close();
		workbook.write(new FileOutputStream(filepath));
		log.debug("end writeExcelFile By 林小应");
		return true;
	}

	/**
	 * author linxy 2010-03-18 无表头 所有数据从一个文件导出
	 * 文件名：一个文件、分页导出 无表头
	 * @param outstream
	 * @param resultSet
	 * @return
	 * @throws Exception
	 */
	public static boolean writeExcelFile_OnlyOneFile(String filepath,
			ResultSet resultSet) throws Exception {
		return writeExcelFile_OnlyOneFile(filepath, resultSet, null);
	}

	
	/**
	 * 分文件、分表单导出
	 * author linxy 2010-03-18 带表头 默认每个sheet最多存放 5万条记录，每个文件最多存两个sheet ,返回文件名 类似：
	 * 20100319172251_营销成功信息统计(1~1000).XLS 20100319172251_营销成功信息统计(1001~2000).XLS
	 * @param filepath  完整文件路径 如：/ngmkt01/application/crm_market/ngape/market/uploadfiles/20100319121212_营销成功信息统计.xls 后缀名可选
	 * @param resultSet 查询结果集
	 * @param headList  表头 ，可为空 null
	 * @return 返回文件名列表
	 * @throws Exception
	 */
	public static List<String> writeExcelFile(String filepath,
			ResultSet resultSet, List<String> headList) throws Exception {
		return writeExcelFile(filepath,resultSet,headList,50000,2);
	}

	/**
	 * 分文件、分表单导出 无表头
	 * author linxy 2010-03-18 无表头
	 * 
	 * @param filepath
	 * @param resultSet
	 * @return  返回文件名列表
	 * @throws Exception
	 */
	public static List<String> writeExcelFile(String filepath,
			ResultSet resultSet) throws Exception {
		return writeExcelFile(filepath, resultSet, null);
	}

	/**
	 * 这是分文件、分表单导出最重要的一个方法
	 * author linxy 2010-03-18 带表头   海量数据导出时，请合理设置  totallines_per_page 和  sheetcount_per_file  每个文件控制在10万条记录以内，否则可能会导致 溢出错误，内存不够用，系统崩溃
	 * 默认每个sheet最多存放 5万条记录，每个文件最多存两个sheet ,返回文件名 类似：20100319172251_营销成功信息统计(1~1000).XLS 20100319172251_营销成功信息统计(1001~2000).XLS
	 * 
	 * @param filepath
	 *            完整文件路径
	 *            如：/ngmkt01/application/crm_market/ngape/market/uploadfiles/20100319121212_营销成功信息统计.xls
	 *            后缀名可选
	 * @param resultSet
	 *            查询结果集
	 * @param headList
	 *            表头 ，可为空 null
	 * @param totallines_per_page
	 *            每页显示多少条记录 最大限制在每页5w条
	 * @param sheetcount_per_file
	 *            每个文件最多开个sheet
	 * @return  返回文件名列表
	 * @throws Exception
	 */
	public static List<String> writeExcelFile(String filepath,
			ResultSet resultSet, List<String> headList,
			int totallines_per_page, int sheetcount_per_file) throws Exception {
		System.out.println("begin writeExcelFile By 林小应 2010-03-18");
		if (null == filepath || "".equals(filepath)) {
			throw new Exception("文件路径不能为空！");
		}
		if (null == resultSet) {
			throw new Exception("传入的数据集为空！");
		}
		if(totallines_per_page > 50000){
			throw new Exception("最大限制在每页5万条！");
		}
		String path = filepath.substring(0, filepath
				.lastIndexOf(File.separator) + 1);
		String filename = filepath.substring(filepath
				.lastIndexOf(File.separator) + 1);
		String testStr = filepath;
		if (Pattern.matches(".*\\.XLS", testStr.toUpperCase())) {
			filename = filename.substring(0, filename.length() - 4);
		}

		System.out.println("path = " + path);
		System.out.println("filename = " + filename);

		int datacols = resultSet.getMetaData().getColumnCount();

		HSSFWorkbook workbook = null;
		HSSFFont font2 = null;
		HSSFFont font4 = null;
		HSSFCellStyle cellStyle2 = null;
		HSSFCellStyle cellStyle4 = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		List<String> filepathList = new ArrayList<String>();

		int cur = 0;
		int totalrecordcount = 0;
		while (resultSet.next()) {
			if (totalrecordcount % (totallines_per_page * sheetcount_per_file) == 0) {// 换表
				// 换页前将webook写入文件
				if (totalrecordcount > 0) {
					String tempPath = filename
							+ "("
							+ ((totalrecordcount
									/ (totallines_per_page * sheetcount_per_file) - 1)
									* sheetcount_per_file * totallines_per_page + 1)
							+ "~" + totalrecordcount + ").XLS";
					workbook.write(new FileOutputStream(tempPath));
					filepathList.add(tempPath);
				}
				workbook = new HSSFWorkbook();
				font2 = workbook.createFont();
				font2.setFontHeightInPoints((short) 10);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				font4 = workbook.createFont();
				font4.setFontHeightInPoints((short) 9);
				font4.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

				cellStyle2 = workbook.createCellStyle(); // data head
				cellStyle4 = workbook.createCellStyle(); // data

				cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cellStyle2.setWrapText(true);
				cellStyle2.setFont(font2);

				cellStyle4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyle4.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyle4.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyle4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cellStyle4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cellStyle4.setFont(font4);
				cellStyle4.setDataFormat(HSSFDataFormat
						.getBuiltinFormat("(#,##0.00_);[Red](#,##0.00)"));
			}
			if (cur % totallines_per_page == 0) { // 换页
				sheet = workbook.createSheet();
				sheet.setDefaultColumnWidth((short) 11);
				cur = 0;
			}
			if (cur == 0 && (null != headList && headList.size() >= datacols)) {// 表头
				row = sheet.createRow((short) 0);
				row.setHeightInPoints(30);
				for (int j = 0; j < datacols; j++) {
					cell = row.createCell((short) j);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellStyle(cellStyle2);
					cell.setCellValue(headList.get(j));
					// System.out.print(headList.get(j) + " ");
				}
				// System.out.println("");
				cur++;
				// totalrecordcount++;
			}
			// data left & data
			row = sheet.createRow((short) (cur));
			row.setHeightInPoints(15);
			for (int j = 0; j < datacols; j++) {
				cell = row.createCell((short) j);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellStyle(cellStyle4);
				cell.setCellValue(resultSet.getString(j + 1));
				// System.out.print(resultSet.getString(j + 1) + " ");
			}
			// System.out.println("");
			cur++;
			totalrecordcount++;
		}
		resultSet.close();
		String tempPath2 = "";// Just like : F:\file\20100319155158_营销成功信息统计(1~5000).XLS
		if (totalrecordcount <= totallines_per_page) {
			tempPath2 = filename + ".XLS";
		} else {
			tempPath2 = filename
					+ "("
					+ ((totalrecordcount
							/ (totallines_per_page * sheetcount_per_file) - 1)
							* sheetcount_per_file * totallines_per_page + 1)
					+ "~" + totalrecordcount + ").XLS";
		}
		workbook.write(new FileOutputStream(tempPath2));
		filepathList.add(tempPath2);
		System.out.println("end writeExcelFile By 林小应");
		return filepathList;
	}

	
	/**
	 * 预编译
	 */
	@SuppressWarnings("conn = null")
	public static int executePreSql(String sql, List in_params) {
		int ret = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			log.debug("PreSql:" + sql);
			if (in_params != null) {
				for (int i = 0; i < in_params.size(); i++) {
					SqlParam param = (SqlParam) in_params.get(i);
					int type = param.getParam_type();
					String value = param.getParam_value();
					log.debug("type:" + type + ",value:" + value);
					switch (type) {
					case SqlTypes.CHAR:
						ps.setString(i + 1, value);
						break;
					case SqlTypes.VARCHAR:
						ps.setString(i + 1, value);
						break;
					case SqlTypes.INTEGER:
						ps.setInt(i + 1, Integer.parseInt(value));
						break;
					case SqlTypes.DOUBLE:
						ps.setDouble(i + 1, Double.parseDouble(value));
						break;
					case SqlTypes.BIGINT:
						ps.setLong(i + 1, Long.parseLong(value));
						break;
					case SqlTypes.DATE:
						ps.setDate(i + 1, new java.sql.Date(Long.parseLong(value)));
						break;
					//add by luantao@20110309 add timestamp and time
					case SqlTypes.TIMESTAMP:
						ps.setTimestamp(i + 1, new java.sql.Timestamp(Long.parseLong(value)));
						break;
					case SqlTypes.TIME:
						ps.setTime(i + 1, new java.sql.Time(Long.parseLong(value)));
						break;
					default:
						throw new RuntimeException(
								"###: SqlParam参数异常，目前只支持String,number两大类数据类型");
					}
				}
			}
			ret = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != ps) {
					ps.close();
				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		return ret;
	}


	/**
	 * 预编译执行批量SQL
	 */
	public static int executeBatchPreSql(String[] sql,
			List<List<SqlParam>> paraList) {
		int ret = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//关闭自动提交，保持原子性
			conn.setAutoCommit(false);

			for (int j = 0; j < sql.length; j++) {
				List<SqlParam> in_params = paraList.get(j);

				ps = conn.prepareStatement(sql[j]);
				log.debug("PreSql[" + j + "]:" + sql[j]);
				if (in_params != null) {
					for (int i = 0; i < in_params.size(); i++) {
						SqlParam param = (SqlParam) in_params.get(i);
						int type = param.getParam_type();
						String value = param.getParam_value();
						log.debug("type:" + type + ",value:" + value);
						switch (type) {
						case SqlTypes.CHAR:
							ps.setString(i + 1, value);
							break;
						case SqlTypes.VARCHAR:
							ps.setString(i + 1, value);
							break;
						case SqlTypes.INTEGER:
							ps.setInt(i + 1, Integer.parseInt(value));
							break;
						case SqlTypes.DOUBLE:
							ps.setDouble(i + 1, Double.parseDouble(value));
							break;
						case SqlTypes.BIGINT:
							ps.setLong(i + 1, Long.parseLong(value));
							break;
						case SqlTypes.DATE:
							ps.setDate(i + 1, new java.sql.Date(Long
									.parseLong(value)));
							break;
							//add by luantao@20110309 add timestamp and time
						case SqlTypes.TIMESTAMP:
							ps.setTimestamp(i + 1, new java.sql.Timestamp(Long.parseLong(value)));
							break;
						case SqlTypes.TIME:
							ps.setTime(i + 1, new java.sql.Time(Long.parseLong(value)));
							break;
						default:
							throw new RuntimeException(
									"###: SqlParam参数异常，目前只支持String,number两大类数据类型");
						}
					}
				}
				ret = ps.executeUpdate();

				if (ret == -1) {
					conn.rollback();
					return -1;
				}
			}

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return -1;
			}
			
			return -1;
		} finally {
			try {
				if (null != ps) {
					ps.close();
				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		return ret;
	}

	/**
	 * 预编译执行批量SQL
	 */
	public static int executeBatchPreSql(List<String> sql,
			List<List<SqlParam>> paraList) {
		int ret = -1;
		System.out.println(sql.size()+"---"+paraList.size());
		Connection conn = null;
		PreparedStatement ps = null;
		try {

			for (int j = 0; j < sql.size(); j++) {
				List<SqlParam> in_params = paraList.get(j);
				log.debug("PreSql[" + j + "]:" + sql.get(j));
				ps = conn.prepareStatement(sql.get(j));
				if (in_params != null) {
					for (int i = 0; i < in_params.size(); i++) {
						SqlParam param = (SqlParam) in_params.get(i);
						int type = param.getParam_type();
						String value = param.getParam_value();
						log.debug("type:" + type + ",value:" + value);
						switch (type) {
						case SqlTypes.CHAR:
							ps.setString(i + 1, value);
							break;
						case SqlTypes.VARCHAR:
							ps.setString(i + 1, value);
							break;
						case SqlTypes.INTEGER:
							ps.setInt(i + 1, Integer.parseInt(value));
							break;
						case SqlTypes.DOUBLE:
							ps.setDouble(i + 1, Double.parseDouble(value));
							break;
						case SqlTypes.BIGINT:
							ps.setLong(i + 1, Long.parseLong(value));
							break;
						case SqlTypes.DATE:
							ps.setDate(i + 1, new java.sql.Date(Long
									.parseLong(value)));
							break;
							//add by luantao@20110309 add timestamp and time
						case SqlTypes.TIMESTAMP:
							ps.setTimestamp(i + 1, new java.sql.Timestamp(Long.parseLong(value)));
							break;
						case SqlTypes.TIME:
							ps.setTime(i + 1, new java.sql.Time(Long.parseLong(value)));
							break;
						default:
							throw new RuntimeException(
									"###: SqlParam参数异常，目前只支持String,number两大类数据类型");
						}
					}
				}
				ret = ps.executeUpdate();

				if (ret == -1) {
					conn.rollback();
					return -1;
				}
			}

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return -1;
			}
			
			return -1;
		} finally {
			try {
				if (null != ps) {
					ps.close();
				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		return ret;
	}
	public static List<String[]> getStrsListPreSql(String sql, List<SqlParam> in_params) {
		List<String[]> ls = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			log.debug("PreSql:" + sql);
			if (in_params != null) {
				for (int i = 0; i < in_params.size(); i++) {
					SqlParam param =  in_params.get(i);
					int type = param.getParam_type();
					String value = param.getParam_value();
					switch (type) {
					case SqlTypes.CHAR:
						ps.setString(i + 1, value);
						break;
					case SqlTypes.VARCHAR:
						ps.setString(i + 1, value);
						break;
					case SqlTypes.INTEGER:
						ps.setInt(i + 1, Integer.parseInt(value));
						break;
					case SqlTypes.DOUBLE:
						ps.setDouble(i + 1, Double.parseDouble(value));
						break;
					case SqlTypes.BIGINT:
						ps.setLong(i + 1, Long.parseLong(value));
						break;
					default:
						throw new RuntimeException(
								"###: SqlParam参数异常，目前只支持String,number两大类数据类型");

					}
				}
			}
			rs = ps.executeQuery();
			ls = RSUtil.getStrsList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != rs)
					rs.close();
				if (null != ps)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ls;

	}

	/**
	 * 调用返回结果集的存储过程，in_params中参数都为输入参数，输出参数只有一个，就是返回的结果集
	 * 
	 * @param sql
	 *            调用存储过程sql语句，如：{call prc_res_balance(?,?,?,?,?)}
	 * @param in_params
	 *            调用存储过程所需的输入参数
	 * @param out_params
	 *            调用存储过程输出参数类型
	 * @return 将返回结果集转化为字符串数组存放在List中
	 */
	public static Connection  getConnection3(){
		Context initCtx = null;
	      try {
	        // Obtain the initial JNDI context
	    	  Properties p = new Properties();
	    	  p.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
	    	  p.put(javax.naming.Context.PROVIDER_URL,"t3://10.209.76.98:8016");

	        initCtx = new InitialContext(p);
	        // Perform JNDI lookup to obtain resource manager
	        // connection factory
	        String dsJndiName = "market";
	        System.out.println("lookup DataSource, dsJndiName="+dsJndiName);
	        DataSource ds = (javax.sql.DataSource) initCtx.lookup(
	            dsJndiName);
	        return ds.getConnection();
	      }
	      catch (Exception ex) {
	     
	      }
	      finally {
	        try {
	          if (initCtx != null)
	            initCtx.close();
	        }
	        catch (NamingException ex) {
	          // This shouldn't really happen
	        
	        }
	      }
	      return null;
	}
	private static Connection  getConnection4(){
		Context initCtx = null;
	      try {
	        // Obtain the initial JNDI context
	    	  Properties p = new Properties();
	    	  p.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
	    	 // p.put(javax.naming.Context.PROVIDER_URL,"t3://10.208.202.80:7001");
		     p.put(javax.naming.Context.PROVIDER_URL,"t3://10.208.230.242:11920");

	        initCtx = new InitialContext(p);
	        // Perform JNDI lookup to obtain resource manager
	        // connection factory
	        String dsJndiName = "market";
	        System.out.println("lookup DataSource, dsJndiName="+dsJndiName);
	        DataSource ds = (javax.sql.DataSource) initCtx.lookup(
	            dsJndiName);
	        return ds.getConnection();
	      }
	      catch (Exception ex) {
	     
	      }
	      finally {
	        try {
	          if (initCtx != null)
	            initCtx.close();
	        }
	        catch (NamingException ex) {
	          // This shouldn't really happen
	        
	        }
	      }
	      return null;
	}
	private static Connection  getConnection2(){
		String url = "jdbc:oracle:thin:@10.208.229.168:1521:cen1";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			return DriverManager.getConnection(url,"dbmarketadm","dBMA_23idrs#!123we#!qs11");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static Connection  getConnection1(){
		Context initCtx = null;
	      try {
	        // Obtain the initial JNDI context
	    	  Properties p = new Properties();
	    	  p.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
	    	  p.put(javax.naming.Context.PROVIDER_URL,"t3://10.208.230.242:11920");

	        initCtx = new InitialContext(p);
	        // Perform JNDI lookup to obtain resource manager
	        // connection factory
	        String dsJndiName = "market";
	        System.out.println("lookup DataSource, dsJndiName="+dsJndiName);
	        DataSource ds = (javax.sql.DataSource) initCtx.lookup(
	            dsJndiName);
	        return ds.getConnection();
	      }
	      catch (Exception ex) {
	     
	      }
	      finally {
	        try {
	          if (initCtx != null)
	            initCtx.close();
	        }
	        catch (NamingException ex) {
	          // This shouldn't really happen
	        
	        }
	      }
	      return null;
	}
	@SuppressWarnings("unchecked")
	public static List<String[]> getStrArrayListPreProc(String sql,
			List<SqlParam> in_params, List<SqlParam> out_params) {
		List oList = new ArrayList();
		List<String[]> list = new ArrayList();
		Connection conn = null;
		CallableStatement proc = null;
		ResultSet rs = null;
		int i = 1;

		try {
			conn = getConnection1();
			System.out.println("GET CONN"+conn);
			proc = conn.prepareCall(sql);

			// 绑定输入参数
			if (in_params != null) {
				for (; i <= in_params.size(); i++) {
					SqlParam param = (SqlParam) in_params.get(i - 1);
					int type = param.getParam_type();
					String value = param.getParam_value();

					switch (type) {
					case SqlTypes.CHAR:
						proc.setString(i, value);
						break;
					case SqlTypes.VARCHAR:
						proc.setString(i, value);
						break;
					case SqlTypes.INTEGER:
						proc.setInt(i, Integer.parseInt(value));
						break;
					case SqlTypes.DOUBLE:
						proc.setDouble(i, Double.parseDouble(value));
						break;
					case SqlTypes.BIGINT:
						proc.setLong(i, Long.parseLong(value));
						break;
					default:
						throw new RuntimeException(
								"###: SqlParam参数异常，目前只支持String,number两大类数据类型");
					}
				}
			}

			// 绑定输出参数
			if (out_params != null) {
				for (int j = 0; j < out_params.size(); j++) {
					SqlParam param = (SqlParam) out_params.get(j);
					int type = param.getParam_type();

					proc.registerOutParameter(i + j, type);
				}
			}
		    long start_tmp = System.currentTimeMillis();
			// 调用存储过程
			proc.execute();
	        System.out.println("--------------->"+(System.currentTimeMillis()-start_tmp));

			// 将输出结果放入List中
			if (out_params != null) {
				int oIndex = in_params.size();
				for (int k = 1; k <= out_params.size(); k++) {
					SqlParam param = (SqlParam) out_params.get(k - 1);
					int type = param.getParam_type();

					switch (type) {
					case SqlTypes.VARCHAR:
						String value = (String) proc.getString(oIndex + k);
						oList.add(value);
						break;
					case SqlTypes.CURSOR:
						rs = (ResultSet) proc.getObject(oIndex + k);
						list = RSUtil.getStrsList(rs);
						oList.add(list);
						break;
					default:
						throw new RuntimeException(
								"###: SqlParam参数异常，目前只支持String,number两大类数据类型");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != rs)
					rs.close();
				if (null != proc)
					proc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return oList;
	}
}
