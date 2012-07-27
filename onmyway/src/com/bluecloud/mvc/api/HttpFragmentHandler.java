/**
 * 
 */
package com.bluecloud.mvc.api;

import com.bluecloud.mvc.external.FragmentEventRegister;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.web.http.ResponseData;

/**
 * @author leo
 *
 */
public interface HttpFragmentHandler {

	/**
	 * 
	 * @param fragment
	 * @param eventRegister
	 * @return 
	 */
	ResponseData service(HtmlFragment fragment, FragmentEventRegister eventRegister);

	/**
	 * 获得请求的fragment标识，即http://ip:port/context/servletpath=fragment标识
	 * @return
	 */
	String getRequestFragment();

}
