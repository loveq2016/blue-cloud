/**
 * 
 */
package com.bluecloud.mvc.exception;

/**
 * @author Leo
 *
 */
public class HtmlFragmentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437598241496231760L;

	/**
	 * 
	 */
	public HtmlFragmentException() {
	}

	/**
	 * @param arg0
	 */
	public HtmlFragmentException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public HtmlFragmentException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HtmlFragmentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
