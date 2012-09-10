package com.yonyouhealth.uaph.framework.comm.codecache;

import java.util.List;
import java.util.Map;

public abstract interface CacheManager
{
  public static final String SERVER_METHOD_LOADALL = "loadAll";
  public static final String SERVER_METHOD_LOADONE = "loadone";
  public static final String SERVER_METHOD_LOADWITHSQL = "loadwithsql";
  public static final String SERVER_METHOD_UPDATE = "update";

  public abstract void load()
    throws Exception;

  public abstract void update()
    throws Exception;

  public abstract List getDataFromServer(String paramString, List paramList)
    throws Exception;

  public abstract void startMonitor();

  public abstract Map getCacheTableData(String paramString)
    throws Exception;
}