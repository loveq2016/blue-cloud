/**
 * 
 */
package com.bluecloud.mvc.web.http;

import javax.servlet.http.HttpServletRequest;

/**
 * @author leo
 * 
 */
public final class BCWebRequest implements HtmlFragmentRequest {

	private HttpServletRequest req;

	private RequestData reqData;

	public BCWebRequest(HttpServletRequest req) {
		this.req = req;
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
		return null;
	}

}
