/**
 * 
 */
package com.bluecloud.mvc.web.http;

import javax.servlet.http.HttpServletResponse;

/**
 * @author leo
 * 
 */
public final class BCWebResponse implements HtmlFragmentResponse {

	private HttpServletResponse res;
	private ResponseData responseData;
	private String dispatch;

	public BCWebResponse(HttpServletResponse res) {
		this.res = res;
		responseData = new ResponseData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentResponse#getData()
	 */
	@Override
	public ResponseData getData() {
		responseData.parse(this);
		return responseData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bluecloud.mvc.web.http.HtmlFragmentResponse#addView(java.lang.String)
	 */
	@Override
	public void addView(String path) {
		this.dispatch = path;
	}

	/**
	 * 
	 * @return
	 */
	public String getView() {
		return this.dispatch;
	}

}
