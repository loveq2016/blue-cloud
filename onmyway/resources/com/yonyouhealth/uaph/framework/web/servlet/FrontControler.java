package com.yonyouhealth.uaph.framework.web.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.logging.Logger;

import com.yonyouhealth.uaph.framework.comm.conf.ConfManager;
import com.yonyouhealth.uaph.framework.comm.pool.ThreadLocalManager;
import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.controller.DoController;
import com.yonyouhealth.uaph.framework.web.fileupload.UploadConstants;
import com.yonyouhealth.uaph.framework.web.fileupload.UploadTools;
import com.yonyouhealth.uaph.framework.web.mvc.UhwebDataSet;

/**
 * 整个应用的前端控制器 todo:所有的日志都替换为System.out.println了，平台部需要以后将其改为UAP的日志
 */
public class FrontControler extends HttpServlet {
	private static final long serialVersionUID = 2308838939791005248L;

	// protected static final LogWritter log =
	// LogFactory.getLogger(Uhweb.class);

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.init("uhweb");
		try {
			// ConfManager.load();

			ThreadLocalManager.add("httpReq", request);
			ThreadLocalManager.add("httpRes", response);
			String str = request.getRequestURI();
			doDelegate(str, request, response);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			errorProcess(e);
		} finally {
			ThreadLocalManager.clear();
			Logger.reset();
		}
	}

	/**
	 * URI分派
	 * @param requestUri
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void doDelegate(String requestUri, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String encode = request.getCharacterEncoding();
		if (encode == null)
			encode = "utf-8";

		UhwebDataSet dataSet = null;
		try {
			Map localMap = request.getParameterMap();
			String postData = null;
			
			//ajax
			if (requestUri.endsWith("/ajax.uhweb")) { 
				Logger.info("Request URI:" + requestUri + ",本次请求为ajax提交。");
				dataSet = new UhwebDataSet(request);
				
				ThreadLocalManager.add("reqDataSet", dataSet);
				doCtrl(dataSet);
				jsonProcess();
			} 
			//from and upload
			else if (requestUri.endsWith("/uhweb") || requestUri.endsWith("/upload.uhweb")
					|| requestUri.endsWith("/form.uhweb")) { 
				if (requestUri.indexOf("upload") != -1) {
					Logger.info("Request URI:" + requestUri + ",本次请求为文件上传请求。");
					
					UploadTools uploadTools = new UploadTools();
					handleUploadParam(request, uploadTools);
					uploadTools.upload(request);
					
					postData = ((UploadTools) uploadTools).getParam("postData");
					dataSet = new UhwebDataSet(postData);
					dataSet.getReqDataObject().getViewData().put("uploadOject", uploadTools);
				} 
				else if (requestUri.indexOf("form") != -1) {
					Logger.info("Request URI:" + requestUri + ",本次提交为form提交......");
//					postData = request.getParameter("postData");
					//by liumin, 2012.8.21 解决以form方式提交，汉字乱码问题
					postData = new String(request.getParameter("postData").getBytes("iso-8859-1"), "utf-8");
					dataSet = new UhwebDataSet(postData);
				} 
				else {
					Logger.info("Request URI:" + requestUri + ",本次请求为form提交。");
					if ((localMap != null) && (((localMap.get("tid") != null) || (localMap.get("ctrl") != null)))) {
						postData = buildJson(localMap);
						dataSet = new UhwebDataSet(postData);
					} else {
						throw new RuntimeException("未获取到有效的Tid或者Ctrl");
					}
				}
				
				ThreadLocalManager.add("reqDataSet", dataSet);
				ThreadLocalManager.add("servletConfig", getServletConfig());
				doCtrl(dataSet);
				
				//Ctrl类的处理结果
				String pageName = (String) ContextAPI.getResDataSet().getResDataObject().getJsonStringMap()
						.get("page");
				ContextAPI.getResDataSet().getResDataObject().getJsonStringMap().remove("page");
				if ((pageName != null) && (postData != null)) {
					if (pageName.indexOf(".jsp") != -1) {
						response.setContentType("text/html;charset=" + encode);
						setJSTLData(request);
						request.getRequestDispatcher(pageName).include(request, response);
						
						request.setAttribute("UhwebPageData",
								ContextAPI.getResDataSet().getJson().replaceAll("'", "&apos;"));
						request.getRequestDispatcher("uhweb/html/UhwebPageData.jsp").include(request, response);
					} 
					else {
						response.setContentType("text/html;charset=" + encode);
						String s = "<div id=\"UhwebPageData\"  style='display:none;' data='"
								+ ContextAPI.getResDataSet().getJson().replaceAll("'", "&apos;") + "'></div>";
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), encode));
						request.getRequestDispatcher(pageName).include(request, response);
						((PrintWriter) pw).println(s);
						((PrintWriter) pw).flush();
						((PrintWriter) pw).close();
					}
				}
				else {
					throw new RuntimeException("未获取到有效的跳转地址！");
				}
			}
			//download
			else if (requestUri.endsWith("/download.uhweb")) {
				Logger.info("Request URI:" + requestUri + ",本次请求为文件下载请求。");
				if ((localMap != null) && (((localMap.get("tid") != null) || (localMap.get("ctrl") != null)))) {
					postData = buildJson(localMap);
					dataSet = new UhwebDataSet(postData);
				} else {
					dataSet = new UhwebDataSet(request);
				}
				ThreadLocalManager.add("reqDataSet", dataSet);
				doCtrl(dataSet);
			}
		} 
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
			
			//得到异常信息和异常类
			String expClassName = "";
			String expMsg = "";
			for (Object expObj = e;; expObj = ((Throwable) expObj).getCause()) {
				if (((Throwable) expObj).getCause() == null) {
					expMsg = ((Throwable) expObj).getMessage();
					expClassName = expObj.getClass().getName();
					break;
				}
				
				if (((Throwable) expObj).getCause().getCause() != null)
					continue;
				
				expMsg = ((Throwable) expObj).getCause().getMessage();
				expClassName = ((Throwable) expObj).getCause().getClass().getName();
				break;
			}
			
			//处理出错状况
			if ((requestUri.endsWith("/uhweb")) || (requestUri.indexOf("form") != -1)) {
				request.setAttribute("exceptionName", expClassName);
				request.setAttribute("exceptionMes", expMsg);
				
				//导向错误页
				Object errPage = ConfManager.getValueByKey("errorPage"); //从配置中得到错误页
				if ((errPage != null) && (!"".equals(errPage.toString())))
					request.getRequestDispatcher(errPage.toString()).forward(request, response);
				else
					request.getRequestDispatcher("uhweb/html/error/error.jsp").forward(request, response);
				
			} 
			else if ((requestUri.endsWith("/download.uhweb")) || (requestUri.endsWith("/upload.uhweb"))) {
				StringBuffer sb = new StringBuffer();
				((StringBuffer) sb).append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
				((StringBuffer) sb).append("<html><header>");
				((StringBuffer) sb)
						.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
				String s = "<script language=\"javascript\" type=\"text/javascript\" >alert('错误报告！\\n名称："
						+ (String) expClassName + "\\n信息：" + expMsg + "');</script>";
				((StringBuffer) sb).append(s);
				((StringBuffer) sb).append("</heander></html>");
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), encode));
				pw.println(((StringBuffer) sb).toString());
				pw.flush();
				pw.close();
			} else if (requestUri.endsWith("ajax.uhweb")) {
				errorProcess(e);
			}
		}
	}

	/**
	 * 处理Ctrl流程(执行指定的Ctrl类的指定的方法)
	 * @param dataSet
	 * @throws Exception
	 */
	private void doCtrl(UhwebDataSet dataSet) throws Exception {
		if (!"".equals(dataSet.getCtrl()) && dataSet.getCtrl() != null) {
			DoController.execute(dataSet.getCtrl(), dataSet.getCtrl_method());
		}
		else {
			//内容被zzj盖住了，即当没有Ctrl时走的地方;可能是如果没有Ctrl，继续向下走，去找BLH了（business logic handler）
//			ContextAPI.goProcess();
		}
	}
	
	/**
	 * 设置JSTL数据，就是把这部分数据都送入request的属性中，因为JSTL首先从这里取值
	 * @param request
	 */
	private void setJSTLData(HttpServletRequest request) {
		if (ContextAPI.getResDataSet().getJstlDataMap() == null)
			return;
		
		Iterator iter = ContextAPI.getResDataSet().getJstlDataMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			request.setAttribute((String) entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 处理上传文件的参数
	 * @param request
	 * @param uploadTools
	 */
	private void handleUploadParam(HttpServletRequest request, UploadTools uploadTools) {
		int i = UploadConstants.FILE_MAX_SIZE;
		int j = 4194304;
		if ((getInitParameter(UploadConstants.KEY_UPLOAD_FILE_MAX_SIZE) != null) && (getInitParameter(UploadConstants.KEY_UPLOAD_FILE_MAX_SIZE).trim().length() > 0))
			try {
				i = Integer.parseInt(getInitParameter(UploadConstants.KEY_UPLOAD_FILE_MAX_SIZE));
				if (i < 0)
					i = -1;
				else
					i *= 1024;
			} catch (NumberFormatException e) {
				Logger.error("err:全局上传文件最大尺寸值[" + getInitParameter(UploadConstants.KEY_UPLOAD_FILE_MAX_SIZE) + "]格式错误!", e);
				throw new RuntimeException("全局上传文件最大尺寸值[" + getInitParameter(UploadConstants.KEY_UPLOAD_FILE_MAX_SIZE) + "]格式错误!");
			}
		if ((getInitParameter(UploadConstants.KEY_UPLOAD_MAX_SIZE) != null)
				&& (getInitParameter(UploadConstants.KEY_UPLOAD_MAX_SIZE).trim().length() > 0))
			try {
				j = Integer.parseInt(getInitParameter(UploadConstants.KEY_UPLOAD_MAX_SIZE));
				if (j < 0)
					j = -1;
				else
					j *= 1024;
			} catch (NumberFormatException e) {
				Logger.error("err:全局上传文件总大小大值[" + getInitParameter(UploadConstants.KEY_UPLOAD_MAX_SIZE) + "]格式错误!", e);
				throw new RuntimeException("全局上传文件总大小大值[" + getInitParameter(UploadConstants.KEY_UPLOAD_MAX_SIZE) + "]格式错误!");
			}
		String str1 = request.getParameter(UploadConstants.KEY_UPLOAD_FILE_MAX_SIZE);
		String str2 = request.getParameter(UploadConstants.KEY_UPLOAD_MAX_SIZE);
		if ((str1 != null) && (str1.trim().length() > 0))
			try {
				i = Integer.parseInt(str1);
				if (i < 0)
					i = -1;
				else
					i *= 1024;
			} catch (NumberFormatException e) {
				Logger.error("err:上传文件最大尺寸值[" + str1 + "]格式错误!", e);
				throw new RuntimeException("上传文件最大尺寸值[" + str1 + "]格式错误!");
			}
		if ((str2 != null) && (str2.trim().length() > 0))
			try {
				j = Integer.parseInt(str2);
				if (j < 0)
					j = -1;
				else
					j *= 1024;
			} catch (NumberFormatException e) {
				Logger.error("err:上传文件总大小大值[" + str2 + "]格式错误!", e);
				throw new RuntimeException("上传文件总大小大值[" + str2 + "]格式错误!");
			}
		uploadTools.setField(UploadConstants.TEMP_FILE_PATH, UploadConstants.BUFFER_SIZE, i, j,
				UploadConstants.ENCODING);
	}

	/**
	 * 将URL参数转换成JSON对象格式
	 * @param paramMap
	 * @return 如：{"ctrl":"SaveCTRL_init"}
	 */
	private String buildJson(Map<String, String[]> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Iterator iter = paramMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			sb.append("\"");
			sb.append((String) entry.getKey()).append("\"");
			sb.append(":").append("\"").append(((String[]) entry.getValue())[0]).append("\"");
			if (iter.hasNext())
				sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 错误处理
	 * @param e
	 */
	private void errorProcess(Throwable e) {
		String errClassName = "";
		String errMsg = "";
		StringBuilder sbMsg = new StringBuilder();
//		if (!"null".equalsIgnoreCase(e.getMessage())) {
//			sbMsg.append(e.getMessage());
//		}
		while (true) {
			
//			if (e.getCause().getCause() == null) {
//				errMsg = e.getCause().getMessage();
//				errClassName = e.getCause().getClass().getName();
//				break;
//			}
			if (sbMsg.length() > 0) sbMsg.append("<br>");
			sbMsg.append(e);
			for (StackTraceElement ste: e.getStackTrace()) {
				sbMsg.append("<br>&nbsp;&nbsp;at ").append(ste.toString());
			}
			if (e.getCause() == null) {
				errClassName = e.getClass().getName();
				break;
			}
			e = e.getCause();
		}
		
		ContextAPI.getResDataSet().addRootAttr("exception", Boolean.valueOf(true));
		ContextAPI.getResDataSet().addRootAttr("exceptionMes", sbMsg.toString());
		ContextAPI.getResDataSet().addRootAttr("exceptionName", errClassName);
		
		jsonProcess();
	}

	/**
	 * json处理(ajax调用后的json处理)
	 */
	private void jsonProcess() {
		String jsonStr = ContextAPI.getResDataSet().getJson();
		Logger.info("返回数据 = " + jsonStr);
		
		//是否是用户自己处理响应数据
		Object isCustomResp = ThreadLocalManager.get("customRes");
		boolean bool = false;
		if (isCustomResp != null)
			bool = ((Boolean) isCustomResp).booleanValue();
		if (bool)
			return;
		
		//将json串加入头响应头后送入输出流
		try {
			String contentType = ContextAPI.getReq().getContentType();
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1))
				ContextAPI.getRes().setContentType("text/html;charset=" + ContextAPI.getReq().getCharacterEncoding());
			else
				ContextAPI.getRes().setContentType("text/json;charset=" + ContextAPI.getReq().getCharacterEncoding());
			
			ContextAPI.getRes().getWriter().write(jsonStr);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		} finally {
			try {
				ContextAPI.getRes().getWriter().close();
			} catch (IOException ex) {
				Logger.error(ex.getMessage(), ex);
			}
		}
	}

	
}