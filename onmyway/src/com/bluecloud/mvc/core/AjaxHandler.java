/**
 * 
 */
package com.bluecloud.mvc.core;

import com.bluecloud.mvc.external.FragmentEvent;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.web.http.HtmlFragmentDispatcher;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;
import com.bluecloud.mvc.web.http.ResponseData;

/**
 * @author Leo
 * 
 */
public class AjaxHandler extends HtmlFragmentHandlerImpl {

	/**
	 * @param response
	 * @param request
	 * 
	 */
	public AjaxHandler(HtmlFragmentRequest request,
			HtmlFragmentResponse response) {
		super(request, response);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.api.HttpFragmentHandler#service(com.bluecloud.mvc.external.HtmlFragment)
	 */
	@Override
	public ResponseData service(HtmlFragment fragment) throws Exception {
		if (fragment == null) {
			forward();
		}
		String eventName = request.getSubmit();
		FragmentEvent event = fragment.getEvent(eventName);
		fragment.setResponse(response);
		if (null == event) {
			fragment.init(request);
		} else {
			event.execute(request, fragment);
		}
		ResponseData respData = response.getData();
		return respData;
	}

	/**
	 * 
	 */
	private void forward() {
		
	}

}
