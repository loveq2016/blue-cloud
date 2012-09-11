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
	void send(String path);

	/**
	 * 
	 * @param path
	 */
	void forward(String path);

}
