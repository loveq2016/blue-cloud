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
public class PropertyMetadataAttribute implements Attribute {
	private String value;

	private Class<?> type;

	private final Map<String, Class<?>> types = new HashMap<String, Class<?>>();

	/**
	 * 
	 */
	public PropertyMetadataAttribute() {
		types.put("String", String.class);
		types.put("Boolean", Boolean.class);
		types.put("Byte", Byte.class);
		types.put("Character", Character.class);
		types.put("Short", Short.class);
		types.put("Integer", Integer.class);
		types.put("Long", Long.class);
		types.put("Double", Double.class);
		types.put("Float", Float.class);
		types.put("boolean", boolean.class);
		types.put("byte", byte.class);
		types.put("char", char.class);
		types.put("short", short.class);
		types.put("int", int.class);
		types.put("long", long.class);
		types.put("double", double.class);
		types.put("float", float.class);
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		if(type==null){
			return new String(value);
		}
		String simpleName=this.type.getSimpleName();
		if(simpleName.equals("String")){
			return new String(value);
		}
		if(simpleName.equals("Boolean")){
			return new Boolean(value);
		}
		if(simpleName.equals("Byte")){
			return new Byte(value);
		}
		if(simpleName.equals("Character")){
			return new Character(value.toCharArray()[0]);
		}
		if(simpleName.equals("Short")){
			return new Short(value);
		}
		if(simpleName.equals("Integer")){
			return new Integer(value);
		}
		if(simpleName.equals("Long")){
			return new Long(value);
		}
		if(simpleName.equals("Double")){
			return new Double(value);
		}
		if(simpleName.equals("Float")){
			return new Float(value);
		}
		if(simpleName.equals("int")){
			return Integer.parseInt(value);
		}
		if(simpleName.equals("double")){
			return Double.parseDouble(value);
		}
		if(simpleName.equals("float")){
			return Float.parseFloat(value);
		}
		if(simpleName.equals("short")){
			return Short.parseShort(value);
		}
		if(simpleName.equals("char")){
			return value.toCharArray()[0];
		}
		if(simpleName.equals("byte")){
			return Byte.parseByte(value);
		}
		if(simpleName.equals("boolean")){
			return Boolean.parseBoolean(value);
		}
		if(simpleName.equals("long")){
			return Long.parseLong(value);
		}
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	private void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	private void setType(String type) {
		this.type = types.get(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.metadata.Attribute#setAttribute(
	 * java.lang.String, java.lang.String)
	 */
	public void setAttribute(String attributeName, String value) {
		if (attributeName.equals(Metadata.ATTRIBUTE_VALUE)) {
			this.setValue(value);
			return;
		}
		if (attributeName.equals(Metadata.ATTRIBUTE_TYPE)) {
			this.setType(value);
			return;
		}
	}
}
