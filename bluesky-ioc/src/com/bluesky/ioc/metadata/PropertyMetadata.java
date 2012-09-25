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

import org.xml.sax.Attributes;

/**
 * <h3>property节点元数据</h3>
 * <ul>
 * <li>
 * 属性<strong>value</strong>，用来设置属性<strong>class</strong>的初始值或者是属性<strong>bind
 * </strong>的初始值。用于{@code new Class(arg..); }</li>
 * <li>属性<strong>type</strong>，标识属性<strong>value</strong>的类型。支持的类型有：<strong>
 * "String"</strong>、 <strong>"Boolean"</strong>、 <strong>"Byte"</strong>、
 * <strong>"Character"</strong>、 <strong>"Short"</strong>、
 * <strong>"Integer"</strong>、 <strong>"Long"</strong>、
 * <strong>"Double"</strong>、 <strong>"Float"</strong>、
 * <strong>"boolean"</strong>、 <strong>"byte"</strong>、 <strong>"char"</strong>、
 * <strong>"short"</strong>、 <strong>"int"</strong>、 <strong>"long"</strong>、
 * <strong>"double"</strong>、 <strong>"float"</strong></li>
 * </ul>
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class PropertyMetadata extends PropertyMetadataAttribute implements
		ComponentAttribute, Metadata {

	/**
	 * 
	 */
	public PropertyMetadata() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.css.sword.esb.server.microkernel.metadata.ComponentAttribute#
	 * setAttributes(org.xml.sax.Attributes)
	 */
	public void setAttributes(Attributes attributes) {
		setAttribute(ATTRIBUTE_VALUE, attributes.getValue(ATTRIBUTE_VALUE));
		setAttribute(ATTRIBUTE_TYPE, attributes.getValue(ATTRIBUTE_TYPE));
	}

	/**
	 * <h3>克隆prperty元数据</h3>
	 * 
	 * @return {@link PropertyMetadata}
	 */
	public PropertyMetadata clonePropertyMetadata() {
		PropertyMetadata pm = new PropertyMetadata();
		pm.setAttribute(ATTRIBUTE_TYPE, this.getType().getSimpleName());
		pm.setAttribute(ATTRIBUTE_VALUE, String.valueOf(this.getValue()));
		return pm;
	}
}
