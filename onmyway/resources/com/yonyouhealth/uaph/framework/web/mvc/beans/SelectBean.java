package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectBean extends AbsDataBean<Object>
{
  private static final long serialVersionUID = -4103983252103569876L;
  private List<Map<String, ?>> dataList = new ArrayList();

  public Map<String, Object> viewData()
  {
    return this.viewData;
  }

  public void setDataList(List<Map<String, ?>> paramList)
  {
    this.dataList = paramList;
  }

  public List<Map<String, ?>> getDataList()
  {
    return this.dataList;
  }

  public void add(Map<String, ?> paramMap)
  {
    this.dataList.add(paramMap);
  }
}