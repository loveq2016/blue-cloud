/**
 * 
 */
package com.bluecloud.mvc.exception;

/**
 * @author Leo
 *
 */
public class FragmentEventException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4536675875176265160L;

	/**
	 * 
	 */
	public FragmentEventException() {
	}

	/**
	 * @param message
	 */
	public FragmentEventException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FragmentEventException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FragmentEventException(String message, Throwable cause) {
		super(message, cause);
	}

}
