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

import java.util.List;
import java.util.Map;

import com.bluesky.ioc.exception.KernelComponentBuilderException;
import com.bluesky.ioc.exception.KernelComponentLifecycleException;
import com.bluesky.ioc.lifecycle.ComponentDependLifecycle;
import com.bluesky.ioc.lifecycle.ComponentLifecycle;
import com.bluesky.ioc.lifecycle.ComponentNotReadyLifecycle;
import com.bluesky.ioc.lifecycle.ComponentReadyLifecycle;
import com.bluesky.ioc.metadata.ComponentMetadata;
import com.bluesky.ioc.metadata.ConstructorMetadata;
import com.bluesky.ioc.metadata.ServiceMetadata;
import com.bluesky.ioc.metadata.ServicesMetadata;

/**
 * Component构建器
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class KernelComponentBuilder implements ComponentBuilder {

	private ComponentContext context;
	private ComponentDepender depender;
	private ComponentLifecycle readyLifecycle;
	private ComponentLifecycle notreadyLifecycle;
	private ComponentLifecycle dependLifecycle;

	/**
	 * 
	 * @param depender
	 * @param context
	 */
	public KernelComponentBuilder(ComponentDepender depender,
			ComponentContext context) {
		this.depender = depender;
		this.context = context;
		readyLifecycle = new ComponentReadyLifecycle(this.depender,
				this.context);
		notreadyLifecycle = new ComponentNotReadyLifecycle(this.context);
		dependLifecycle = new ComponentDependLifecycle(this.depender,
				this.context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.core.ComponentBuilder#build(com.
	 * css.sword.esb.server.microkernel.core.ComponentObject, java.util.Map)
	 */
	public void build(ComponentObject component,
			Map<String, ComponentMetadata> components)
			throws KernelComponentBuilderException {
		int isNotDepend = 0;
		ComponentMetadata componentMetadata = component.getComponentMetadate();
		ConstructorMetadata constructor = componentMetadata.getConstructor();
		// 是否存在Constructor依赖注入
		if (null != constructor) {
			if (!this.isConstructorDepend(constructor, component, components)) {
				isNotDepend++;
			}
		}
		// 是否存在列表属性依赖注入
		if (!this.isServicesDepend(componentMetadata.getListService(),
				component, components)) {
			isNotDepend++;
		}
		// 是否存在属性依赖注入
		if (!this.isServiceList(componentMetadata.getServices(), component,
				components)) {
			isNotDepend++;
		}
		try {
			if (isNotDepend == 0) {
				readyLifecycle.invoke(component, notreadyLifecycle);
			} else {
				dependLifecycle.invoke(component, notreadyLifecycle);
			}
		} catch (KernelComponentLifecycleException e) {
			throw new KernelComponentBuilderException(e);
		}
	}

	/**
	 * 
	 * @param listService
	 * @param component
	 * @param components
	 * @return
	 * @throws KernelComponentBuilderException
	 */
	private boolean isServicesDepend(List<ServicesMetadata> listService,
			ComponentObject component, Map<String, ComponentMetadata> components)
			throws KernelComponentBuilderException {
		for (ServicesMetadata services : listService) {
			if (!this.isServiceList(services.getServices(), component,
					components)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param constructor
	 * @param component
	 * @param components
	 * @return 如果存在依赖，返回false，否则返回true;
	 * @throws KernelComponentBuilderException
	 */
	private boolean isConstructorDepend(ConstructorMetadata constructor,
			ComponentObject component, Map<String, ComponentMetadata> components)
			throws KernelComponentBuilderException {
		return this.isServiceList(constructor.getServices(), component,
				components);
	}

	/**
	 * 
	 * @param services
	 * @param co
	 * @param components
	 * @return 如果存在依赖，返回false，否则返回true
	 * @throws KernelComponentBuilderException
	 */
	private boolean isServiceList(List<ServiceMetadata> services,
			ComponentObject component, Map<String, ComponentMetadata> components)
			throws KernelComponentBuilderException {
		int i = 0;
		for (ServiceMetadata service : services) {
			if (!this.isServiceDepend(service, component, components)) {
				i++;
			}
		}
		if (i > 0) {
			return false;
		}
		return true;
	}

	/**
	 * <h3>验证是否存在依赖</h3>
	 * 
	 * @param service
	 * @param component
	 * @param components
	 * @return 如果存在依赖，返回false，否则返回true
	 * @throws KernelComponentBuilderException
	 */
	private boolean isServiceDepend(ServiceMetadata service,
			ComponentObject component, Map<String, ComponentMetadata> components)
			throws KernelComponentBuilderException {
		String bind = service.getBind();
		if (bind != null) {
			// 如果Component没有解决依赖，则验证Component是否存在，即使没有解决依赖，如果不存在，抛出异常
			if (components.get(bind) == null) {
				throw new KernelComponentBuilderException("Component：" + bind
						+ "不存在，无法解析依赖");
			}
			if (context.getComponent(bind) == null) {
				component.addDependComponent(service);
				((KernelComponentContext) context).addNotReadyComponent(bind,
						component);
				return false;
			}
		}
		return true;
	}
}
