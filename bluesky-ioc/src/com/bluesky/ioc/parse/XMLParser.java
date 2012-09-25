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
package com.bluesky.ioc.parse;

import com.bluesky.ioc.exception.KernelXMLParserException;
import com.bluesky.ioc.metadata.CompositeMetadata;
import com.bluesky.ioc.source.KernelSource;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public interface XMLParser {
	/**
	 * <h3>解析xml文件</h3> 把XML文件中的元数据解析为Kernel的部署数据
	 * 
	 * @throws KernelXMLParserException
	 */
	public CompositeMetadata parse() throws KernelXMLParserException;

	/**
	 * <h3>加载XML配置文件资源</h3>
	 * 
	 * @param source
	 *            XML配置文件的资源
	 * @throws KernelXMLParserException
	 */
	public void load(KernelSource source) throws KernelXMLParserException;
}
