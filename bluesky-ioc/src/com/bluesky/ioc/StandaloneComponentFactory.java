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
package com.bluesky.ioc;

import com.bluesky.ioc.core.ComponentContext;
import com.bluesky.ioc.exception.KernelException;
import com.bluesky.ioc.source.KernelSource;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class StandaloneComponentFactory implements ComponentFactory {

	private ComponentContext context;

	/**
	 * <h3>根据xml的资源，构建Component工厂</h3>
	 * 
	 * @param source
	 * @throws KernelException
	 */
	public StandaloneComponentFactory(final KernelSource source)
			throws KernelException {
		this(MicroKernel.getInstance(), source);
	}

	/**
	 * 
	 * @param kernel
	 * @param source
	 * @throws KernelException
	 */
	private StandaloneComponentFactory(MicroKernel kernel, KernelSource source)
			throws KernelException {
		if (kernel.getComponentContext() == null) {
			context = kernel.load(source);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.ComponentFactory#getComponent(java
	 * .lang.String)
	 */
	public Object getComponent(String name) {
		if (name == null || name.trim().equals("")) {
			return null;
		}
		return context.getComponent(name);
	}

}
