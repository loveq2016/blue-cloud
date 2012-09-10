package com.yonyouhealth.uaph.framework.core.event;

import java.util.HashMap;
import java.util.Map;

public class CSSBaseRequestEvent
  implements IRequestEvent
{
  private String monitorId = null;
  protected String transactionID;
  protected String sessionID;
  private String method;
  private int key = -1;

  public String transaction_flag = "";

  private Map cache = new HashMap();

  public String getMonitorId()
  {
    return this.monitorId;
  }

  public void setMonitorId(String id) {
    this.monitorId = id;
  }

  public void setTransactionID(String transactionID)
  {
    this.transactionID = transactionID;
  }

  public CSSBaseRequestEvent(String transactionID, String sessionID)
  {
    if ((transactionID != null) && (transactionID.indexOf("_") > -1)) {
      this.transactionID = transactionID.substring(0, transactionID.indexOf("_"));

      this.method = transactionID.substring(transactionID.indexOf("_") + 1);
    }
    else {
      this.transactionID = transactionID;
    }
    this.sessionID = sessionID;
  }

  public String getTransactionID() {
    return this.transactionID;
  }

  public String getSessionID() {
    return this.sessionID;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public int getKey() {
    return this.key;
  }

  public String getMethod() {
    return this.method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void put(String name, Object object)
  {
    this.cache.put(name, object);
  }

  public Object get(String name) {
    return this.cache.get(name);
  }
}