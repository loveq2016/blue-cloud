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

	private HttpServletResponse resp;

	public BCWebResponse(HttpServletResponse resp) {
		this.resp = resp;
	}

}
