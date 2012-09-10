package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.Map;

public class AttrBean extends AbsDataBean<String>
{
  private static final long serialVersionUID = -2219248403645710221L;
  private Map<String, String> attrMap;

  public Map<String, String> getAttrMap()
  {
    return this.attrMap;
  }

  public void setAttrMap(Map<String, String> paramMap)
  {
    this.attrMap = paramMap;
  }

  public Map<String, String> viewData()
  {
    return super.getParams();
  }
}