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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class BlueSkyLog implements Log {
	private Logger logger;

	public BlueSkyLog(String name) {
		logger = Logger.getLogger(name);
	}

	public final boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	public final boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	public final boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
	}

	public final boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	public final boolean isFatalEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	public final boolean isTraceEnabled() {
		return logger.isLoggable(Level.FINER);
	}

	public final void debug(Object message) {
		log(Level.FINE, String.valueOf(message), null);
	}

	public final void debug(Object message, Throwable t) {
		log(Level.FINE, String.valueOf(message), t);
	}

	public final void trace(Object message) {
		log(Level.FINER, String.valueOf(message), null);
	}

	public final void trace(Object message, Throwable t) {
		log(Level.FINER, String.valueOf(message), t);
	}

	public final void info(Object message) {
		log(Level.INFO, String.valueOf(message), null);
	}

	public final void info(Object message, Throwable t) {
		log(Level.INFO, String.valueOf(message), t);
	}

	public final void warn(Object message) {
		log(Level.WARNING, String.valueOf(message), null);
	}

	public final void warn(Object message, Throwable t) {
		log(Level.WARNING, String.valueOf(message), t);
	}

	public final void error(Object message) {
		log(Level.SEVERE, String.valueOf(message), null);
	}

	public final void error(Object message, Throwable t) {
		log(Level.SEVERE, String.valueOf(message), t);
	}

	public final void fatal(Object message) {
		log(Level.SEVERE, String.valueOf(message), null);
	}

	public final void fatal(Object message, Throwable t) {
		log(Level.SEVERE, String.valueOf(message), t);
	}

	private void log(Level level, String msg, Throwable ex) {
		if (logger.isLoggable(level)) {
			Throwable dummyException = new Throwable();
			StackTraceElement locations[] = dummyException.getStackTrace();
			String cname = "unknown";
			String method = "unknown";
			if (locations != null && locations.length > 2) {
				StackTraceElement caller = locations[2];
				cname = caller.getClassName();
				method = caller.getMethodName();
			}
			if (ex == null) {
				logger.logp(level, cname, method, msg);
			} else {
				logger.logp(level, cname, method, msg, ex);
			}
		}
	}

	static void release() {

	}

	static Log getInstance(String name) {
		return new BlueSkyLog(name);
	}
}
