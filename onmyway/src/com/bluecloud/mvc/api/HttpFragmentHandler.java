/**
 * 
 */
package com.bluecloud.mvc.api;

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
	 * @return 
	 * @throws Exception 
	 */
	ResponseData service(HtmlFragment fragment) throws Exception;

	/**
	 * 获得请求的fragment标识，即http://ip:port/context/servletpath=fragment标识
	 * @return
	 */
	String getRequestFragment();

}
