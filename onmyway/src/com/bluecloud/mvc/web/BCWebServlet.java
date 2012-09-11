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
import com.bluecloud.mvc.external.HtmlFragment;
import com.bluecloud.mvc.web.http.HtmlFragmentDispatcher;
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
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		if(!BCWeb.isStart()){
			throw new ServletException(BCWeb.getStartInfo());
		}
		HttpFragmentHandler fragmentHandler=BCWeb.createHtmlFragmentHandler(req,res);
		HtmlFragmentDepository depository=BCWeb.getFragmentDepository();
		String fragmentName=fragmentHandler.getRequestFragment();
		HtmlFragment fragment=depository.getHtmlFragment(fragmentName);
		try {
			ResponseData resData=fragmentHandler.service(fragment);
			HtmlFragmentDispatcher dispatch = resData.getDispatch();
			if (null != dispatch) {
				dispatch.exce();
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		BCWeb.start(config);
	}
}
