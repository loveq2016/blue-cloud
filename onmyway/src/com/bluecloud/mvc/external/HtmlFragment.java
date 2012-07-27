/**
 * 
 */
package com.bluecloud.mvc.external;

import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 * 
 */
public abstract class HtmlFragment {

	private HtmlFragmentResponse response;

	public HtmlFragment() {
	}

	/**
	 * 注册方法，用于每个页面或者片段的提交请求
	 * 
	 * @return 事件注册器
	 */
	protected abstract FragmentEventRegister regEvent();

	/**
	 * 
	 * @param request
	 * @return
	 */
	public abstract HtmlFragmentResponse init(HtmlFragmentRequest request);

	/**
	 * 
	 * @param response
	 */
	public final void setResponse(HtmlFragmentResponse response) {
		this.response=response;
	}

	/**
	 * 
	 * @return
	 */
	public final  HtmlFragmentResponse getResponse() {
		return this.response;
	}

}
