/**
 * 
 */
package com.bluecloud.mvc.core;

import com.bluecloud.mvc.api.HttpFragmentHandler;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 * 
 */
public abstract class HtmlFragmentHandlerImpl implements HttpFragmentHandler {

	protected HtmlFragmentRequest request;
	protected HtmlFragmentResponse response;

	public HtmlFragmentHandlerImpl(HtmlFragmentRequest request,
			HtmlFragmentResponse response) {
		this.request = request;
		this.response = response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bluecloud.mvc.api.HttpFragmentHandler#getRequestFragment()
	 */
	@Override
	public final String getRequestFragment() {
		return request.getName();
	}

}
