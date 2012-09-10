package com.yonyouhealth.uaph.framework.comm.pool;

public abstract interface KeyedPoolableObjectFactory
{
  public abstract Object makeObject(Object paramObject)
    throws Exception;

  public abstract void destroyObject(Object paramObject1, Object paramObject2)
    throws Exception;

  public abstract boolean validateObject(Object paramObject1, Object paramObject2);

  public abstract void activateObject(Object paramObject1, Object paramObject2)
    throws Exception;

  public abstract void passivateObject(Object paramObject1, Object paramObject2)
    throws Exception;
}