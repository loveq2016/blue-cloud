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
package com.bluesky.ioc.source;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import com.bluesky.ioc.exception.KernelSourceException;

/**
 * 包含jar包中的<strong>swordesb.xml</strong>的文件URL资源，{@code
 * XMLPathSource.resource()}返回URL[]
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class JarXMLSource extends URLSource {

	/**
	 * @param jarDir
	 * @throws KernelSourceException
	 * 
	 */
	public JarXMLSource(File jarDir) throws KernelSourceException {
		super(new JarFileSource(jarDir));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.source.URLSource#load(java.io.File
	 * [])
	 */
	@Override
	protected void load(File[] files) throws IOException {
		Enumeration<URL> xmlPath = Thread.currentThread()
				.getContextClassLoader().getResources("swordesb.xml");
		Set<URL> xmls = new LinkedHashSet<URL>();
		while (xmlPath.hasMoreElements()) {
			URL url = xmlPath.nextElement();
			xmls.add(url);
		}
		urls = xmls.toArray(new URL[xmls.size()]);
	}

}
