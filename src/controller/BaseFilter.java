package controller;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// 配置跨域请求
		res.setHeader("Access-Control-Allow-Origin", "*");
		// 配置跨域请求
		// response.setHeader("Access-Control-Allow-Origin",
		// "http://localhost:3000");
		res.addHeader("Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS");
		res.setHeader(
				"Access-Control-Allow-Headers",
				"Origin, Accept,COOKIES,No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
		res.addHeader("Access-Control-Allow-Credentials", "true");

		// 如果IE浏览器则设置头信息如下
		if ("IE".equals(request.getParameter("type"))) {
			res.addHeader("XDomainRequestAllowed", "1");
		}
		System.out.println("i am in");
		// 1.处理接收消息
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");
		// 2.得到请求完整路径，不论GET或POST都可以通过getRequestURL+getParameterMap()来
		StringBuffer url = req.getRequestURL();
		String[] urltemp = url.toString().split("/");
		int last = urltemp.length - 1;
		// 3.截取访问路径 ***/weixin/user_list,拼接为weixin.user,
		String module = urltemp[last - 1];// weixin
		String process = urltemp[last];// user_list
		String[] strings = process.split("_");
		String actionName = strings[0];
		String methodName = strings[1];
		/** 配置文件的命名规则，模块名.Action名 weixin.user */
		String beanName = module + "." + actionName;

		// 4.处理请求并返回结果
		ActionSupport actionSupport = (ActionSupport) LoadProperties
				.getBean(beanName);
		if (null != actionSupport) {
			// 传入请求和响应
			System.out.println("req==" + req + ";res==" + res);
			actionSupport.setRequestAndResponse(req, res);
			// 调用相应的方法处理
			Method method;
			try {
				method = actionSupport.getClass().getMethod(methodName, null);
				System.out.println("methodName==" + methodName);
				method.invoke(actionSupport, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
