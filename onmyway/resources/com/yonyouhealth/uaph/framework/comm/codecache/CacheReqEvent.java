package com.yonyouhealth.uaph.framework.comm.codecache;

import java.util.HashMap;
import java.util.Map;

import com.yonyouhealth.uaph.framework.core.event.CSSBaseRequestEvent;


public class CacheReqEvent extends CSSBaseRequestEvent
{
  private Map cacheElements = new HashMap();

  private String tableName = "";
  private String sqlWhere = "";

  private String loadMethod = "";

  public CacheReqEvent(String transactionID, String sessionID)
  {
    super(transactionID, sessionID);
  }

  public Map getCacheElements() {
    return this.cacheElements;
  }

  public void setCacheElements(Map cacheElements) {
    this.cacheElements = cacheElements;
  }

  public String getLoadMethod() {
    return this.loadMethod;
  }

  public void setLoadMethod(String loadMethod) {
    this.loadMethod = loadMethod;
  }

  public String getSqlWhere() {
    return this.sqlWhere;
  }

  public void setSqlWhere(String sqlWhere) {
    this.sqlWhere = sqlWhere;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}