/**
 * 
 */
package com.bluecloud.mvc.web.http;

import javax.servlet.http.HttpServletRequest;

import com.bluecloud.mvc.external.bean.FragmentBean;

/**
 * @author leo
 * 
 */
public interface HtmlFragmentRequest {

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return
	 */
	String getSubmit();

	/**
	 * 
	 * @return
	 */
	HttpServletRequest getHttpServletRequest();

	/**
	 * 
	 * @param beanName
	 * @return
	 */
	FragmentBean getForm(String beanName);

	/**
	 * 
	 * @return
	 */
	RequestData getReqData();

}
