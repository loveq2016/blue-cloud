/**
 * 
 */
package com.bluecloud.mvc.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluecloud.mvc.api.HtmlFragmentDepository;
import com.bluecloud.mvc.api.HttpFragmentHandler;
import com.bluecloud.mvc.core.BCWeb;
import com.bluecloud.mvc.external.FragmentEventRegister;
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.external.HtmlFragmentRegister;
import com.bluecloud.mvc.web.http.ResponseData;

/**
 * @author leo
 *
 */
public class BCWebServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6121738819477670441L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!BCWeb.isStart()){
			throw new ServletException(BCWeb.getStartInfo());
		}
		HttpFragmentHandler fragmentHandler=BCWeb.createHtmlFragmentHandler(req,resp);
		HtmlFragmentDepository depository=BCWeb.getFragmentDepository();
		String fragmentName=fragmentHandler.getRequestFragment();
		HtmlFragmentRegister register=depository.getHtmlFragment(fragmentName);
		HtmlFragment fragment=register.getFragment();
		FragmentEventRegister eventRegister=register.getEventRegister();
		ResponseData respData=fragmentHandler.service(fragment,eventRegister);
		String dispatch=respData.getDispatch();
		if(null!=dispatch&&!dispatch.equals("")){
			
		}else{
			
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		BCWeb.start();
	}
}
