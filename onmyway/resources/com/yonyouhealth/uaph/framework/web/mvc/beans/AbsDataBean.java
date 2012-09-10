package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsDataBean<T>
  implements IObjectBean<T>
{
  private static final long serialVersionUID = 4903437484720009789L;
  protected String dataSrc;
  protected String optFlag;
  protected String submitType;
  protected Map<String, String> params = new HashMap();
  protected String paramsJson;
  protected Map<String, T> viewData;

  public Map<String, T> getViewData()
  {
    return this.viewData;
  }

  public void setViewData(Map<String, T> paramMap)
  {
    this.viewData = paramMap;
  }

  public String getParamsJson()
  {
    return this.paramsJson;
  }

  public void setParamsJson(String paramString)
  {
    this.paramsJson = paramString;
  }

  public Map<String, String> getParams()
  {
    return this.params;
  }

  public void setParams(Map<String, String> paramMap)
  {
    this.params = paramMap;
  }

  public String getDataSrc()
  {
    return this.dataSrc;
  }

  public void setDataSrc(String paramString)
  {
    this.dataSrc = paramString;
  }

  public String getOptFlag()
  {
    return this.optFlag;
  }

  public String getSubmitType()
  {
    return this.submitType;
  }

  public void setSubmitType(String paramString)
  {
    this.submitType = paramString;
  }

  public void setOptFlag(String paramString)
  {
    this.optFlag = paramString;
  }

  public void putParam(String paramString1, String paramString2)
  {
    this.params.put(paramString1, paramString2);
  }
}