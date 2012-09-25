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

import com.bluesky.ioc.exception.KernelSourceException;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public abstract class FileSource implements KernelSource {
	protected File[] files = null;

	/**
	 * @param dir
	 *            jar包文件夹路径
	 * @throws KernelSourceException
	 *             如果该文件夹不存在或者不可读，抛出异常
	 * 
	 */
	public FileSource(File dir) throws KernelSourceException {
		try {
			File finalLibDir = dir.getCanonicalFile();
			if (!finalLibDir.exists()) {
				throw new KernelSourceException(finalLibDir.toString() + "不存在");
			}
			if (!finalLibDir.canRead()) {
				throw new KernelSourceException(finalLibDir.toString() + "不可读");
			}
			load(finalLibDir);
		} catch (IOException e) {
			throw new KernelSourceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.css.sword.esb.server.microkernel.source.KernelSource#resource()
	 */
	public Object resource() {
		return files;
	}

	/**
	 * 
	 * @param finalLibDir
	 */
	protected abstract void load(File finalLibDir);
}
