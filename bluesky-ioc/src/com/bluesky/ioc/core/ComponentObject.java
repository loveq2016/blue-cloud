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
import java.util.Map;

import com.bluesky.ioc.metadata.ComponentMetadata;
import com.bluesky.ioc.metadata.ServiceMetadata;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ComponentObject {

	private Object componentObject = null;
	private ComponentMetadata componentMetadata = null;
	private Map<String, ServiceMetadata> dependComponents = null;

	/**
	 * @param componentMetadata
	 * 
	 */
	public ComponentObject(ComponentMetadata componentMetadata) {
		this.componentMetadata = componentMetadata;
	}

	/**
	 * <h3>设置Component的Object</h3>
	 * 
	 * @param componentObject
	 */
	public void setDependObject(Object componentObject) {
		this.componentObject = componentObject;
	}

	/**
	 * <h3>添加依赖的Component</h3>
	 * 
	 * @param service
	 *            依赖的Component元数据
	 */
	public void addDependComponent(ServiceMetadata service) {
		if (null == this.dependComponents) {
			dependComponents = new HashMap<String, ServiceMetadata>();
		}
		dependComponents.put(service.getBind(), service);
	}

	/**
	 * <h3>获得Component对象的名称</h3>
	 * 
	 * @return Component对象的名称
	 */
	public String getName() {
		return componentMetadata.getName();
	}

	/**
	 * <h3>获得Component的Object</h3>
	 * 
	 * @return Component的Object
	 */
	public Object getObject() {
		return componentObject;
	}

	/**
	 * <h3>获得Component对象的元数据信息</h3>
	 * 
	 * @return Component对象的元数据信息
	 */
	public ComponentMetadata getComponentMetadate() {
		return componentMetadata;
	}

	/**
	 * <h3>是否是Ready生命周期</h3> 判断该Component是否已经解决了依赖，如果已经解决依赖，则返回true,否则返回false
	 * 
	 * @param name
	 *            Component的名称标识
	 * @return 如果该Component已经解决了所有依赖，返回true，否则返回false
	 */
	public boolean isReady(String name) {
		dependComponents.remove(name);
		return dependComponents.isEmpty();
	}

	/**
	 * 验证Component的scope
	 * 
	 * @return 如果scope==new，返回true。否则返回false
	 */
	public boolean isNew() {
		return componentMetadata.isNew();
	}
}
