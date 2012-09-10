package com.yonyouhealth.uaph.framework.core.event;

import java.io.Serializable;

public abstract interface IRequestEvent extends Cloneable, Serializable
{
  public abstract String getTransactionID();

  public abstract String getSessionID();

  public abstract void setKey(int paramInt);

  public abstract int getKey();

  public abstract String getMethod();

  public abstract void setMethod(String paramString);

  public abstract String getMonitorId();

  public abstract void setMonitorId(String paramString);
}