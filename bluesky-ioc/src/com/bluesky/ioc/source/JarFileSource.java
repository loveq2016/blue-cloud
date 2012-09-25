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
import java.io.FilenameFilter;

import com.bluesky.ioc.exception.KernelSourceException;

/**
 * 包含jar包的文件资源，{@code JarFileSource.resource()}返回File[]
 * 
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class JarFileSource extends FileSource {

	/**
	 * @param jarDir
	 *            jar包文件夹路径
	 * @throws KernelSourceException
	 *             如果该文件夹不存在或者不可读，抛出异常
	 * 
	 */
	public JarFileSource(File jarDir) throws KernelSourceException {
		super(jarDir);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.source.FileSource#load(java.io.File)
	 */
	@Override
	protected void load(File finalLibDir) {
		files = finalLibDir.listFiles(new FilenameFilter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.io.FilenameFilter#accept(java.io.File,
			 * java.lang.String)
			 */
			public boolean accept(File dir, String name) {
				int index = name.lastIndexOf(".");
				if (index < 0) {
					return false;
				}
				String suffix = name.substring(index).toLowerCase();
				return suffix.equals(".jar");
			}
		});
	}

}
