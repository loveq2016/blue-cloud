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

import java.util.Set;

import com.bluesky.ioc.core.ComponentContext;
import com.bluesky.ioc.core.ComponentObject;
import com.bluesky.ioc.core.KernelComponentContext;
import com.bluesky.ioc.exception.KernelComponentLifecycleException;

/**
 * Component生命周期的未就绪阶段，即Component的相关依赖还没有完全注入，需等待其依赖的Component解析
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ComponentNotReadyLifecycle implements ComponentLifecycle {
	private ComponentContext context;

	/**
	 * 
	 * @param context
	 */
	public ComponentNotReadyLifecycle(ComponentContext context) {
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
		Set<ComponentObject> notReadyComponents = ((KernelComponentContext) context)
				.getNotReadyComponents(component.getName());
		if (null != notReadyComponents) {
			for (ComponentObject notReadyComponent : notReadyComponents) {
				if (notReadyComponent.isReady(component.getName())) {
					if (((KernelComponentContext) context)
							.getDependComponent(component.getName()) != null) {
						((KernelComponentContext) context)
								.removeNotReadyComponent(component);
					}
					lifecycle.invoke(notReadyComponent, this);
				}
			}
		}
	}

}
