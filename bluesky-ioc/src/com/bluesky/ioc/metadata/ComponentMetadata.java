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
 * <h3>component节点元数据信息</h3>
 * <ul>
 * <li><strong>属性</strong>
 * <ul>
 * <li>
 * 属性<strong>name</strong>，用来唯一标识Component的名称，可被Local或者是OutSite的Component以该标识访问
 * ，如果name重复，则会使后者覆盖前者</li>
 * <li>属性<strong>class</strong>，用来标识Component的实现</li>
 * <li>属性<strong>scope</strong>，用来标识Component的实例是singleton的还是new的，如果是singleton，
 * 则只生产一个Component实例。如果是new，则每个依赖都会生成新的Component实例。默认为singleton</li>
 * </ul>
 * </li>
 * <li><strong>子节点</strong>
 * <ul>
 * <li>
 * 节点<strong>constructor</strong>，标识Component由构造函数注入依赖，或者是以构造函数的方式实例化Component
 * ，详见 {@link ConstructorMetadata}</li>
 * <li>节点<strong>service</strong>，标识Component由set方法注入单个依赖，详见
 * {@link ServiceMetadata}</li>
 * <li>节点<strong>services</strong>，标识Component由set方法注入列表依赖，详见
 * {@link ServicesMetadata}</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ComponentMetadata extends ComponentMetadataAttribute implements
		ComponentElement, ComponentAttribute, Metadata {

	private ConstructorMetadata constructor;
	private List<ServiceMetadata> services;
	private List<ServicesMetadata> listService;

	/**
	 * 
	 */
	public ComponentMetadata() {
		services = new ArrayList<ServiceMetadata>();
		listService = new ArrayList<ServicesMetadata>();
	}

	/**
	 * @return the constructor
	 */
	public ConstructorMetadata getConstructor() {
		return constructor;
	}

	/**
	 * @param constructor
	 *            the constructor to set
	 */
	public void setConstructor(ConstructorMetadata constructor) {
		this.constructor = constructor;
	}

	/**
	 * @return the services
	 */
	public List<ServiceMetadata> getServices() {
		return services;
	}

	/**
	 * @return the listService
	 */
	public List<ServicesMetadata> getListService() {
		return listService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.css.sword.esb.server.microkernel.metadata.ComponentElement#
	 * addServiceMetadata
	 * (com.css.sword.esb.server.microkernel.metadata.ServiceMetadata)
	 */
	public void addServiceMetadata(ServiceMetadata serviceMetadata) {
		this.services.add(serviceMetadata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.css.sword.esb.server.microkernel.metadata.ComponentAttribute#
	 * setAttributes(org.xml.sax.Attributes)
	 */
	public void setAttributes(Attributes attributes) {
		setAttribute(ATTRIBUTE_NAME, attributes.getValue(ATTRIBUTE_NAME));
		setAttribute(ATTRIBUTE_CLASS, attributes.getValue(ATTRIBUTE_CLASS));
		setAttribute(ATTRIBUTE_SCOPE, attributes.getValue(ATTRIBUTE_SCOPE));

	}

	/**
	 * 
	 * @param servicesMetadata
	 */
	public void addServicesMetadata(ServicesMetadata servicesMetadata) {
		this.listService.add(servicesMetadata);
	}

	/**
	 * <h3>验证Component的构建域</h3> 检查Component的构建域，是以单例形态提供注入还是以new的形态提供注入
	 * 
	 * @return 如果Component的scope属性设置为new。返回true，如果设置为singleton，返回false
	 */
	public boolean isNew() {
		return this.getScope().equals(scopes[1]);
	}
}
