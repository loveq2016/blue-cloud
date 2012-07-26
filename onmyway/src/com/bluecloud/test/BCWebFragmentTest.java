/**
 * 
 */
package com.bluecloud.test;

import java.util.HashSet;
import java.util.Set;

import com.bluecloud.mvc.external.FragmentEvent;
import com.bluecloud.mvc.external.FragmentEventRegister;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 *
 */
public class BCWebFragmentTest extends HtmlFragment{

	
	/**
	 * 
	 */
	public BCWebFragmentTest() {
	}

	@Override
	protected FragmentEventRegister regEvent() {
		FragmentEventRegister reg=new FragmentEventRegister();
		reg.add(new FragmentMethodDoAdd("add"));
		return reg;
	}

	@Override
	public HtmlFragmentResponse init() {
		return null;
	}

}
