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

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ServiceMetadataAttribute extends GlobalAttribute {
	// 指向组件，如果此组件没有退外暴露成服务，则绑定无效。此服务为不可用
	private String bind;

	/**
	 * 
	 */
	public ServiceMetadataAttribute() {
	}

	/**
	 * @return the bind
	 */
	public String getBind() {
		return bind;
	}

	/**
	 * @param bind
	 *            the bind to set
	 */
	private void setBind(String bind) {
		this.bind = bind;
	}

	@Override
	public void setAttribute(String attributeName, String value) {
		if(attributeName.equals(Metadata.ATTRIBUTE_BIND)){
			this.setBind(value);
			return;
		}
		super.setAttribute(attributeName, value);
	}
}
