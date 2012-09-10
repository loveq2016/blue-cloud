package com.yonyouhealth.uaph.framework.comm.pool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.comm.util.StringUtils;

public class SingletonPool extends AbsBaseKeyedObjectPool implements IHierarchicalPool {
	private static final LogWritter logger = LogFactory.getLogger(SingletonPool.class);

	protected Map cache = Collections.synchronizedMap(new HashMap());

	public SingletonPool() {
	}

	public SingletonPool(String key, IKeyedObjectPool child) {
		this.cache.put(key, child);
	}

	public void invalidateObject(String key, Object object) throws PoolException {
		throw new UnsupportedOperationException();
	}

	public Object borrowObject(String key) {
		return this.cache.get(key);
	}

	public void returnObject(String key, Object object) throws PoolException {
		this.cache.put(key, object);
	}

	public void returnObject(String key, Object obj, boolean check) throws PoolException {
		if ((check) && (this.cache.containsKey(key))) {
			logger.debug("��ǰ������key=" + key + ",�����ٷţ�");
			throw new OccupiedLocationException("50", key);
		}
		returnObject(key, obj);
	}

	public void returnObjects(Map objects) {
		this.cache.putAll(objects);
	}

	public void returnObjectFromChild(String nestkey, Object object, boolean check) {
		IKeyedObjectPool pool = setChildsPool(nestkey);

		String key = StringUtils.unqualify(nestkey, ".");

		if (check)
			pool.returnObject(key, object, true);
		else
			pool.returnObject(key, object);
	}

	public Object borrowObjectFromChild(String nestkey) {
		Object result = null;
		SingletonPool pool = (SingletonPool) getBottomChildPool(nestkey);
		String key = StringUtils.unqualify(nestkey, ".");

		if (pool != null) {
			if ("*".equals(key))
				result = pool.getCache();
			else {
				result = pool.borrowObject(key);
			}
		}
		return result;
	}

	protected IKeyedObjectPool getBottomChildPool(String nestkey) throws NoSuchPoolException {
		IKeyedObjectPool curPool = this;
		IKeyedObjectPool nextPool = null;

		if ((nestkey != null) && (!"".equals(nestkey.trim()))) {
			if (nestkey.indexOf(".") > 0) {
				String[] names = StringUtils.delimitedListToStringArray(nestkey, ".");

				for (int i = 0; i < names.length - 1; ++i) {
					try {
						nextPool = (IKeyedObjectPool) curPool.borrowObject(names[i]);

						if (nextPool == null) {
							return null;
						}
						curPool = nextPool;
					} catch (ClassCastException ex) {
						throw new NoSuchPoolException("51", names[i]);
					}
				}
				// break label114:
				return nextPool;
			}
			return this;
		}

		throw new NoSuchPoolException("52");

		// label114: return nextPool;
	}

	public IKeyedObjectPool setChildsPool(String nestkey) throws PoolException {
		if ((nestkey != null) && (!"".endsWith(nestkey.trim())) && (nestkey.indexOf(".") > 0)) {
			String[] names = StringUtils.delimitedListToStringArray(nestkey, ".");

			IKeyedObjectPool curPool = this;
			IKeyedObjectPool nextPool = null;
			for (int i = 0; i < names.length - 1; ++i) {
				nextPool = (IKeyedObjectPool) curPool.borrowObject(names[i]);
				if (nextPool == null) {
					nextPool = new SingletonPool();
					curPool.returnObject(names[i], nextPool, true);
				}
				curPool = nextPool;
			}
			return nextPool;
		}

		return this;
	}

	public Map getCache() {
		return this.cache;
	}

	public boolean containsObjectFromChild(String nestkey) {
		boolean result = false;
		SingletonPool pool = (SingletonPool) getBottomChildPool(nestkey);
		String key = StringUtils.unqualify(nestkey, ".");

		if (pool == null)
			result = false;
		else {
			result = pool.containsObject(key);
		}
		return result;
	}

	public boolean containsObject(String key) {
		return this.cache.containsKey(key);
	}
}