package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.io.Serializable;
import java.util.Map;

public abstract interface IObjectBean<T> extends Serializable
{
  public abstract Map<String, T> viewData();
}