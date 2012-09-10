package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.Map;

public class FormBean extends AbsDataBean<FormDataBean>
{
  private static final long serialVersionUID = 7124104318907295747L;

  public Map<String, FormDataBean> viewData()
  {
    return this.viewData;
  }
}