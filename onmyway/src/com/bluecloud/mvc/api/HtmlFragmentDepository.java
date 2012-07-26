/**
 * 
 */
package com.bluecloud.mvc.api;

import com.bluecloud.mvc.external.HtmlFragmentRegister;

/**
 * @author leo
 *
 */
public interface HtmlFragmentDepository {

	/**
	 * 
	 * @param fragmentName
	 * @return
	 */
	HtmlFragmentRegister getHtmlFragment(String fragmentName);

	void load(ClassLoader classLoader) throws Exception;

}
