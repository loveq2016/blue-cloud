/*
 * <p>Copyright: Copyright (c) 2009 中国软件与技术服务股份有限公司</p>
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

package com.bluesky.logging;

import java.util.Properties;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class LogFactory {

	public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";

	public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";

	public static final String FACTORY_PROPERTIES = "commons-logging.properties";

	public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";

	private static LogFactory singleton = new LogFactory();

	Properties logConfig;

	private LogFactory() {
		logConfig = new Properties();
	}

	void setLogConfig(Properties p) {
		this.logConfig = p;
	}

	public Log getInstance(String name) throws LogConfigurationException {
		return BlueSkyLog.getInstance(name);
	}

	public void release() {
		BlueSkyLog.release();
	}

	public Object getAttribute(String name) {
		return logConfig.get(name);
	}

	public String[] getAttributeNames() {
		String result[] = new String[logConfig.size()];
		return logConfig.keySet().toArray(result);
	}

	public void removeAttribute(String name) {
		logConfig.remove(name);
	}

	public void setAttribute(String name, Object value) {
		logConfig.put(name, value);
	}

	public Log getInstance(Class<? extends Object> clazz)
			throws LogConfigurationException {
		return getInstance(clazz.getName());
	}

	public static LogFactory getFactory() throws LogConfigurationException {
		return singleton;
	}

	public static Log getLog(Class<? extends Object> clazz)
			throws LogConfigurationException {
		return (getFactory().getInstance(clazz));

	}

	public static Log getLog(String name) throws LogConfigurationException {
		return (getFactory().getInstance(name));

	}

	public static void release(ClassLoader classLoader) {
	}

	public static void releaseAll() {
		singleton.release();
	}

	public static String objectId(Object o) {
		if (o == null) {
			return "null";
		} else {
			return o.getClass().getName() + "@" + System.identityHashCode(o);
		}
	}
}
