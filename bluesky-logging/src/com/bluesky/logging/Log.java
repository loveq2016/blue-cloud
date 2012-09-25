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
public interface Log {

	/**
	 * <h3>检查日志系统是否开启Debug级别</h3>
	 * 
	 * @return 如果日志系统设置Debug级别，返回false，否则返回true
	 */
	public boolean isDebugEnabled();

	/**
	 * <h3>检查日志系统是否设置Error级别</h3>
	 * 
	 * @return 如果日志系统设置Error级别，返回false，否则返回true
	 */
	public boolean isErrorEnabled();

	/**
	 * <h3>检查日志系统是否设置Fatal级别</h3>
	 * 
	 * @return 如果日志系统设置Fatal级别，返回false，否则返回true
	 */
	public boolean isFatalEnabled();

	/**
	 * <h3>检查日志系统是否设置Info级别</h3>
	 * 
	 * @return 如果日志系统设置Info级别，返回false，否则返回true
	 */
	public boolean isInfoEnabled();

	/**
	 * <h3>检查日志系统是否设置Trace级别</h3>
	 * 
	 * @return 如果日志系统设置Trace级别，返回false，否则返回true
	 */
	public boolean isTraceEnabled();

	/**
	 * <h3>检查日志系统是否设置Warn级别</h3>
	 * 
	 * @return 如果日志系统设置Warn级别，返回false，否则返回true
	 */
	public boolean isWarnEnabled();

	/**
	 * <h3>日志系统执行trace操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 */
	public void trace(Object message);

	/**
	 * <h3>日志系统执行trace操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 * @param t
	 *            异常信息
	 */
	public void trace(Object message, Throwable t);

	/**
	 * <h3>日志系统执行debug操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 */
	public void debug(Object message);

	/**
	 * <h3>日志系统执行debug操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 * @param t
	 *            异常信息
	 */
	public void debug(Object message, Throwable t);

	/**
	 * <h3>日志系统执行info操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 */
	public void info(Object message);

	/**
	 * <h3>日志系统执行info操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 * @param t
	 *            异常信息
	 */
	public void info(Object message, Throwable t);

	/**
	 * <h3>日志系统执行warn操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 */
	public void warn(Object message);

	/**
	 * <h3>日志系统执行warn操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 * @param t
	 *            异常信息
	 */
	public void warn(Object message, Throwable t);

	/**
	 * <h3>日志系统执行error操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 */
	public void error(Object message);

	/**
	 * <h3>日志系统执行error操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 * @param t
	 *            异常信息
	 */
	public void error(Object message, Throwable t);

	/**
	 * <h3>日志系统执行fatal操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 */
	public void fatal(Object message);

	/**
	 * <h3>日志系统执行fatal操作</h3>
	 * 
	 * @param message
	 *            日志信息
	 * @param t
	 *            异常信息
	 */
	public void fatal(Object message, Throwable t);

}
