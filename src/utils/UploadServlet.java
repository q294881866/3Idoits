package utils;
/* *
 * multipart/form-data的请求头必须包含一个特殊的头信息：Content-Type，
 * 且其值也必须规定为multipart/form-data，同时还需要规定一个内容分割符
 * 用于分割请求体中的多个post的内容，如文件内容和文本内容自然需要分割开来，
 * 不然接收方就无法正常解析和还原这个文件了。具体的头信息如下：
 * [html] view plain copy Content-Type: multipart/form-data; boundary=${bound} 
 *  其中${bound} 是一个占位符，代表我们规定的分割符，可以自己任意规定，
 *  但为了避免和正常文本重复了，尽量要使用复杂一点的内容。如：--------------------56423498738365
 *  
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;

public class UploadServlet extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		
		try {
			//首先，判断是否multipart编码类型
						boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart){
				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iter = upload.getItemIterator(request);
				while(iter.hasNext()){
					FileItemStream item = iter.next();
					//得到表单域的名称
				    String name = item.getFieldName();
				    //得到表单域的值（这是一个输入流）
				    InputStream stream = item.openStream();
				    
				    //如果是普通表单域
				    if(item.isFormField()){
				    	String value = Streams.asString(stream,request.getCharacterEncoding());
				    	System.out.println(name+"="+value);
				    }else{ //如果是文件
				    	if(stream.available() != 0){//如果文件域没有选择文件，则忽略处理
					    	String filename = item.getName(); //得到上传的文件名称
					    	if(filename != null){
					    		//因为在IE下面，上传的文件还包含有此文件在客户端机器的路径
					    		//所以，要把这个路径去掉，只取文件名
					    		filename = FilenameUtils.getName(filename);
					    	}
					    	System.out.println(name+"="+filename);
					    	//将上传文件的输入流输出到磁盘的文件上
					    	Streams.copy(stream, new FileOutputStream("d:/temp/upload/"+filename), true);
				    	}
				    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

}
