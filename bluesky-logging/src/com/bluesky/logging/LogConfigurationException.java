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

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class LogConfigurationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 46027409747115873L;

	public LogConfigurationException() {
		super();
	}

	public LogConfigurationException(String message) {
		super(message);
	}

	public LogConfigurationException(Throwable cause) {
		this(((cause == null) ? null : cause.toString()), cause);
	}

	public LogConfigurationException(String message, Throwable cause) {

		super(message);
		this.cause = cause;

	}

	protected Throwable cause = null;

	public Throwable getCause() {

		return (this.cause);

	}

}
