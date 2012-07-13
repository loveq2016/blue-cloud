/**
 * 
 */
package com.bluecloud.server;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.bluecloud.core.BlueCloud;
import com.bluesky.ioc.ComponentFactory;
import com.bluesky.ioc.StandaloneComponentFactory;
import com.bluesky.ioc.exception.KernelException;
import com.bluesky.ioc.exception.KernelSourceException;
import com.bluesky.ioc.source.XMLPathSource;
import com.bluesky.logging.Log;
import com.bluesky.logging.LogFactory;

/**
 * @author leo
 * 
 */
public class DomainServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7746262162585708515L;

	private Log log=LogFactory.getLog(DomainServlet.class);
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		DomainConfig domainConfig=new DomainConfig(config);
		String confPath = domainConfig.getPath();
		if(log.isInfoEnabled()){
			log.info("加载配置信息：".concat(confPath));
			log.info("加载组件...");
		}
		try {
			ComponentFactory componentFac = new StandaloneComponentFactory(
					new XMLPathSource(new File(confPath)));
			if(log.isInfoEnabled()){
				log.info("开始启动服务器...");
			}
			BlueCloud.bootstrap(componentFac,domainConfig);
			if(log.isInfoEnabled()){
				log.info("开始启动成功");
			}
		} catch (KernelSourceException e) {
			log.error("服务器启动失败", e);
		} catch (KernelException e) {
			log.error("服务器启动失败", e);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
