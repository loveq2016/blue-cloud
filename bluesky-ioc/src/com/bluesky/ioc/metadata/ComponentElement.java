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
package com.bluesky.ioc.metadata;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public interface ComponentElement {

	/**
	 * <h3>添加ServiceMetadata</h3>
	 * 将解析XML配置文件中完毕的ServiceMetadata节点添加到其父节点中，ComponentMetadata、
	 * ServicesMetadata等
	 * 
	 * @param serviceMetadata
	 *            解析XML配置文件中完毕的ServiceMetadata节点
	 */
	public void addServiceMetadata(ServiceMetadata serviceMetadata);
}
