package com.yonyouhealth.uaph.framework.comm.codecache;

import java.io.Serializable;
import java.util.List;

public class CacheElement
  implements Serializable
{
  private String tableName = "";
  private int version;
  private List filters;

  public CacheElement(String tableName)
  {
    this.tableName = tableName;
  }

  public String getTableName() {
    return this.tableName;
  }

  public int getVersion() {
    return this.version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public List getFilters() {
    return this.filters;
  }

  public void setFilters(List filters) {
    this.filters = filters;
  }
}