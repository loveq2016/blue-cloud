package com.yonyouhealth.uaph.framework.comm.pool;

import java.util.Map;

public class PoolManager
{
  private static PoolManager poolManager = null;

  private static final SingletonPool single = new SingletonPool();

  public static PoolManager getInstance()
  {
    if (poolManager == null) {
      poolManager = new PoolManager();
    }
    return poolManager;
  }

  private PoolManager()
  {
    init();
  }

  private void init()
  {
  }

  public void addSingle(String key, Object object)
  {
    single.returnObjectFromChild(key, object, true);
  }

  public void addSingles(Map objects)
  {
    single.returnObjects(objects);
  }

  public Object borrowObject(String key)
  {
    return single.borrowObjectFromChild(key);
  }

  public Object borrowObject(Object key, Class type)
  {
    return null;
  }

  public void returnObject(String key, Object object)
  {
    single.returnObjectFromChild(key, object, true);
  }

  public void refreshObject(String key, Object object)
  {
    single.returnObjectFromChild(key, object, false);
  }

  public boolean containsObject(String key)
  {
    return single.containsObjectFromChild(key);
  }
}