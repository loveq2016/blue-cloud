/**
 * 
 */
package com.bluecloud.mvc.web.http;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import com.bluecloud.mvc.external.bean.FragmentBean;

/**
 * @author leo
 * 
 */
public final class BCWebRequest implements HtmlFragmentRequest {

	private HttpServletRequest req;

	private RequestData reqData;

	private String submitName;

	public BCWebRequest(HttpServletRequest req, ServletConfig conf) {
		this.req = req;
		String name=conf.getInitParameter("name");
		this.submitName=req.getParameter(name);
		reqData = new RequestData();
		reqData.parse(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentRequest#getName()
	 */
	@Override
	public String getName() {
		String sp = req.getServletPath();
		return sp.substring(0, sp.indexOf("."));
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentRequest#getSubmit()
	 */
	@Override
	public String getSubmit() {
		return submitName;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentRequest#getHttpServletRequest()
	 */
	@Override
	public HttpServletRequest getHttpServletRequest() {
		return this.req;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentRequest#getForm(java.lang.String)
	 */
	@Override
	public FragmentBean getForm(String beanName) {
		Map<String,String> formData=reqData.getFormData(beanName);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentRequest#getReqData()
	 */
	@Override
	public RequestData getReqData() {
		return reqData;
	}
}
