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

import com.bluesky.ioc.exception.KernelComponentDependerException;
import com.bluesky.ioc.metadata.ComponentMetadata;
import com.bluesky.ioc.metadata.ServiceMetadata;
import com.bluesky.ioc.metadata.ServicesMetadata;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public interface ComponentDepender {

	/**
	 * <h3>构造函数依赖注入</h3>
	 * 
	 * @param componentMetadata
	 *            Component的元数据
	 * @param context
	 *            已经解决依赖的Component上下文
	 * @return 构造函数注入的Component对象
	 * @throws KernelComponentDependerException
	 */
	public Object injectConstructor(ComponentMetadata componentMetadata,
			ComponentContext context) throws KernelComponentDependerException;

	/**
	 * <h3>属性依赖注入</h3>
	 * 
	 * @param componentObject
	 *            Component被注入对象
	 * @param service
	 *            注入的依赖Component元数据绑定信息
	 * @param context
	 *            已经解决依赖的Component上下文
	 * @throws KernelComponentDependerException
	 */
	public void inject(Object componentObject, ServiceMetadata service,
			ComponentContext context) throws KernelComponentDependerException;

	/**
	 * <h3>列表依赖注入</h3>
	 * 
	 * @param componentObject
	 *            Component被注入对象
	 * @param services
	 *            注入的依赖Component元数据绑定信息
	 * @param context
	 *            已经解决依赖的Component上下文
	 * @throws KernelComponentDependerException
	 */
	public void injectList(Object componentObject, ServicesMetadata services,
			ComponentContext context) throws KernelComponentDependerException;

}
