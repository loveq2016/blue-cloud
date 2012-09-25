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
package com.bluesky.ioc.classloader;

import java.net.URL;

import com.bluesky.ioc.source.KernelSource;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class ClassLoaderFactory {

	/**
	 * 
	 */
	private ClassLoaderFactory() {
	}

	/**
	 * <h3>创建ClassLoader</h3> 根据name创建ClassLoader，该name将会被用在标识来获取相应的ClassLoader
	 * 
	 * @param name
	 *            ClassLoader的名字
	 * @param parent
	 *            父ClassLoader
	 * @param source
	 *            本地jar包文件夹的路径、本地jar包的路径、本地Class的路径</br>
	 *            <ul>
	 *            <li>[dir]</li>
	 *            <li>[dir]\[name].jar</li>
	 *            <li>[dir]\[name].class</li>
	 *            </ul>
	 * @return {@link StandaloneClassLoader}
	 */
	public static ClassLoader createClassLoader(final String name,
			ClassLoader parent, KernelSource source) {
		StandaloneClassLoader classloader = null;
		URL[] urls = (URL[]) source.resource();
		if (parent == null) {
			classloader = new StandaloneClassLoader(urls);
		} else {
			classloader = new StandaloneClassLoader(urls, parent);
		}
		classloader.setName(name);
		return classloader;
	}

}
