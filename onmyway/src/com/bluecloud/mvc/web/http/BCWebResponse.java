/**
 * 
 */
package com.bluecloud.mvc.web.http;

import javax.servlet.http.HttpServletResponse;

import com.bluecloud.mvc.web.dispatch.FramgmentForward;
import com.bluecloud.mvc.web.dispatch.FramgmentSend;

/**
 * @author leo
 * 
 */
public final class BCWebResponse implements HtmlFragmentResponse {

	private HttpServletResponse res;
	private ResponseData responseData;
	private HtmlFragmentDispatcher dispatcher;

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
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentResponse#forward(java.lang.String)
	 */
	@Override
	public void forward(String path) {
		dispatcher=new FramgmentForward(res,path);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.web.http.HtmlFragmentResponse#send(java.lang.String)
	 */
	@Override
	public void send(String path) {
		dispatcher=new FramgmentSend(res,path);
	}

	/**
	 * 
	 * @return
	 */
	public HtmlFragmentDispatcher getDispatch() {
		return dispatcher;
	}
}
