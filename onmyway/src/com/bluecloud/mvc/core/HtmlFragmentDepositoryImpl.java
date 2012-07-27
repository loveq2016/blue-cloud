/**
 * 
 */
package com.bluecloud.mvc.core;

import java.util.HashMap;
import java.util.Map;

import com.bluecloud.mvc.api.HtmlFragmentDepository;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.external.HtmlFragmentRegister;

/**
 * @author leo
 * 
 */
final class HtmlFragmentDepositoryImpl implements HtmlFragmentDepository {

	private Map<String, HtmlFragmentRegister> fragments;

	public HtmlFragmentDepositoryImpl() {
		fragments=new HashMap<String,HtmlFragmentRegister>();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bluecloud.mvc.api.HtmlFragmentDepository#getHtmlFragment(java.lang
	 * .String)
	 */
	@Override
	public HtmlFragmentRegister getHtmlFragment(String fragmentName) {
		return fragments.get(fragmentName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluecloud.mvc.api.HtmlFragmentDepository#load(java.lang.ClassLoader)
	 */
	@Override
	public void load(ClassLoader classLoader) throws Exception {
		HtmlFragmentRegister fragmentReg = new HtmlFragmentRegister();
		Class<?> clazz=classLoader.loadClass("com.bluecloud.test.BCWebFragmentTest");
		HtmlFragment fragment=(HtmlFragment) clazz.newInstance();
		fragmentReg.add(fragment);
		fragments.put(clazz.getSimpleName().toLowerCase(), fragmentReg);
	}

}
