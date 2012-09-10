package com.yonyouhealth.uaph.framework.core.event;

import java.util.HashMap;
import java.util.Map;

public class CSSBaseResponseEvent
  implements IResponseEvent
{
  private long costTime;
  private Map cache;

  public CSSBaseResponseEvent()
  {
    this.cache = new HashMap();
  }

  public boolean isSuccess()
  {
    return true;
  }

  public boolean isOne()
  {
    return true;
  }

  public void setSuccess()
  {
  }

  public void setCostTime(long costTime) {
    this.costTime = costTime;
  }

  public long getCostTime() {
    return this.costTime;
  }

  public void put(String name, Object object)
  {
    this.cache.put(name, object);
  }

  public Object get(String name) {
    return this.cache.get(name);
  }
}