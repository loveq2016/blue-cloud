/**
 * 
 */
package com.bluecloud.mvc.external;

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

	public abstract HtmlFragmentResponse execute(HtmlFragmentRequest req,HtmlFragment fragment);
	
	@Override
	public String toString() {
		return this.name;
	}

}
