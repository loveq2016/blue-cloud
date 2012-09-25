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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.xml.sax.Attributes;

/**
 * <h3>services节点的元数据信息</h3>
 * <ul>
 * <li><strong>属性</strong>
 * <ul>
 * <li>属性<strong>name</strong>，标识Component中需要注入的属性名称</li>
 * <li>属性<strong>type</strong>，标识Component中注入依赖的列表类型，目前支持类型：Map、List、Set、
 * Array四种列表类型 。在java中分别默认为{@link HashMap}、{@link ArrayList}、{@link HashSet}
 * 和数组。如果是map列表，key默认为子节点<strong>service </strong>的<strong>bind</strong>属性值
 * ，如果子节点
 * <strong>service</strong>设置<strong>name</strong>属性值，则为name。只支持一维数组，默认的数组为Object[]</li>
 * <li>
 * 属性<strong>size</strong>，标识Component中注入依赖的列表的初始大小 。min=1、max=256，默认值为1，</li>
 * <li>
 * 属性<strong>limited</strong>，标识Component中注入依赖的列表限定类型，如果设置了此属性，则列表中的类型均为设定的类型
 * 。默认为{@link Object}</li>
 * </ul>
 * </li>
 * <li><strong>子节点</strong>
 * <ul>
 * <li>子节点<strong>service</strong>，标识Component以列表的形式注入依赖，详见
 * {@link ServiceMetadata}</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ServicesMetadata extends ServicesMetadataAttribute implements
		ComponentElement, ComponentAttribute, Metadata {

	private List<ServiceMetadata> services;

	/**
	 * 
	 */
	public ServicesMetadata() {
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
		setAttribute(ATTRIBUTE_NAME, attributes.getValue(ATTRIBUTE_NAME));
		setAttribute(ATTRIBUTE_TYPE, attributes.getValue(ATTRIBUTE_TYPE));
		setAttribute(ATTRIBUTE_SIZE, attributes.getValue(ATTRIBUTE_SIZE));
		setAttribute(ATTRIBUTE_LIMITED, attributes.getValue(ATTRIBUTE_LIMITED));
	}

}
