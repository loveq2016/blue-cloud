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

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class JdkLoggerFormatter extends Formatter {
	// values from JDK Level
	public static final int LOG_LEVEL_TRACE = 400;
	public static final int LOG_LEVEL_DEBUG = 500;
	public static final int LOG_LEVEL_INFO = 800;
	public static final int LOG_LEVEL_WARN = 900;
	public static final int LOG_LEVEL_ERROR = 1000;
	public static final int LOG_LEVEL_FATAL = 1000;

	public String format(LogRecord record) {
		Throwable t = record.getThrown();
		int level = record.getLevel().intValue();
		String name = record.getLoggerName();
		long time = record.getMillis();
		String message = formatMessage(record);

		if (name.indexOf(".") >= 0)
			name = name.substring(name.lastIndexOf(".") + 1);

		// Use a string buffer for better performance
		StringBuffer buf = new StringBuffer();

		buf.append(time);
		buf.append(" ");

		// pad to 8 to make it more readable
		for (int i = 0; i < 8 - buf.length(); i++) {
			buf.append(" ");
		}

		// Append a readable representation of the log level.
		switch (level) {
		case LOG_LEVEL_TRACE:
			buf.append(" T ");
			break;
		case LOG_LEVEL_DEBUG:
			buf.append(" D ");
			break;
		case LOG_LEVEL_INFO:
			buf.append(" I ");
			break;
		case LOG_LEVEL_WARN:
			buf.append(" W ");
			break;
		case LOG_LEVEL_ERROR:
			buf.append(" E ");
			break;
		// case : buf.append(" F "); break;
		default:
			buf.append("   ");
		}

		// Append the name of the log instance if so configured
		buf.append(name);

		// pad to 20 chars
		for (int i = 0; i < 8 - buf.length(); i++) {
			buf.append(" ");
		}

		// Append the message
		buf.append(message);

		// Append stack trace if not null
		if (t != null) {
			buf.append(" \n");

			java.io.StringWriter sw = new java.io.StringWriter(1024);
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			t.printStackTrace(pw);
			pw.close();
			buf.append(sw.toString());
		}

		buf.append("\n");
		// Print to the appropriate destination
		return buf.toString();
	}

}
