package com.yonyouhealth.uaph.framework.web.comm;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {
//	protected static final LogWritter log = LogFactory.getLogger(CharacterEncodingFilter.class);
	protected String encoding = null;
	protected FilterConfig filterConfig = null;
	protected boolean ignore = true;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		if ((!this.ignore) || (request.getCharacterEncoding() == null)) {
			String str = selectEncoding(request);
			if (str != null)
				request.setCharacterEncoding(str);
		}
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		this.encoding = config.getInitParameter("encoding");
		String str = config.getInitParameter("ignore");
		if (str == null)
			this.ignore = true;
		else if (str.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (str.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
	}

	protected String selectEncoding(ServletRequest paramServletRequest) {
		return this.encoding;
	}
}