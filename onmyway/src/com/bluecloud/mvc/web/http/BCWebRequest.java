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

	public BCWebRequest(HttpServletRequest req) {
		this.req = req;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentRequest#getName()
	 */
	@Override
	public String getName() {
		String sp = req.getServletPath();
		return sp;
	}

	@Override
	public String getSubmit() {
		return null;
	}

}
