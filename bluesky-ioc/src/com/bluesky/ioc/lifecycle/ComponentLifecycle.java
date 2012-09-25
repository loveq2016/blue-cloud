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
package com.bluesky.ioc.lifecycle;

import com.bluesky.ioc.core.ComponentObject;
import com.bluesky.ioc.exception.KernelComponentLifecycleException;

/**
 * Component的生命周期
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public interface ComponentLifecycle {

	/**
	 * <h3>执行Component的生命周期</h3>
	 * <ul>
	 * <li><strong>Ready</strong>生命周期，</li>
	 * <li><strong>NotReady</strong>生命周期，</li>
	 * </ul>
	 * 
	 * @param component
	 *            Component对象
	 * @param lifecycle
	 *            生命周期
	 * @throws KernelComponentLifecycleException
	 */
	public void invoke(ComponentObject component, ComponentLifecycle lifecycle)
			throws KernelComponentLifecycleException;
}
