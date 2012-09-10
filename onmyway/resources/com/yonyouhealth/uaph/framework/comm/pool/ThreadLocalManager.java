package com.yonyouhealth.uaph.framework.comm.pool;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程相关的MAP对象
 */
public class ThreadLocalManager {
	private static ThreadLocal pool = new ThreadLocal();

	public static Object get(String key) {
		Map map = (Map) pool.get();
		if (map == null) {
			return null;
		}
		return map.get(key);
	}

	public static void add(String key, Object value) {
		if (pool.get() == null) {
			pool.set(new HashMap());
		}
		Map map = (Map) pool.get();
		map.put(key, value);
	}

	public static Map getMap() {
		return (Map) pool.get();
	}

	public static void clear() {
		pool.set(null);
	}
}