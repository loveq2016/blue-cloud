package com.yonyouhealth.uaph.framework.comm.codecache;

import java.util.Map;

import com.yonyouhealth.uaph.framework.core.event.CSSBaseResponseEvent;


public class CacheResEvent extends CSSBaseResponseEvent
{
  private Map cachedata;

  public Map getCachedata()
  {
    return this.cachedata;
  }

  public void setCachedata(Map cachedata) {
    this.cachedata = cachedata;
  }
}