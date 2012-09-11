/**
 * 
 */
package com.bluecloud.mvc.external;

import com.bluecloud.mvc.exception.FragmentEventException;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 *
 */
public abstract class FragmentEvent {
	private String name;

	public FragmentEvent(final String name){
		this.name=name;
	}

	/**
	 * 
	 * @param req
	 * @param fragment
	 * @return
	 * @throws Exception
	 */
	public abstract HtmlFragmentResponse execute(HtmlFragmentRequest req,HtmlFragment fragment) throws FragmentEventException;
	
	@Override
	public String toString() {
		return this.name;
	}

}
