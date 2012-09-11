/**
 * 
 */
package com.bluecloud.mvc.external;

import com.bluecloud.mvc.exception.HtmlFragmentException;
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
	 * 注册事件，用于每个页面或者片段的提交请求
	 * 
	 * @return 事件注册器
	 */
	protected abstract FragmentEventRegister regEvent();

	/**
	 * 注册VO，用户每个页面或者片段提交数据的封装
	 * @return
	 */
	protected abstract FragmentBeanRegister regVO();
	/**
	 * 
	 * @param request
	 * @return
	 */
	public abstract HtmlFragmentResponse init(HtmlFragmentRequest request)throws HtmlFragmentException;

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

	/**
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 
	 * @param eventName
	 * @return
	 */
	public FragmentEvent getEvent(String eventName) {
		FragmentEventRegister reg=regEvent();
		FragmentEvent event = null;
		if(null!=reg){
			event=reg.find(eventName);
		}
		return event;
	}
}
