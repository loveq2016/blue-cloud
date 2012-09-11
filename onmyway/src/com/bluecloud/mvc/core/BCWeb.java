/**
 * 
 */
package com.bluecloud.mvc.core;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluecloud.mvc.api.HtmlFragmentDepository;
import com.bluecloud.mvc.api.HttpFragmentHandler;
import com.bluecloud.mvc.web.http.BCWebRequest;
import com.bluecloud.mvc.web.http.BCWebResponse;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 * 
 */
public final class BCWeb {

	private static HtmlFragmentDepository fragDepository;
	private static boolean isStart = false;
	private static Exception info;
	private static ServletConfig conf;

	public static HttpFragmentHandler createHtmlFragmentHandler(
			HttpServletRequest req, HttpServletResponse resp) {
		HttpFragmentHandler handler=null;
		HtmlFragmentRequest request = new BCWebRequest(req,conf);
		HtmlFragmentResponse response = new BCWebResponse(resp);
		String reqType=req.getParameter("reqType");
		if(reqType!=null){
			reqType=reqType.trim();
			if(reqType.equals("ajax")){
				handler=new AjaxHandler(request, response);
			}
		}else{
			
		}
		return handler;
	}

	public static HtmlFragmentDepository getFragmentDepository() {
		return fragDepository;
	}

	/**
	 * @param config 
	 * 
	 */
	public static void start(ServletConfig config) {
		if (isStart()) {
			return;
		}
		fragDepository = new HtmlFragmentDepositoryImpl();
		try {
			if(config==null){
				throw new Exception("web.xml中没有配置BCWebServlet的初始化参数name");
			}
			String name=config.getInitParameter("name");
			if(name==null||name.trim().equals("")){
				throw new Exception("web.xml中没有配置BCWebServlet的初始化参数name");
			}
			conf=config;
			fragDepository.load(fragDepository.getClass().getClassLoader());
			isStart = true;
		} catch (Exception e) {
			e.printStackTrace();
			info=e;
		}
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isStart() {
		return isStart;
	}

	/**
	 * 
	 * @return
	 */
	public static Exception getStartInfo() {
		return info;
	}

}
