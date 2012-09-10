package com.yonyouhealth.uaph.framework.web.mvc.cachecode;

import java.util.List;
import java.util.Map;

public abstract interface ICacheCodeConfig
{
  public static final String tableName = "tableName";
  public static final String codeRowName = "codeRowName";
  public static final String codeName = "codeName";
  public static final String valueName = "valueName";

  public abstract void set();

  public abstract ICacheCodeConfig setMore(String paramString1, String paramString2);

  public abstract List<Map<String, String>> getAllSet();
}