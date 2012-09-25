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

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class CompositeMetadata {

	private Map<String,ComponentMetadata> componentMetadatas;

	/**
	 * 
	 */
	public CompositeMetadata() {
		componentMetadatas = new HashMap<String,ComponentMetadata>();
	}

	/**
	 * @return the componentMetadatas
	 */
	public Map<String,ComponentMetadata> getComponentMetadatas() {
		return componentMetadatas;
	}

	/**
	 * 
	 * @param compoentMetadata
	 */
	public void addComponentMetadata(ComponentMetadata compoentMetadata) {
		this.componentMetadatas.put(compoentMetadata.getName(),compoentMetadata);
	}

}
