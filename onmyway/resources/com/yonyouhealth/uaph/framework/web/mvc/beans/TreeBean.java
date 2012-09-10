package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeBean extends AbsDataBean<Object>
{
  private static final long serialVersionUID = 3284282312709196713L;
  private List<Map<String, Object>> dataList = new ArrayList();

  public Map<String, Object> viewData()
  {
    return this.viewData;
  }

  public void setDataList(List<Map<String, Object>> paramList)
  {
    this.dataList = paramList;
  }

  public List<Map<String, Object>> getDataList()
  {
    return this.dataList;
  }

  public void add(Map<String, Object> paramMap)
  {
    this.dataList.add(paramMap);
  }
}