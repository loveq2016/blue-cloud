/**
 * 
 */
package com.bluecloud.mvc.web.http;

/**
 * @author leo
 * 
 */
public interface HtmlFragmentResponse {

	/**
	 * 
	 * @return
	 */
	ResponseData getData();

	/**
	 * 
	 * @param path
	 */
	void addView(String path);

}
