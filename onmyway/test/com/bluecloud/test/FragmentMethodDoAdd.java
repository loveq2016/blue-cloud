/**
 * 
 */
package com.bluecloud.test;

import com.bluecloud.mvc.external.FragmentEvent;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 * 
 */
public class FragmentMethodDoAdd extends FragmentEvent {

	public FragmentMethodDoAdd(String name) {
		super(name);
	}

	public HtmlFragmentResponse execute(HtmlFragmentRequest req,HtmlFragment fragment) {
		HtmlFragmentResponse res=fragment.getResponse();
		res.addView("sss");
		return res;
	}

}
