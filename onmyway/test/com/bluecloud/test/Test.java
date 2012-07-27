/**
 * 
 */
package com.bluecloud.test;

import com.bluecloud.mvc.external.FragmentEvent;
import com.bluecloud.mvc.external.FragmentEventRegister;
import com.bluecloud.mvc.web.http.BCWebRequest;
import com.bluecloud.mvc.web.http.BCWebResponse;
import com.bluecloud.mvc.web.http.ResponseData;
import com.bluecloud.mvc.web.http.HtmlFragmentRequest;
import com.bluecloud.mvc.web.http.HtmlFragmentResponse;

/**
 * @author leo
 *
 */
public class Test {

	/**
	 * @param res 
	 * @param req 
	 * 
	 */
	public Test(HtmlFragmentResponse res, HtmlFragmentRequest req) {
		BCWebFragmentTest s=new BCWebFragmentTest();
		FragmentEventRegister r=s.regEvent();
		FragmentEvent e=r.find("add");
		s.setResponse(res);
		e.execute(req, s);
		ResponseData data=res.getData();
		System.out.println(data.getDispatch());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HtmlFragmentRequest req=new BCWebRequest(null);
		HtmlFragmentResponse  res=new BCWebResponse(null);
		new Test(res, req);
	}

}
