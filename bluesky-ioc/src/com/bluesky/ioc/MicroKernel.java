/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluesky.ioc;

import com.bluesky.ioc.core.ComponentBuilder;
import com.bluesky.ioc.core.ComponentContext;
import com.bluesky.ioc.core.ComponentDepender;
import com.bluesky.ioc.core.ComponentDeployer;
import com.bluesky.ioc.core.KernelComponentBuilder;
import com.bluesky.ioc.core.KernelComponentContext;
import com.bluesky.ioc.core.KernelComponentDepender;
import com.bluesky.ioc.core.KernelComponentDeployer;
import com.bluesky.ioc.exception.KernelException;
import com.bluesky.ioc.parse.KernelXMLParser;
import com.bluesky.ioc.parse.XMLParser;
import com.bluesky.ioc.source.KernelSource;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class MicroKernel {

	private boolean isLoaded = false;
	private static MicroKernel kernel = null;
	private ComponentContext context;

	/**
	 * 
	 */
	private MicroKernel() {
	}

	/**
	 * <h3>获得MicroKernel的实例</h3>
	 * 
	 * @return 如果MicroKernel为null，创建新实例返回。否则返回已经有的实例
	 */
	public static MicroKernel getInstance() {
		if (kernel == null) {
			kernel = new MicroKernel();
		}
		return kernel;
	}

	/**
	 * <h3>加载Component</h3> 根据xml文件的URL数组，加载Component的配置信息
	 * 
	 * @param source
	 *            xml文件的资源
	 * @return context Component的上下文，用户可以根据name从context中获得Component
	 * @throws KernelException
	 */
	protected ComponentContext load(KernelSource source) throws KernelException {
		if (!isLoaded) {
			context = new KernelComponentContext();
			isLoaded = true;
		}
		XMLParser parser = new KernelXMLParser();
		parser.load(source);
		ComponentDepender depender=new KernelComponentDepender();
		ComponentBuilder builder = new KernelComponentBuilder(depender,context);
		ComponentDeployer deployer = new KernelComponentDeployer();
		deployer.deploy(parser,depender,builder);
		parser=null;
		depender=null;
		builder=null;
		deployer=null;
		return getComponentContext();
	}

	protected ComponentContext getComponentContext() {
		return context;
	}
}
