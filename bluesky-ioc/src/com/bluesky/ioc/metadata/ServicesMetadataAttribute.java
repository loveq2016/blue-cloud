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
public class ServicesMetadataAttribute extends BasicAttribute {
	// 标识列表的类型，如List?Map?Set?
	private String type;
	// 标识列表的限定类型，如List<String>?Map<String,Object>。如果不限定类型，默认使用Object
	private String limited;

	private int size=1;
	
	private final String[] types = { "List", "Set", "Map", "Array" };

	/**
	 * 
	 */
	public ServicesMetadataAttribute() {
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	private void setType(String type) {
		if (type != null && !type.trim().equals("")) {
			for (String t : types) {
				if (t.equals(type)) {
					this.type = t;
					return;
				}
			}
		}
		this.type = types[0];
	}

	/**
	 * @return the limited
	 */
	public String getLimited() {
		return limited;
	}

	/**
	 * @param limited
	 *            the limited to set
	 */
	private void setLimited(String limited) {
		this.limited = limited;
	}

	
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	private void setSize(String size) {
		if(size==null||size.trim().equals("")){
			return;
		}
		this.size = Integer.parseInt(size);
	}

	@Override
	public void setAttribute(String attributeName, String value) {
		if (attributeName.equals(Metadata.ATTRIBUTE_TYPE)) {
			this.setType(value);
			return;
		}
		if (attributeName.equals(Metadata.ATTRIBUTE_LIMITED)) {
			this.setLimited(value);
			return;
		}
		if (attributeName.equals(Metadata.ATTRIBUTE_SIZE)) {
			this.setSize(value);
			return;
		}
		super.setAttribute(attributeName, value);
	}
}
