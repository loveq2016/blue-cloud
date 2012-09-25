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
import java.util.LinkedHashSet;
import java.util.Set;

import com.bluesky.ioc.exception.KernelSourceException;

/**
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public abstract class URLSource implements KernelSource {
	protected URL[] urls;

	/**
	 * 
	 * @param source
	 * @throws KernelSourceException
	 */
	public URLSource(FileSource source) throws KernelSourceException {
		File[] files = source.files;
		try {
			load(files);
		} catch (IOException e) {
			throw new KernelSourceException(e);
		}
	}

	/**
	 * 
	 * @param files
	 * @throws IOException
	 */
	protected void load(File[] files) throws IOException {
		Set<URL> xmlURLs = new LinkedHashSet<URL>();
		for (File xmlURL : files) {
			if (xmlURL.canRead()) {
				xmlURLs.add(xmlURL.toURL());
			}
		}
		urls = xmlURLs.toArray(new URL[xmlURLs.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.css.sword.esb.server.microkernel.source.KernelSource#resource()
	 */
	public Object resource() {
		return urls;
	}
}
