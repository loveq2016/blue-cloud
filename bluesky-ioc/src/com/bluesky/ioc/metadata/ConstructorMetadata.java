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

import org.xml.sax.Attributes;

/**
 * <h3>constructor节点元数据信息</h3>
 * <ul>
 * <li>属性<strong>index</strong>，标识Component由第几个构造函数注入依赖，默认0，也即由第1个构造函数注入依赖。
 * 如果这个构造函数不是<strong>public</strong>声明的，则无法注入依赖</li>
 * <li>子节点<strong>service</strong>，标识Component由构造函数注入依赖，每个Service按顺序对应构造函数的参数。详见
 * {@link ServiceMetadata}</li>
 * </ul>
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ConstructorMetadata extends ConstructorMetadataAttribute implements
		ComponentElement, ComponentAttribute, Metadata {

	private List<ServiceMetadata> services;

	/**
	 * 
	 */
	public ConstructorMetadata() {
		services = new ArrayList<ServiceMetadata>();
	}

	/**
	 * @return the services
	 */
	public List<ServiceMetadata> getServices() {
		return services;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.css.sword.esb.server.microkernel.metadata.ComponentElement#
	 * addServiceMetadata
	 * (com.css.sword.esb.server.microkernel.metadata.ServiceMetadata)
	 */
	public void addServiceMetadata(ServiceMetadata serviceMetadata) {
		services.add(serviceMetadata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.css.sword.esb.server.microkernel.metadata.ComponentAttribute#
	 * setAttributes(org.xml.sax.Attributes)
	 */
	public void setAttributes(Attributes attributes) {
		setAttribute(ATTRIBUTE_INDEX, attributes.getValue(ATTRIBUTE_INDEX));
	}
}
