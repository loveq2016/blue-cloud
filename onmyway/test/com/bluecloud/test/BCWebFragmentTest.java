/**
 * 
 */
package com.bluecloud.test;

import com.bluecloud.mvc.external.FragmentEventRegister;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 *
 */
public class BCWebFragmentTest extends HtmlFragment{

	@Override
	protected FragmentEventRegister regEvent() {
		FragmentEventRegister reg=new FragmentEventRegister();
		reg.add(new FragmentMethodDoAdd("add"));
		return reg;
	}


	@Override
	public HtmlFragmentResponse init(HtmlFragmentRequest request) {
		this.getResponse();
		return null;
	}

}
