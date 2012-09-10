package com.yonyouhealth.uaph.framework.comm.codecache;

import java.util.List;
import java.util.Map;

public abstract interface CacheUserInterface
{
  public abstract List getCacheData(String paramString)
    throws Exception;

  public abstract Map getCacheDataMap(String paramString1, String paramString2)
    throws Exception;

  public abstract List getCacheData(String paramString, List paramList)
    throws Exception;

  public abstract Map getCacheDataMap(String paramString1, List paramList, String paramString2)
    throws Exception;

  public abstract List getCacheDataFromDB(String paramString1, String paramString2)
    throws Exception;

  public abstract Map getCacheDataRow(String paramString, List paramList)
    throws Exception;

  public abstract Object getCacheValueByColname(String paramString1, List paramList, String paramString2)
    throws Exception;

  public abstract String getCacheXtcs(String paramString)
    throws Exception;
}