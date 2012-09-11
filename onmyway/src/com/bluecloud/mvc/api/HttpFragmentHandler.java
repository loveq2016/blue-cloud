/**
 * 
 */
package com.bluecloud.mvc.api;

import com.bluecloud.mvc.external.FragmentEventRegister;
import com.bluecloud.mvc.external.HtmlFragment;

/**
 * @author leo
 *
 */
public interface HttpFragmentHandler {

	/**
	 * 
	 * @param fragment
	 * @param eventRegister
	 * @throws Exception 
	 */
	void service(HtmlFragment fragment, FragmentEventRegister eventRegister) throws Exception;

	/**
	 * 获得请求的fragment标识，即http://ip:port/context/servletpath=fragment标识
	 * @return
	 */
	String getRequestFragment();

}
