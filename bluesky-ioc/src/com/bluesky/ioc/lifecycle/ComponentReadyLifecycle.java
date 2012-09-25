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
package com.bluesky.ioc.lifecycle;

import com.bluesky.ioc.classloader.ClassHandler;
import com.bluesky.ioc.core.ComponentContext;
import com.bluesky.ioc.core.ComponentDepender;
import com.bluesky.ioc.core.ComponentObject;
import com.bluesky.ioc.core.KernelComponentContext;
import com.bluesky.ioc.exception.KernelComponentLifecycleException;
import com.bluesky.ioc.metadata.ComponentMetadata;
import com.bluesky.ioc.metadata.ServiceMetadata;
import com.bluesky.ioc.metadata.ServicesMetadata;
import com.bluesky.logging.Log;
import com.bluesky.logging.LogFactory;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class ComponentReadyLifecycle implements ComponentLifecycle {

	private Log log = LogFactory.getLog(ComponentReadyLifecycle.class);
	private ComponentDepender depender;
	private ComponentContext context;

	/**
	 * @param context
	 * @param depender
	 * 
	 */
	public ComponentReadyLifecycle(ComponentDepender depender,
			ComponentContext context) {
		this.depender = depender;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.lifecycle.ComponentLifecycle#invoke
	 * (com.css.sword.esb.server.microkernel.core.ComponentObject)
	 */
	public void invoke(ComponentObject component, ComponentLifecycle lifecycle)
			throws KernelComponentLifecycleException {
		ComponentMetadata componentMetadata = component.getComponentMetadate();
		try {
			Object componentObject = component.getObject();
			if (componentObject == null) {
				// 是否存在Constructor依赖注入，如果存在，Constructor注入依赖。
				if (componentMetadata.getConstructor() != null) {
					componentObject = depender.injectConstructor(
							componentMetadata, context);
				} else {
					componentObject = ClassHandler.getClass(componentMetadata
							.getClazz(), true);
				}
				// 是否存在属性依赖
				for (ServiceMetadata service : componentMetadata.getServices()) {
					depender.inject(componentObject, service, context);
				}
				// 是否存在属性列表依赖
				for (ServicesMetadata services : componentMetadata
						.getListService()) {
					depender.injectList(componentObject, services, context);
				}
				component.setDependObject(componentObject);
				((KernelComponentContext) context).addReadyComponent(component);
				log.info("构建完成Component：" + component.getName() + "："
						+ componentObject);
				lifecycle.invoke(component, this);
				((KernelComponentContext) context)
						.removeNotReadyComponent(component);
			} else {
				if (componentMetadata.getConstructor() != null) {
					componentObject = depender.injectConstructor(
							componentMetadata, context);
				}
				// 是否存在属性依赖
				for (ServiceMetadata service : componentMetadata.getServices()) {
					depender.inject(componentObject, service, context);
				}
				// 是否存在属性列表依赖
				for (ServicesMetadata services : componentMetadata
						.getListService()) {
					depender.injectList(componentObject, services, context);
				}
				component.setDependObject(componentObject);
				((KernelComponentContext) context).addReadyComponent(component);
				log.info("构建完成Component：" + component.getName() + "："
						+ componentObject);
				lifecycle.invoke(component, new ComponentDependLifecycle(
						depender, context));
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("微内核构建Component：" + componentMetadata.getName()
						+ "失败", e);
			}
			throw new KernelComponentLifecycleException(e);
		}
	}

}
