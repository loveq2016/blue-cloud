package com.yonyouhealth.uaph.framework.comm.filetemplet;

public abstract interface IRuler
{
  public static final String KEY_FLAG_START = "{";
  public static final String KEY_FLAG_END = "}";

  public abstract Object make(ITempFile paramITempFile);

  public abstract void setParams(Object paramObject);
}