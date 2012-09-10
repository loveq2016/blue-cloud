package com.yonyouhealth.uaph.framework.comm.pool;

public abstract class AbsBaseKeyedObjectPool
  implements IKeyedObjectPool
{
  public abstract Object borrowObject(String paramString)
    throws PoolException;

  public abstract void returnObject(String paramString, Object paramObject)
    throws PoolException;

  public abstract void invalidateObject(String paramString, Object paramObject)
    throws PoolException;

  public void addObject(String key)
    throws PoolException, UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  public int getNumIdle(String key)
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  public int getNumActive(String key)
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  public int getNumIdle()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  public int getNumActive()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  public void clear()
    throws PoolException, UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  public void clear(String key)
    throws PoolException, UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  public void close()
    throws PoolException
  {
  }

  public void setFactory(KeyedPoolableObjectFactory factory)
    throws IllegalStateException, UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }
}