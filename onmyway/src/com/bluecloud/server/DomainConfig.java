/**
 * 
 */
package com.bluecloud.server;

import javax.servlet.ServletConfig;

/**
 * @author leo
 * 
 */
public final class DomainConfig {

	private ServletConfig config;

	private static final String WEBINF="/WEB-INF";
	private static final String CONF=WEBINF.concat("/conf");
	private static final String CLAZZES=WEBINF.concat("/classes");
	/**
	 * @param config
	 * 
	 */
	protected DomainConfig(ServletConfig config) {
		this.config = config;
	}

	public String getPath() {
		return this.config.getServletContext().getRealPath("/").concat(CONF);
	}

}
