package com.yonyouhealth.uaph.framework.web.mvc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.comm.pool.ThreadLocalManager;
import com.yonyouhealth.uaph.framework.web.mvc.beans.AttrBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.FormBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.FormDataBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.SelectBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TDBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TRBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TableBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TreeBean;
import com.yonyouhealth.uaph.framework.web.mvc.dataobjects.ReqDataObject;
import com.yonyouhealth.uaph.framework.web.mvc.dataobjects.ResDataObject;
import com.yonyouhealth.uaph.framework.web.mvc.util.json.JSON;

public class DataBuilder
{
  protected static final LogWritter log = LogFactory.getLogger(DataBuilder.class);

  public static ReqDataObject build(String paramString, ReqDataObject paramReqDataObject)
  {
    log.debug("DataBuilder:" + paramString);
    HashMap localHashMap = new HashMap();
    JSONObject localJSONObject = JSON.getJsonObject(paramString);
    Iterator localIterator = localJSONObject.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str = localEntry.getKey().toString();
      if (str.equals("data"))
      {
        log.debug("Data Object:" + new StringBuilder().append("{'data':").append(localEntry.getValue()).append("}").toString().replaceAll("\\\"", "\\'"));
        getDataMap(localHashMap, localEntry.getValue());
      }
      buildParams(localHashMap, str, localEntry.getValue());
    }
    localHashMap.put("bizParams", paramString);
    paramReqDataObject.setViewData(localHashMap);
    return paramReqDataObject;
  }

  private static void buildParams(Map<String, Object> paramMap, String paramString, Object paramObject)
  {
    if (((("tid".equals(paramString)) || ("ctrl".equals(paramString)))) && (paramString != null) && (!"".equals(paramString)))
      if (paramObject.toString().indexOf("?") != -1)
      {
        String[] arrayOfString1 = paramObject.toString().split("\\?");
        paramMap.put(paramString, arrayOfString1[0]);
        String[] arrayOfString2 = arrayOfString1[1].toString().split("\\&");
        for (int i = 0; i < arrayOfString2.length; ++i)
        {
          String[] arrayOfString3 = arrayOfString2[i].split("\\=");
          paramMap.put(arrayOfString3[0], (arrayOfString3.length > 1) ? arrayOfString3[1] : "");
        }
      }
      else
      {
        paramMap.put(paramString, paramObject);
      }
    else
      paramMap.put(paramString, paramObject);
  }

  private static void getDataMap(Map<String, Object> paramMap, Object paramObject)
  {
    if ((JSON.getJsonArray(paramObject) == null) || (JSON.getJsonArray(paramObject).equals("")))
      return;
    Object[] arrayOfObject = JSON.getJSONObjectArray(JSON.getJsonArray(paramObject));
    for (int i = 0; i < arrayOfObject.length; ++i)
    {
      String str = JSON.getJsonObjectValue(arrayOfObject[i], "uhweb").toString();
      if (str.equals("attr"))
        fillAttrBean(arrayOfObject[i], paramMap);
      else if (str.equals("UhwebForm"))
        fillFormBean(arrayOfObject[i], paramMap);
      else if (str.equals("UhwebGrid"))
        fillTableBean(arrayOfObject[i], paramMap);
      else if (str.equals("UhwebTree"))
        fillTreeBean(arrayOfObject[i], paramMap);
      else
        throw new RuntimeException("No Correct Data(s)!!Your data's type is:" + str);
    }
  }

  private static void fillTreeBean(Object paramObject, Map<String, Object> paramMap)
  {
    log.debug("DataBuilder.fillTreeBean:" + paramObject.toString());
    TreeBean localTreeBean = new TreeBean();
    HashMap localHashMap1 = new HashMap();
    ArrayList localArrayList = new ArrayList();
    JSONObject localJSONObject1 = (JSONObject)paramObject;
    String str1 = "";
    Iterator localIterator1 = localJSONObject1.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry1 = (Map.Entry)localIterator1.next();
      String str2 = (String)localEntry1.getKey();
      if (str2.equals("data"))
      {
        Object[] arrayOfObject = JSON.getJSONObjectArray(JSON.getJsonArray(localEntry1.getValue()));
        for (int i = 0; i < arrayOfObject.length; ++i)
        {
          JSONObject localJSONObject2 = (JSONObject)arrayOfObject[i];
          HashMap localHashMap2 = new HashMap();
          Iterator localIterator2 = localJSONObject2.entrySet().iterator();
          while (localIterator2.hasNext())
          {
            Map.Entry localEntry2 = (Map.Entry)localIterator2.next();
            localHashMap2.put(localEntry2.getKey().toString(), localEntry2.getValue());
          }
          localArrayList.add(localHashMap2);
        }
      }
      else if (str2.equals("name"))
      {
        str1 = localEntry1.getValue().toString();
      }
      else
      {
        localHashMap1.put(str2, localEntry1.getValue());
      }
    }
    localTreeBean.setViewData(localHashMap1);
    localTreeBean.setDataList(localArrayList);
    paramMap.put(str1, localTreeBean);
  }

  private static void fillAttrBean(Object paramObject, Map<String, Object> paramMap)
  {
    log.debug("DataBuilder.fillAttrBean:" + paramObject.toString());
    AttrBean localAttrBean = new AttrBean();
    HashMap localHashMap = new HashMap();
    String str1 = (String)JSON.getJsonObjectValue(paramObject, "name");
    String str2 = (String)JSON.getJsonObjectValue(paramObject, "value");
    localHashMap.put(str1, str2);
    localAttrBean.setAttrMap(localHashMap);
    paramMap.put(str1, localAttrBean);
  }

  private static void fillTableBean(Object paramObject, Map<String, Object> paramMap)
  {
    log.debug("DataBuilder.fillTableBean:" + paramObject);
    TableBean localTableBean = new TableBean();
    JSONObject localJSONObject = (JSONObject)paramObject;
    String str1 = null;
    Iterator localIterator1 = localJSONObject.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      String str2 = localEntry.getKey().toString();
      if (str2.equalsIgnoreCase("name"))
      {
        str1 = JSON.getJsonObjectValue(paramObject, str2).toString();
      }
      else if (str2.equalsIgnoreCase("trs"))
      {
        Object[] arrayOfObject = JSON.getJSONObjectArray(JSON.getJsonArray(JSON.getJsonObjectValue(paramObject, "trs")));
        ArrayList localArrayList1 = new ArrayList();
        for (int i = 0; i < arrayOfObject.length; ++i)
        {
          TRBean localTRBean = new TRBean();
          ArrayList localArrayList2 = new ArrayList();
          Object localObject1 = JSON.getJsonObjectValue(arrayOfObject[i], "status");
          if (localObject1 == null)
            localObject1 = "";
          String str3 = localObject1.toString();
          localTRBean.setOptFlag(str3);
          Map localMap = JSON.getJSONObjectMap(JSON.getJsonObjectValue(arrayOfObject[i], "tds"));
          Iterator localIterator2 = localMap.keySet().iterator();
          while (localIterator2.hasNext())
          {
            TDBean localTDBean = new TDBean();
            String str4 = (String)localIterator2.next();
            localTDBean.setKey(str4);
            Object localObject2 = JSON.getJsonObjectValue(localMap.get(str4), "value");
            if (localObject2 == null)
              localObject2 = "";
            Object localObject3 = JSON.getJsonObjectValue(localMap.get(str4), "code");
            localTDBean.setCode((localObject3 == null) ? "" : localObject3.toString());
            localTDBean.setValue(localObject2.toString());
            localTDBean.setValueType((String)JSON.getJsonObjectValue(localMap.get(str4), "type"));
            localArrayList2.add(localTDBean);
          }
          localTRBean.setTdList(localArrayList2);
          localArrayList1.add(localTRBean);
        }
        localTableBean.setTrList(localArrayList1);
      }
      else
      {
        localTableBean.putParam(str2, localEntry.getValue().toString());
      }
    }
    paramMap.put(str1, localTableBean);
  }

  private static void fillFormBean(Object paramObject, Map<String, Object> paramMap)
  {
    log.debug("DataBuilder.fillFormBean:" + paramObject);
    FormBean localFormBean = new FormBean();
    String str1 = null;
    Iterator localIterator1 = ((JSONObject)paramObject).entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      if (localEntry.getKey().toString().equals("name"))
      {
        str1 = (String)JSON.getJsonObjectValue(paramObject, "name");
      }
      else if (localEntry.getKey().toString().equalsIgnoreCase("data"))
      {
        Map localMap = JSON.getJSONObjectMap(JSON.getJsonObjectValue(paramObject, "data"));
        HashMap localHashMap = new HashMap();
        Iterator localIterator2 = localMap.keySet().iterator();
        while (localIterator2.hasNext())
        {
          FormDataBean localFormDataBean = new FormDataBean();
          String str2 = (String)localIterator2.next();
          localFormDataBean.setValue("" + JSON.getJsonObjectValue(localMap.get(str2), "value"));
          localFormDataBean.setDataType("" + JSON.getJsonObjectValue(localMap.get(str2), "type"));
          localHashMap.put(str2, localFormDataBean);
        }
        localFormBean.setViewData(localHashMap);
        paramMap.put(str1, localFormBean);
      }
      else
      {
        localFormBean.putParam(localEntry.getKey().toString(), localEntry.getValue().toString());
      }
    }
  }

  public static ResDataObject tableBeanToJson(ResDataObject paramResDataObject)
  {
    log.debug("DataBuilder.tableBeanToJson:" + paramResDataObject);
    TableBean localTableBean = (TableBean)paramResDataObject.getObjectBean();
    HashMap localHashMap1 = new HashMap();
    Object localObject = new ArrayList();
    ArrayList localArrayList = new ArrayList();
    localObject = (List)localTableBean.getViewData().get(localTableBean.getTableName());
    for (int i = 0; i < ((List)localObject).size(); ++i)
    {
      HashMap localHashMap2 = new HashMap();
      HashMap localHashMap3 = new HashMap();
      for (int j = 0; j < ((TRBean)((List)localObject).get(i)).getTdList().size(); ++j)
      {
        String str1 = ((TDBean)((TRBean)((List)localObject).get(i)).getTdList().get(j)).getKey();
        if (str1.equals("ROWNUM_"))
          continue;
        String str2 = ((TDBean)((TRBean)((List)localObject).get(i)).getTdList().get(j)).getValue();
        String str3 = ((TDBean)((TRBean)((List)localObject).get(i)).getTdList().get(j)).getCode();
        HashMap localHashMap4 = new HashMap();
        localHashMap4.put("value", str2);
        if (str3 != null)
          localHashMap4.put("code", str3);
        localHashMap3.put(str1, localHashMap4);
        localHashMap2.put("tds", localHashMap3);
      }
      localHashMap2.put("status", ((TRBean)((List)localObject).get(i)).getOptFlag());
      localArrayList.add(localHashMap2);
    }
    checkPaginationConfig(localHashMap1, localTableBean.getTableName());
    localHashMap1.put("trs", localArrayList);
    localHashMap1.put("uhweb", "UhwebGrid");
    localHashMap1.put("name", localTableBean.getTableName());
    log.debug("Table Data:" + localHashMap1);
    paramResDataObject.addJsonDatas(localHashMap1);
    return (ResDataObject)paramResDataObject;
  }

  private static void checkPaginationConfig(Map<String, Object> paramMap, String paramString)
  {
    if (ThreadLocalManager.get("paginationConfig") == null)
      return;
    Map localMap1 = (Map)ThreadLocalManager.get("paginationConfig");
    Map localMap2 = (Map)localMap1.get(paramString);
    if (localMap2 == null)
      return;
    paramMap.putAll(localMap2);
  }

  public static ResDataObject formBeanToJson(ResDataObject paramResDataObject)
  {
    FormBean localFormBean = (FormBean)paramResDataObject.getObjectBean();
    Map localMap1 = localFormBean.getParams();
    HashMap localHashMap1 = new HashMap();
    String str1 = (String)localMap1.get("name");
    HashMap localHashMap2 = new HashMap();
    Map localMap2 = localFormBean.getViewData();
    Iterator localIterator = localMap2.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str2 = (String)localEntry.getKey();
      Map localMap3 = ((FormDataBean)localEntry.getValue()).getParams();
      String str3 = ((FormDataBean)localEntry.getValue()).getCode();
      if (str3 != null)
        localMap3.put("code", str3);
      localHashMap2.put(str2, localMap3);
    }
    localHashMap1.put("uhweb", "UhwebForm");
    localHashMap1.put("name", str1);
    localHashMap1.put("data", localHashMap2);
    log.debug("Form Data:" + localHashMap1);
    paramResDataObject.addJsonDatas(localHashMap1);
    return paramResDataObject;
  }

  public static ResDataObject attrBeanToJson(ResDataObject paramResDataObject)
  {
    AttrBean localAttrBean = (AttrBean)paramResDataObject.getObjectBean();
    Map localMap = localAttrBean.getAttrMap();
    HashMap localHashMap = new HashMap();
    Iterator localIterator = localMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str1 = (String)localEntry.getKey();
      String str2 = (String)localEntry.getValue();
      localHashMap.put("name", str1);
      localHashMap.put("value", str2);
      localHashMap.put("type", "");
    }
    localMap.put("uhweb", "attr");
    log.debug("Attr Datas:" + localHashMap);
    paramResDataObject.addJsonDatas(localHashMap);
    return paramResDataObject;
  }

  public static ResDataObject selectBeanToJson(ResDataObject paramResDataObject)
  {
    SelectBean localSelectBean = (SelectBean)paramResDataObject.getObjectBean();
    List localList = localSelectBean.getDataList();
    Map localMap = localSelectBean.viewData();
    localMap.put("data", localList);
    log.debug("Select Datas:" + localMap);
    paramResDataObject.addJsonDatas(localMap);
    return paramResDataObject;
  }

  public static ResDataObject treeBeanToJson(ResDataObject paramResDataObject)
  {
    TreeBean localTreeBean = (TreeBean)paramResDataObject.getObjectBean();
    List localList = localTreeBean.getDataList();
    Map localMap = localTreeBean.viewData();
    localMap.put("data", localList);
    log.debug("Tree Datas:" + localMap);
    paramResDataObject.addJsonDatas(localMap);
    return paramResDataObject;
  }

  public static String resJsonData(ResDataObject paramResDataObject)
  {
    paramResDataObject.putToJson("data", paramResDataObject.getJsonDatas());
    String str = JSON.mapToJson(paramResDataObject.getJsonStringMap());
    log.debug("JsonString:" + str);
    return str;
  }

  public static String resCusJsonData(ResDataObject paramResDataObject)
  {
    String str = JSON.getJsonObject(paramResDataObject.getObject()).toString();
    return str;
  }

  public static String getSetterName(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("set");
    String str1 = paramString.substring(0, 1).toUpperCase();
    localStringBuffer.append(str1);
    String str2 = paramString.substring(1);
    localStringBuffer.append(str2);
    return localStringBuffer.toString();
  }

  public static String getGetterName(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("get");
    String str1 = paramString.substring(0, 1).toUpperCase();
    localStringBuffer.append(str1);
    String str2 = paramString.substring(1);
    localStringBuffer.append(str2);
    return localStringBuffer.toString();
  }
}