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
public class FramgmentForward implements HtmlFragmentDispatcher {

	private String path;

	/**
	 * @param res 
	 * @param path 
	 * 
	 */
	public FramgmentForward(HttpServletResponse res, String path) {
		this.path=path;
	}

	/* 
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentDispatcher#exce()
	 */
	@Override
	public void exce() {

	}

}
