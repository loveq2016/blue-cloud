/*
 * Copyright 2010 the original author or authors.
 * 
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

import java.util.Map;

import com.bluesky.ioc.exception.KernelComponentBuilderException;
import com.bluesky.ioc.metadata.ComponentMetadata;

/**
 * Component构建器
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public interface ComponentBuilder {

	/**
	 * <h3>解析Component元数据，构建Component对象，进行依赖注入</h3>
	 * 构建Component依赖关系，如果没有依赖注入或者已经解决了依赖注入，则执行Component的Ready生命周期。
	 * 否则将Component的生命周期设置为NotReady，等待其依赖的Component执行Ready生命周期后完成依赖注入。
	 * 
	 * @param component
	 *            Component对象
	 * @param components
	 * @throws KernelComponentBuilderException
	 */
	public void build(ComponentObject component,
			Map<String, ComponentMetadata> components)
			throws KernelComponentBuilderException;

}
