/**
 * 
 */
package com.bluecloud.mvc.api;

import com.bluecloud.mvc.external.HtmlFragment;

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
	HtmlFragment getHtmlFragment(String fragmentName);

	void load(ClassLoader classLoader) throws Exception;

}
