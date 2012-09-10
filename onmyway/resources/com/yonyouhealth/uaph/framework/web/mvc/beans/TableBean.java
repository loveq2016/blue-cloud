package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableBean extends AbsDataBean<List<TRBean>>
{
  private static final long serialVersionUID = 513193831641839761L;
  private List<TRBean> trList = new ArrayList();
  private String tableName;

  public String getTableName()
  {
    return this.tableName;
  }

  public void setTableName(String paramString)
  {
    this.tableName = paramString;
  }

  public List<TRBean> getTrList()
  {
    return this.trList;
  }

  public void setTrList(List<TRBean> paramList)
  {
    this.trList = paramList;
  }

  public Map<String, List<TRBean>> viewData()
  {
    return this.viewData;
  }
}