package com.yonyouhealth.uaph.framework.web.mvc.util.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSON
{
  public static JSONObject getJsonObject(Object paramObject)
  {
    return JSONObject.fromObject(paramObject);
  }

  public static JSONArray getJsonArray(Object paramObject)
  {
    JSONArray localJSONArray = JSONArray.fromObject(paramObject);
    return localJSONArray;
  }

  public static Object getJsonObjectValue(Object paramObject1, Object paramObject2)
  {
    return ((JSONObject)paramObject1).get(paramObject2);
  }

  public static Object[] getJSONObjectArray(JSONArray paramJSONArray)
  {
    Object[] arrayOfObject = new Object[paramJSONArray.size()];
    for (int i = 0; i < paramJSONArray.size(); ++i)
      arrayOfObject[i] = paramJSONArray.getJSONObject(i);
    return arrayOfObject;
  }

  public static Map<String, Object> getJSONObjectMap(Object paramObject)
  {
    JSONObject localJSONObject = JSONObject.fromObject(paramObject);
    HashMap localHashMap = new HashMap();
    Iterator localIterator = localJSONObject.keys();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localHashMap.put(str, localJSONObject.get(str));
    }
    return localHashMap;
  }

  public static String mapToJson(Map<String, Object> paramMap)
  {
    JSONObject localJSONObject = JSONObject.fromObject(paramMap);
    return localJSONObject.toString();
  }

  public static String stringMapToJson(Map<String, String> paramMap)
  {
    JSONObject localJSONObject = JSONObject.fromObject(paramMap);
    return localJSONObject.toString();
  }
}