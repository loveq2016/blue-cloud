package com.yonyouhealth.uaph.framework.web.mvc.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyouhealth.uaph.framework.web.mvc.beans.IObjectBean;

public class ResDataObject
  implements IDataObject
{
  private static final long serialVersionUID = -8291329970330280955L;
  private List<Object> jsonDatas = new ArrayList();
  private IObjectBean<?> objectBean;
  private Map<String, Object> jsonStringMap = new HashMap();
  private Object object;
  private boolean customJson = false;

  public boolean isCustomJson()
  {
    return this.customJson;
  }

  public void setCustomJson(boolean paramBoolean)
  {
    this.customJson = paramBoolean;
  }

  public Object getObject()
  {
    return this.object;
  }

  public void setObject(Object paramObject)
  {
    this.object = paramObject;
  }

  public IObjectBean<?> getObjectBean()
  {
    return this.objectBean;
  }

  public void setObjectBean(IObjectBean<?> paramIObjectBean)
  {
    this.objectBean = paramIObjectBean;
  }

  public List<Object> getJsonDatas()
  {
    return this.jsonDatas;
  }

  public void setJsonDatas(List<Object> paramList)
  {
    this.jsonDatas = paramList;
  }

  public Map<String, Object> getJsonStringMap()
  {
    return this.jsonStringMap;
  }

  public void setJsonStringMap(Map<String, Object> paramMap)
  {
    this.jsonStringMap = paramMap;
  }

  public void putToJson(String paramString, Object paramObject)
  {
    this.jsonStringMap.put(paramString, paramObject);
  }

  public void addJsonDatas(Object paramObject)
  {
    this.jsonDatas.add(paramObject);
  }
}