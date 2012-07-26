/**
 * 
 */
package com.bluecloud.mvc.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluecloud.mvc.api.HtmlFragmentDepository;
import com.bluecloud.mvc.api.HttpFragmentHandler;
import com.bluecloud.mvc.web.http.BCWebRequest;
import com.bluecloud.mvc.web.http.BCWebResponse;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 * 
 */
public final class BCWeb {

	private static HttpServletResponse response;
	private static HtmlFragmentDepository fragDepository;
	private static boolean isStart = false;

	public static HttpFragmentHandler createHtmlFragmentHandler(
			HttpServletRequest req, HttpServletResponse resp) {
		HtmlFragmentRequest request = new BCWebRequest(req);
		HtmlFragmentResponse response = new BCWebResponse(resp);
		return new HtmlFragmentHandlerImpl(request, response);
	}

	public static HtmlFragmentDepository getFragmentDepository() {
		return fragDepository;
	}

	public static void start() {
		if (isStart) {
			return;
		}
		fragDepository = new HtmlFragmentDepositoryImpl();
		try {
			fragDepository.load(Thread.currentThread().getContextClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
		isStart = true;
	}

}