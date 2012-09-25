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
package com.bluesky.ioc.core;

import com.bluesky.ioc.exception.KernelComponentDeployerException;
import com.bluesky.ioc.parse.XMLParser;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public interface ComponentDeployer {

	/**
	 * <h3>部署Component</h3>
	 * 将XML文件中配置的Component元数据解析，然后使用Component构建器构建Component，
	 * 最后将构建好的Component缓存到Context中
	 * 
	 * @param parser
	 *            XML解析器
	 * @param depender Component依赖解析器
	 * @param builder
	 *            Component构建器
	 * @throws KernelComponentDeployerException
	 *             如果解析XML元数据失败或者是构建Component的过程中出现错误，则会抛出异常
	 */
	void deploy(XMLParser parser, ComponentDepender depender, ComponentBuilder builder)
			throws KernelComponentDeployerException;

}
