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
 * <h3>service节点的元数据信息</h3>
 * <ul>
 * <li><strong>属性</strong>
 * <ul>
 * <li>
 * 属性<strong>bind</strong>，用来绑定其他的<strong>component</strong>节点，在构建Component的过程中
 * ，如果发现了<strong>bind</strong>属性， 则不需要再解析其他属性，
 * 但可以解析其子节点的<strong>property</strong>
 * ，如果子节点存在的话。子节点<strong>property</strong>可以解决多态性问题，比如某个Component的初始值会因为需求的不同而不同
 * ，配置<strong>property</strong>后会生产不同的Component交付依赖
 * ，必须保证的是，Component的<strong>scope</strong>必须为new，要不只会依赖于第一个构建好的Component</li>
 * <li>
 * 属性<strong>name</strong>，用来指定此Component的依赖属性，配合属性<strong>class</strong>
 * 和子节点<strong>property</strong>， 组成一个Service提供给Component依赖注入</li>
 * <li>
 * 属性<strong>class</strong>，用来指定此Component的依赖实现，需要结合属性<strong>name</strong>。
 * 如果<strong>class</strong>是JDK的简单对象，
 * 比如<strong>java.lang.String</strong>、<strong>java
 * .lang.Integer</strong>等，可以配合子节点property，设置初始值。
 * 如果<strong>class</strong>是JDK的复杂对象或者是自定义的复杂对象
 * ，也可以配合子节点<strong>property</strong>，设置初始值</li>
 * </ul>
 * </li>
 * <li><strong>子节点</strong>
 * <ul>
 * <li>
 * 节点<strong>property</strong>，用来标识Service的属性值。在设定属性<strong>name</strong>和属性
 * <strong>class</strong>的情况下，设置初始值。详见 {@link PropertyMetadata}</li>
 * <li>节点<strong>entry</strong>，用来标识Service的属性值。在设定<strong>bind</strong>属性之后，
 * 可以实现多态配置Component的初始值
 * ，每个<strong>entry</strong>对于着依赖的Component的<strong>constructor
 * </strong>节点的<strong>service</strong>节点
 * ，该<strong>bind</strong>的Component的<strong
 * >scope</strong>值必须为<strong>new</strong>。详见{@link EntryMetadata}</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ServiceMetadata extends ServiceMetadataAttribute implements
		ComponentAttribute, ServiceElement, Metadata {

	private List<PropertyMetadata> properties;
	private List<EntryMetadata> entries;

	/**
	 * 
	 */
	public ServiceMetadata() {
		properties = new ArrayList<PropertyMetadata>();
		entries = new ArrayList<EntryMetadata>();
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
	 * 
	 * @param entryMetadata
	 */
	public void addEntryMetadata(EntryMetadata entryMetadata) {
		this.entries.add(entryMetadata);
	}

	/**
	 * @return the properties
	 */
	public List<PropertyMetadata> getProperties() {
		return properties;
	}

	/**
	 * @return the entries
	 */
	public List<EntryMetadata> getEntries() {
		return entries;
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
		setAttribute(ATTRIBUTE_BIND, attributes.getValue(ATTRIBUTE_BIND));
	}

	/**
	 * <h3>克隆service元数据</h3>
	 * 
	 * @return {@link ServiceMetadata}
	 */
	public ServiceMetadata cloneServiceMetadata() {
		ServiceMetadata sm = new ServiceMetadata();
		if (this.getName() != null) {
			sm.setAttribute(ATTRIBUTE_NAME, new String(this.getName()));
		}
		if (this.getClazz() != null) {
			sm.setAttribute(ATTRIBUTE_CLASS, new String(this.getClazz()));
		}
		if (this.getBind() != null) {
			sm.setAttribute(ATTRIBUTE_BIND, new String(this.getBind()));
		}
		for (PropertyMetadata pm : this.properties) {
			sm.addPropertyMetadata(pm.clonePropertyMetadata());
		}
		for (EntryMetadata em : this.entries) {
			sm.addEntryMetadata(em.cloneEntryMetadata());
		}
		return sm;
	}

}
