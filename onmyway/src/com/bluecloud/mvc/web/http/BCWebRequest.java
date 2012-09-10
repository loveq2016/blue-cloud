/**
 * 
 */
package com.bluecloud.mvc.web.http;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

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
		this.submitName=conf.getInitParameter("name");
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

	@Override
	public String getSubmit() {
		String submit=req.getParameter(this.submitName);
		return null;
	}

}
