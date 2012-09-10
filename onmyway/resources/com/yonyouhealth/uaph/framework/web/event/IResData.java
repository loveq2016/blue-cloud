package com.yonyouhealth.uaph.framework.web.event;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import sun.jdbc.rowset.CachedRowSet;

import com.yonyouhealth.uaph.framework.core.event.IResponseEvent;
import com.yonyouhealth.uaph.framework.web.mvc.cachecode.ICacheCodeConfig;

public abstract interface IResData extends IResponseEvent
{
  public abstract void addTable(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addTable(String paramString, CachedRowSet paramCachedRowSet, ICacheCodeConfig paramICacheCodeConfig);

  public abstract void addTable(String paramString, CachedRowSet paramCachedRowSet, String[] paramArrayOfString);

  public abstract void addTable(String paramString, List<?> paramList, String[] paramArrayOfString);

  public abstract void addTable(String paramString, List<?> paramList);

  public abstract void addTableMap(String paramString, List<Map<String, Object>> paramList);

  public abstract void addForm(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addForm(String paramString, Map<String, String> paramMap);

  public abstract void addForm(String paramString, Object paramObject);

  public abstract void addForm(String paramString, CachedRowSet paramCachedRowSet, ICacheCodeConfig paramICacheCodeConfig);

  public abstract void addAttr(String paramString, Object paramObject);

  public abstract void addPage(String paramString);

  public abstract void addSelectWithWidgetName(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addSelectWithWidgetName(String paramString, Map<String, String> paramMap);

  public abstract void addSelectWithWidgetName(String paramString, List<Map<String, Object>> paramList, String[] paramArrayOfString);

  public abstract void addSelectWithDataName(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addSelectWithDataName(String paramString, Map<String, String> paramMap);

  public abstract void addSelectWithDataName(String paramString, List<Map<String, Object>> paramList, String[] paramArrayOfString);

  public abstract void addMultiSelectWithName(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addMultiSelectWithDataName(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addTree(String paramString, CachedRowSet paramCachedRowSet, boolean paramBoolean);

  public abstract void addTree(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addTree(String paramString, List<Map<String, Object>> paramList);

  public abstract String getJson();

  public abstract void addMessage(Object paramObject);

  public abstract void setCustomRes(boolean paramBoolean);

  public abstract void addValidator(boolean paramBoolean, String paramString);

  public abstract void addValidator(boolean paramBoolean);

  public abstract IResData joinRes(IResData paramIResData);

  public abstract void setCusJson(boolean paramBoolean);

  public abstract void addJSTL(String paramString, Object paramObject);

  public abstract void addRadioWithWidgetName(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addRadioWithWidgetName(String paramString, Map<String, String> paramMap);

  public abstract void addRadioWithWidgetName(String paramString, Object paramObject);

  public abstract void addCheckBoxWithWidgetName(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addCheckBoxWithWidgetName(String paramString, Map<String, String> paramMap);

  public abstract void addCheckBoxWithWidgetName(String paramString, Object paramObject);

  public abstract void addListWithWidgetName(String paramString, CachedRowSet paramCachedRowSet);

  public abstract void addListWithWidgetName(String paramString, Map<String, String> paramMap);

  public abstract void addListWithWidgetName(String paramString, Object paramObject);

  public abstract void addChart(String paramString1, String paramString2);

  public abstract Locale getLocale();

  public abstract void put(String paramString, Object paramObject);

  public abstract Object get(String paramString);
}