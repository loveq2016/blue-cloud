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

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>entry节点元数据</h3>
 * 该节点与父节点<strong>service</strong>的<strong>bind</strong>属性配合，
 * 实现对依赖的Component构造方法设置默认初始值。每个<strong>entry<strong>对应于依赖的Component
 * 的<strong>constructor</strong>节点的<strong>service</strong>节点
 * <ul>
 * <li>子节点<strong>property</strong>，用来标识Service的属性值。在设定属性name和属性
 * class的情况下，设置初始值。详见 {@link PropertyMetadata}</li>
 * </ul>
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class EntryMetadata implements ServiceElement {
	private List<PropertyMetadata> properties;

	/**
	 * 
	 */
	public EntryMetadata() {
		properties = new ArrayList<PropertyMetadata>();
	}

	/**
	 * <h3>获得属性列表</h3>
	 * 
	 * @return the properties
	 */
	public List<PropertyMetadata> getProperties() {
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.css.sword.esb.server.microkernel.metadata.ServiceElement#
	 * addPropertyMetadata
	 * (com.css.sword.esb.server.microkernel.metadata.PropertyMetadata)
	 */
	public void addPropertyMetadata(PropertyMetadata propertyMetadata) {
		this.properties.add(propertyMetadata);
	}

	/**
	 * <h3>克隆entry元数据</h3>
	 * 
	 * @return {@link EntryMetadata}
	 */
	public EntryMetadata cloneEntryMetadata() {
		EntryMetadata em = new EntryMetadata();
		for (PropertyMetadata pm : this.properties) {
			em.addPropertyMetadata(pm.clonePropertyMetadata());
		}
		return em;
	}

}
