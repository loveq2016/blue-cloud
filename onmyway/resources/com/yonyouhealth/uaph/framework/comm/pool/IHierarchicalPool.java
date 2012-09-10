package com.yonyouhealth.uaph.framework.comm.pool;

public abstract interface IHierarchicalPool
{
  public static final String NESTED_SEPARATOR = ".";

  public abstract void returnObjectFromChild(String paramString, Object paramObject, boolean paramBoolean)
    throws PoolException;

  public abstract Object borrowObjectFromChild(String paramString)
    throws PoolException;
}