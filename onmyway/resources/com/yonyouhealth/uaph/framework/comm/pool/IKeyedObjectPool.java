package com.yonyouhealth.uaph.framework.comm.pool;

public abstract interface IKeyedObjectPool
{
  public abstract Object borrowObject(String paramString)
    throws PoolException;

  public abstract void returnObject(String paramString, Object paramObject)
    throws PoolException;

  public abstract void returnObject(String paramString, Object paramObject, boolean paramBoolean)
    throws PoolException;

  public abstract void invalidateObject(String paramString, Object paramObject)
    throws PoolException;

  public abstract void addObject(String paramString)
    throws PoolException;

  public abstract int getNumIdle(String paramString)
    throws UnsupportedOperationException;

  public abstract int getNumActive(String paramString)
    throws UnsupportedOperationException;

  public abstract int getNumIdle()
    throws UnsupportedOperationException;

  public abstract int getNumActive()
    throws UnsupportedOperationException;

  public abstract void clear()
    throws PoolException, UnsupportedOperationException;

  public abstract void clear(String paramString)
    throws PoolException, UnsupportedOperationException;

  public abstract void close()
    throws PoolException;

  public abstract void setFactory(KeyedPoolableObjectFactory paramKeyedPoolableObjectFactory)
    throws IllegalStateException, UnsupportedOperationException;
}