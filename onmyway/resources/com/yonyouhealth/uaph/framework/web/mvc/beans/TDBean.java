package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.Map;

public class TDBean extends AbsDataBean<String>
{
  private static final long serialVersionUID = -3221519776874836709L;
  private String key;
  private String value;
  private String valueType;
  private String code;

  public String getCode()
  {
    return this.code;
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public String getKey()
  {
    return this.key;
  }

  public String getValue()
  {
    return this.value;
  }

  public String getValueType()
  {
    return this.valueType;
  }

  public void setKey(String paramString)
  {
    this.key = paramString;
  }

  public void setValue(String paramString)
  {
    this.value = paramString;
  }

  public void setValueType(String paramString)
  {
    this.valueType = paramString;
  }

  public Map<String, String> viewData()
  {
    return super.getParams();
  }
}