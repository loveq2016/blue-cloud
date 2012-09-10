package com.yonyouhealth.uaph.framework.comm.codecache;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class CacheTable
  implements Serializable
{
  private int size;
  public static final int CACHE_TYPE_MEM = 0;
  public static final int CACHE_TYPE_DB = 1;
  private List cacheData;
  private int version;
  private Calendar lastAccessTime;
  private String tableName;
  private int cacheType;

  public CacheTable()
  {
    this.cacheData = null;
  }

  public int getSize()
  {
    return this.size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public List getCacheData()
  {
    return this.cacheData;
  }

  public void setCacheData(List cacheData) {
    this.cacheData = cacheData;
  }

  public int getVersion() {
    return this.version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public Calendar getLastAccessTime() {
    return this.lastAccessTime;
  }

  public void setLastAccessTime(Calendar lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public int getCacheType() {
    return this.cacheType;
  }

  public void setCacheType(int cacheType) {
    this.cacheType = cacheType;
  }
}