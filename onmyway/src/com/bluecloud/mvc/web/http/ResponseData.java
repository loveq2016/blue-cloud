/**
 * 
 */
package com.bluecloud.mvc.web.http;

/**
 * @author leo
 * 
 */
public final class ResponseData {

	private HtmlFragmentDispatcher dispatch;

	protected ResponseData() {
	}

	public HtmlFragmentDispatcher getDispatch() {
		return this.dispatch;
	}

	protected void parse(BCWebResponse bcWebResponse) {
		this.setDispatch(bcWebResponse.getDispatch());
	}

	/**
	 * 
	 * @param htmlFragmentDispatcher
	 */
	private void setDispatch(HtmlFragmentDispatcher htmlFragmentDispatcher) {
		this.dispatch = htmlFragmentDispatcher;
	}

}
