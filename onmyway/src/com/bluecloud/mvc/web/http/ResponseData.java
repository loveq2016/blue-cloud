/**
 * 
 */
package com.bluecloud.mvc.web.http;

/**
 * @author leo
 * 
 */
public final class ResponseData {

	private String dispatch;

	protected ResponseData() {
	}

	public String getDispatch() {
		return this.dispatch.trim();
	}

	protected void parse(BCWebResponse bcWebResponse) {
		this.setDispatch(bcWebResponse.getView());
	}

	/**
	 * 
	 * @param dispatch
	 */
	private void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

}
