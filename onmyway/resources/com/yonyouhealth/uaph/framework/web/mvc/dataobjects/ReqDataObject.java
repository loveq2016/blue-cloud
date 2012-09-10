package com.yonyouhealth.uaph.framework.web.mvc.dataobjects;

import java.util.Map;

public class ReqDataObject
  implements IDataObject
{
  private static final long serialVersionUID = -6511763517661863305L;
  private Map<String, Object> viewData;

  public Map<String, Object> getViewData()
  {
    return this.viewData;
  }

  public void setViewData(Map<String, Object> paramMap)
  {
    this.viewData = paramMap;
  }
}