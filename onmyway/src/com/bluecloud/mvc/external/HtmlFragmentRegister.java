/**
 * 
 */
package com.bluecloud.mvc.external;

/**
 * @author leo
 * 
 */
public class HtmlFragmentRegister {

	private HtmlFragment fragment;
	private FragmentEventRegister eventRegister;

	/**
	 * 
	 */
	public HtmlFragmentRegister() {
	}

	public HtmlFragment getFragment() {
		return fragment;
	}

	public FragmentEventRegister getEventRegister() {
		return eventRegister;
	}

	/**
	 * 
	 * @param fragment
	 */
	public void add(HtmlFragment fragment) {
		if(fragment!=null){
			this.eventRegister=fragment.regEvent();
		}
		this.fragment = fragment;
	}

}
