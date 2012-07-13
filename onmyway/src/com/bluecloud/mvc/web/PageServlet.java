/**
 * 
 */
package com.bluecloud.mvc.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluecloud.core.engine.page.HtmlPage;
import com.bluecloud.core.engine.page.PageDepository;

/**
 * @author leo
 *
 */
public class PageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5877934011390006162L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String pxmlPath=req.getServletPath();
		HtmlPage htmlPage=PageDepository.getInstance().getPage(pxmlPath);
		res.getWriter().write(htmlPage.getContext());
	}
}
