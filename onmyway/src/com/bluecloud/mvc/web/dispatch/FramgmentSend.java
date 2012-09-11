/**
 * 
 */
package com.bluecloud.mvc.web.dispatch;

import javax.servlet.http.HttpServletResponse;

import com.bluecloud.mvc.web.http.HtmlFragmentDispatcher;

/**
 * @author Leo
 *
 */
public class FramgmentSend implements HtmlFragmentDispatcher {

	private String path;

	/**
	 * @param res 
	 * @param path 
	 * 
	 */
	public FramgmentSend(HttpServletResponse res, String path) {
		this.path=path;
	}

	/* (non-Javadoc)
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentDispatcher#exce()
	 */
	@Override
	public void exce() {

	}

}
