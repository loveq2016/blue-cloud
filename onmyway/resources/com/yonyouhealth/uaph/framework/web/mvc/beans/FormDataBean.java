package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.Map;

public class FormDataBean extends AbsDataBean<String>
{
  private static final long serialVersionUID = 8582878606121078176L;
  private String value;
  private String dataType;
  private String code;

  public String getValue()
  {
    return this.value;
  }

  public String getDataType()
  {
    return this.dataType;
  }

  public void setValue(String paramString)
  {
    this.value = paramString;
  }

  public void setDataType(String paramString)
  {
    this.dataType = paramString;
  }

  public Map<String, String> viewData()
  {
    return super.getParams();
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public String getCode()
  {
    return this.code;
  }
}