/**
 * 
 */
package com.bluecloud.mvc.api;

import java.util.Set;

import com.bluecloud.mvc.external.FragmentEvent;
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
	 */
	void service(HtmlFragment fragment, FragmentEventRegister eventRegister);

	/**
	 * 获得请求的fragment标识，即http://ip:port/context/servletpath=fragment标识
	 * @return
	 */
	String getRequestFragment();

}
