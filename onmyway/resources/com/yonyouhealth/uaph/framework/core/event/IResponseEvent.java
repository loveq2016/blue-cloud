package com.yonyouhealth.uaph.framework.core.event;

import java.io.Serializable;

public abstract interface IResponseEvent extends Cloneable, Serializable
{
  public abstract boolean isOne();

  public abstract void setCostTime(long paramLong);

  public abstract long getCostTime();

  public abstract boolean isSuccess();
}