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
public interface Metadata {
	// element 标记
	public static final String ELEMENT_COMPONENT = "component";
	public static final String ELEMENT_CONSTRUCTOR = "constructor";
	public static final String ELEMENT_SERVICE = "service";
	public static final String ELEMENT_SERVICES = "services";
	public static final String ELEMENT_ENTRY = "entry";
	public static final String ELEMENT_PROPERTY = "property";
	// attribute 标记
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ATTRIBUTE_CLASS = "class";
	public static final String ATTRIBUTE_TYPE = "type";
	public static final String ATTRIBUTE_LIMITED = "limited";
	public static final String ATTRIBUTE_BIND = "bind";
	public static final String ATTRIBUTE_VALUE = "value";
	public static final String ATTRIBUTE_SCOPE = "scope";
	public static final String ATTRIBUTE_INDEX = "index";
	public static final String ATTRIBUTE_SIZE = "size";
}
