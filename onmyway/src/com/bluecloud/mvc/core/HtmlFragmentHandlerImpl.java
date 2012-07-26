/**
 * 
 */
package com.bluecloud.mvc.core;

import com.bluecloud.mvc.api.HttpFragmentHandler;
import com.bluecloud.mvc.external.FragmentEvent;
import com.bluecloud.mvc.external.FragmentEventRegister;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 * 
 */
final class HtmlFragmentHandlerImpl implements HttpFragmentHandler {

	private HtmlFragmentRequest request;
	private HtmlFragmentResponse response;

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
	public String getRequestFragment() {
		return request.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bluecloud.mvc.api.HttpFragmentHandler#service(com.bluecloud.mvc.external
	 * .HtmlFragment, com.bluecloud.mvc.external.FragmentEventRegister)
	 */
	@Override
	public void service(HtmlFragment fragment,
			FragmentEventRegister eventRegister) {
		String eventName = request.getSubmit();
		FragmentEvent event=eventRegister.find(eventName);
		if (null == eventName || eventName.trim().equals("")||null==event) {
			fragment.init();
		} else {
			event.execute(request, fragment);
		}
	}

}
