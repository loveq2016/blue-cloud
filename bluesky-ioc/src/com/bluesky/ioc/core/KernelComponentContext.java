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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class KernelComponentContext implements ComponentContext {

	private Map<String, ComponentObject> readyContext = new HashMap<String, ComponentObject>();
	private Map<String, Set<ComponentObject>> notReadyContext = new HashMap<String, Set<ComponentObject>>();
	private Map<String, ComponentObject> dependContext = new HashMap<String, ComponentObject>();

	/**
	 * 
	 */
	public KernelComponentContext() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.core.KernelContext#getComponent(
	 * java.lang.String)
	 */
	public Object getComponent(String name) {
		if (name == null) {
			return null;
		}
		ComponentObject component = readyContext.get(name);
		return component == null ? null : component.getObject();
	}

	/**
	 * 
	 * @param bind
	 *            被依赖的Component标识
	 * @param component
	 *            NotReady生命周期的Component
	 */
	protected void addNotReadyComponent(String bind, ComponentObject component) {
		Set<ComponentObject> notReadyComponents = notReadyContext.get(bind);
		if (null == notReadyComponents) {
			notReadyComponents = new HashSet<ComponentObject>();
		}
		notReadyComponents.add(component);
		notReadyContext.put(bind, notReadyComponents);
	}

	/**
	 * <h3>添加已经完全解决依赖的Component，标识着Component已经构建完毕</h3>
	 * 
	 * @param component
	 */
	public void addReadyComponent(ComponentObject component) {
		readyContext.put(component.getName(), component);
	}

	/**
	 * <h3>获得被依赖的Component的依赖Component列表</h3>
	 * 
	 * @param name
	 *            被依赖的Component
	 * @return 该Component的依赖列表
	 */
	public Set<ComponentObject> getNotReadyComponents(String name) {
		return this.notReadyContext.get(name);
	}

	/**
	 *<h3>移除NotReadyComponent</h3>
	 * 
	 * @param component
	 */
	public void removeNotReadyComponent(ComponentObject component) {
		notReadyContext.remove(component.getName());
	}

	/**
	 * <h3>获得已经构建好的Component对象</h3>
	 * 
	 * @param bind
	 *            Component的标识
	 * @return {@link ComponentObject}
	 */
	public ComponentObject getReadyComponent(String bind) {
		return readyContext.get(bind);
	}

	/**
	 * <h3>添加依赖的Component</h3>
	 * 
	 * @param component
	 *            要添加的Component
	 */
	public void addDependComponent(ComponentObject component) {
		dependContext.put(component.getName(), component);
	}

	/**
	 * <h3>获得依赖的Component</h3>
	 * 
	 * @param bind
	 *            依赖的Component的名称
	 * @return {@link ComponentObject}
	 */
	public ComponentObject getDependComponent(String bind) {
		return dependContext.get(bind);
	}

	/**
	 * <h3>移除依赖的Component</h3>
	 * 
	 * @param component
	 *            要移除的Component
	 */
	public void removeDependComponent(ComponentObject component) {
		dependContext.remove(component.getName());
	}

}
