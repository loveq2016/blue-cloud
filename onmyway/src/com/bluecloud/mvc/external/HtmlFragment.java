/**
 * 
 */
package com.bluecloud.mvc.external;

import com.bluecloud.mvc.web.http.HtmlFragmentResponse;


/**
 * @author leo
 *
 */
public abstract class HtmlFragment {

	/**
	 * 注册方法，用于每个页面或者片段的提交请求
	 * @return 事件注册器
	 */
	protected abstract FragmentEventRegister regEvent();

	/**
	 * 
	 * @return
	 */
	public HtmlFragmentResponse getResponse() {
		return new HtmlFragmentResponse(){
		};
	}

	public abstract HtmlFragmentResponse init();

}
