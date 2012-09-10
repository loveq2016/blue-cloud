package com.yonyouhealth.uaph.framework.comm.codecache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import com.yonyouhealth.uaph.framework.comm.conf.ConfManager;
import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;


public class ServerCacheManager
  implements CacheUserInterface, CacheManager
{
  private static final LogWritter logger = LogFactory.getLogger(ServerCacheManager.class);

  private Map codeCachePool = new HashMap();

  private static ServerCacheManager instance = null;

  private boolean useMemoryCache = true;

  private boolean useLocalXMLCache = false;

  private XMLCacheManager xmlCacheManager = null;

  private Thread monitor = null;

  private ServerCacheManager()
  {
    init();
  }

  private void init()
  {
    Properties props = (Properties)ConfManager.getValueByKey("codecache");
    String strUseMemoryCache = props.getProperty("use-memory-cache");
    if ((strUseMemoryCache != null) && (strUseMemoryCache.equalsIgnoreCase("true")))
    {
      this.useMemoryCache = true;
    }
    else this.useMemoryCache = false;

    String strLocalXMLCacheDir = props.getProperty("local-xml-cache-dir");

    if ((strLocalXMLCacheDir != null) && (strLocalXMLCacheDir.trim().length() > 0))
    {
      this.useLocalXMLCache = true;
      this.xmlCacheManager = new XMLCacheManager();
      this.xmlCacheManager.setLocalXMLCacheRoot(strLocalXMLCacheDir.trim());
      this.xmlCacheManager.init();
    } else {
      this.useLocalXMLCache = false;
      this.xmlCacheManager = null;
    }
  }

  public static CacheUserInterface getCacheUserInterface()
  {
    if (instance == null) {
      instance = new ServerCacheManager();
    }
    return instance;
  }

  public static CacheManager getInstance()
  {
    if (instance == null) {
      instance = new ServerCacheManager();
    }

    return instance;
  }

  public Thread getMonitor() {
    return this.monitor;
  }

  public Map getCodeCachePool() {
    return this.codeCachePool;
  }

  public boolean isUseMemoryCache() {
    return this.useMemoryCache;
  }

  public List getCacheData(String tableName)
    throws Exception
  {
    if (!this.useMemoryCache) {
      return getDataFromServer(tableName, null);
    }

    tableName = tableName.toUpperCase();

    CacheTable table = (CacheTable)this.codeCachePool.get(tableName);
    if (table == null) {
      return null;
    }

    List list = null;
    if (table.getCacheType() == 1)
      list = getDataFromServer(tableName, null);
    else {
      list = table.getCacheData();
    }

    if (list == null) {
      list = new ArrayList();
    }

    return list;
  }

  public Map getCacheDataMap(String tableName, String keyFieldName) throws Exception
  {
    List list = getCacheData(tableName);

    Map map = convertList2Map(list, keyFieldName);

    return map;
  }

  public List getCacheData(String tableName, List filters)
    throws Exception
  {
    if (!this.useMemoryCache) {
      return getDataFromServer(tableName, filters);
    }
    tableName = tableName.toUpperCase();

    CacheTable table = (CacheTable)this.codeCachePool.get(tableName);
    if (table == null) {
      return null;
    }

    List list = null;
    if (table.getCacheType() == 1) {
      list = getDataFromServer(tableName, filters);
      return list;
    }

    list = table.getCacheData();
    if (list == null) {
      return null;
    }

    List filterList = new ArrayList();
    List mirror = list;
    for (int i = 0; i < filters.size(); ++i) {
      CacheFilter cf = (CacheFilter)filters.get(i);
      String fieldName = cf.getFieldName();
      Object fieldValue = cf.getFieldValue();
      String filterOperator = cf.getFilterOperator();

      if (fieldValue == null) {
        continue;
      }

      for (int k = 0; k < mirror.size(); ++k) {
        Map map = (Map)mirror.get(k);
        if (filterOperator.equals("=")) {
          Object data = map.get(fieldName.toUpperCase());
          if (fieldValue instanceof String) {
            data = "" + data;
          }
          if (fieldValue.equals(data))
            filterList.add(map);
        }
        else if (filterOperator.equals("<>"))
        {
          Object data = map.get(fieldName.toUpperCase());
          if (fieldValue instanceof String) {
            data = "" + data;
          }
          if (!fieldValue.equals(data))
            filterList.add(map);
        }
        else if (filterOperator.equals(">"))
        {
          Object data = map.get(fieldName.toUpperCase());
          try
          {
            Float floatData = Float.valueOf("" + data);
            Float floatfield = Float.valueOf("" + fieldValue);

            if (floatData.floatValue() > floatfield.floatValue())
              filterList.add(map);
          }
          catch (NumberFormatException e) {
          }
        }
        else if (filterOperator.equals("<"))
        {
          Object data = map.get(fieldName.toUpperCase());
          try {
            Float floatData = Float.valueOf("" + data);
            Float floatfield = Float.valueOf("" + fieldValue);

            if (floatData.floatValue() < floatfield.floatValue())
              filterList.add(map);
          } catch (NumberFormatException e) {
          }
        }
        else if (filterOperator.equals("in"))
        {
          List fieldValueList = (List)fieldValue;
          for (int index = 0; index < fieldValueList.size(); ++index) {
            String s1 = "" + fieldValueList.get(index);
            String s2 = "" + map.get(fieldName.toUpperCase());
            if (s1.equals(s2)) {
              filterList.add(map);
              break;
            }
          }
        } else {
          if (!filterOperator.equals("not in"))
            continue;
          List fieldValueList = (List)fieldValue;
          boolean flag = true;
          for (int index = 0; index < fieldValueList.size(); ++index) {
            String s1 = "" + fieldValueList.get(index);
            String s2 = "" + map.get(fieldName.toUpperCase());
            if (s1.equals(s2)) {
              flag = false;
              break;
            }
          }

          if (flag) {
            filterList.add(map);
          }
        }

      }

      mirror = filterList;
      filterList = new ArrayList();
    }

    filterList = mirror;

    if (filterList == null) {
      filterList = new ArrayList();
    }

    return filterList;
  }

  public Map getCacheDataMap(String tableName, List filters, String keyFieldName) throws Exception
  {
    List list = getCacheData(tableName, filters);

    Map map = convertList2Map(list, keyFieldName);

    return map;
  }

  public List getCacheDataFromDB(String tableName, String filterSQL)
    throws Exception
  {
    List ret = null;
    logger.debug("从服务器端加载单表数据： tableName=" + tableName + " filterSQL=" + filterSQL);

    tableName = tableName.toUpperCase();

    CacheReqEvent req = new CacheReqEvent("cachehandler", "cachehandler-session");

    req.setLoadMethod("loadwithsql");

    req.setTableName(tableName);
    req.setSqlWhere(filterSQL);

    Map data = delegate(req);

    CacheTable ct = (CacheTable)data.get(tableName);
    ret = ct.getCacheData();

    return ret;
  }

  public Map getCacheDataRow(String tableName, List filters)
    throws Exception
  {
    List list = getCacheData(tableName, filters);
    if (list.size() > 0) {
      return (Map)list.get(0);
    }
    return new HashMap();
  }

  public Object getCacheValueByColname(String tableName, List filters, String colName)
    throws Exception
  {
    Map map = getCacheDataRow(tableName, filters);
    if (map.isEmpty()) {
      return null;
    }
    return map.get(colName.toUpperCase());
  }

  public String getCacheXtcs(String csbm)
    throws Exception
  {
    String csbName = "t_xt_xtcs".toUpperCase();
    List list = getCacheData(csbName);

    String value = "";

    for (int i = 0; i < list.size(); ++i) {
      Map row = (Map)list.get(i);
      Iterator iter = row.keySet().iterator();
      while (iter.hasNext()) {
        String key = (String)iter.next();
        if (csbm.equalsIgnoreCase(key)) {
          value = (String)row.get(key);
          return value;
        }
      }
    }

    return value;
  }

  public void load()
    throws Exception
  {
    if (!this.useMemoryCache) {
      return;
    }

    CacheReqEvent req = new CacheReqEvent("cachehandler", "cachehandler-session");

    req.setLoadMethod("loadAll");

    Map data = delegate(req);

    updateLocalCache(data);

    startMonitor();
  }

  public void update()
    throws Exception
  {
    if (!this.useMemoryCache) {
      return;
    }

    logger.debug("============= 开始更新缓存代码表 ================");
    CacheReqEvent req = new CacheReqEvent("cachehandler", "cachehandler-session");

    req.setLoadMethod("update");

    Iterator iter = this.codeCachePool.values().iterator();
    Map cacheElements = new HashMap();
    while (iter.hasNext()) {
      CacheTable table = (CacheTable)iter.next();
      CacheElement ce = new CacheElement(table.getTableName());
      ce.setVersion(table.getVersion());

      cacheElements.put(table.getTableName(), ce);
    }
    req.setCacheElements(cacheElements);

    Map data = delegate(req);

    updateLocalCache(data);
  }

  public List getDataFromServer(String tableName, List filters)
    throws Exception
  {
    List ret = null;
    tableName = tableName.toUpperCase();

    logger.debug("从服务器端加载单表数据： tableName=" + tableName + " filters=" + filters);

    CacheReqEvent req = new CacheReqEvent("cachehandler", "cachehandler-session");

    req.setLoadMethod("loadone");

    Map cacheElements = new HashMap();
    CacheElement ce = new CacheElement(tableName);
    ce.setFilters(filters);
    cacheElements.put(tableName, ce);

    req.setCacheElements(cacheElements);

    Map data = delegate(req);

    CacheTable ct = (CacheTable)data.get(tableName);
    ret = ct.getCacheData();

    if ((!this.useMemoryCache) && (((filters == null) || (filters.size() == 0)))) {
      Map map = new HashMap();
      map.put(tableName, ct);
      updateLocalCache(map);
    }

    return ret;
  }

  private Map delegate(CacheReqEvent req) throws Exception {
    Map data = new HashMap();

//    IResponseEvent resp = BizDelegate.delegate(req);
//    if (resp.isSuccess()) {
//      CacheResEvent res = (CacheResEvent)resp;
//      data = res.getCachedata();
//    }
//    else {
//      AppExceptionResponseEvent exceptionResp = (AppExceptionResponseEvent)resp;
//      logger.error("加载了缓存代码表出席那异常： 异常码 = " + exceptionResp.getExceptionMessage(), exceptionResp.getException());
//    }

    return data;
  }

  public void startMonitor()
  {
    Monitor thread = new Monitor();
    this.monitor = thread;
    thread.setDaemon(true);
    thread.start();
  }

  private Map convertList2Map(List table, String keyFieldName) {
    Map map = new HashMap();
    List keys = new ArrayList();
    keyFieldName = keyFieldName.toUpperCase();
    if (keyFieldName.indexOf(",") < 0) {
      keys.add(keyFieldName);
    } else {
      StringTokenizer st = new StringTokenizer(keyFieldName, ",");
      while (st.hasMoreElements()) {
        keys.add(st.nextElement());
      }
    }

    for (int i = 0; i < table.size(); ++i) {
      Map row = (Map)table.get(i);
      String key = "";
      for (int keyIndex = 0; keyIndex < keys.size(); ++keyIndex) {
        key = key + row.get(keys.get(keyIndex)) + ",";
      }
      if (key.endsWith(",")) {
        key = key.substring(0, key.length() - 1);
      }
      map.put(key, row);
    }

    return map;
  }

  private void updateLocalCache(Map data) throws Exception
  {
    synchronized (this.codeCachePool) {
      this.codeCachePool.putAll(data);
    }

    if (this.useLocalXMLCache) {
      this.xmlCacheManager.updateLocalXMLCache(data);
    }

    Iterator iter = data.values().iterator();
    while (iter.hasNext()) {
      CacheTable table = (CacheTable)iter.next();
      String msg = "更新本地缓存： tableName = " + table.getTableName();
      msg = msg + " version = " + table.getVersion();
      msg = msg + " cacheType= " + ((table.getCacheType() == 0) ? "MEM" : "DB");

      if (table.getCacheType() == 0) {
        msg = msg + " rowCount= " + table.getCacheData().size();
      }

      logger.info(msg);
    }
  }

  public static void main(String[] s)
  {
    try {
      ServerCacheManager scm = (ServerCacheManager)getCacheUserInterface();

      if (scm.isUseMemoryCache())
        getInstance().load();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List reload(String tableName)
    throws Exception
  {
    List ret = null;
    tableName = tableName.toUpperCase();

    logger.debug("从服务器端加载单表数据： tableName=" + tableName);

    CacheReqEvent req = new CacheReqEvent("cachehandler", "cachehandler-session");

    req.setLoadMethod("loadone");

    Map cacheElements = new HashMap();
    CacheElement ce = new CacheElement(tableName);
    cacheElements.put(tableName, ce);

    req.setCacheElements(cacheElements);

    Map data = delegate(req);

    CacheTable ct = (CacheTable)data.get(tableName);
    ret = ct.getCacheData();

    if (this.useMemoryCache) {
      Map map = new HashMap();
      map.put(tableName, ct);
      updateLocalCache(map);
    }

    return ret;
  }

  public Map getCacheTableData(String tableName) throws Exception {
    CacheReqEvent req = new CacheReqEvent("cachehandler", "cachehandler-session");

    req.setLoadMethod("ctd");
    Map ele = new HashMap();
    ele.put("tableName", tableName);
    req.setCacheElements(ele);
    Map data = delegate(req);
    return data;
  }

  class Monitor extends Thread
  {
    Monitor()
    {
    }

    public void run()
    {
      Properties props = (Properties)ConfManager.getValueByKey("codecache");

      String strUpdateCyc = props.getProperty("update-cyc");
      if ((strUpdateCyc == null) || (strUpdateCyc.equals(""))) {
        strUpdateCyc = "5";
      }

      int updateCyc = -1;
      try {
        updateCyc = Integer.parseInt(strUpdateCyc);
      } catch (Exception e1) {
        ServerCacheManager.logger.error("代码表缓存管理同步线程出现异常！", e1);
        return;
      }
      while (true)
      {
        try {
          sleep(updateCyc * 60 * 1000);
        } catch (InterruptedException e) {
          ServerCacheManager.logger.error("代码表缓存管理同步线程出现异常！", e);
        }
        try
        {
          ServerCacheManager.this.update();
        } catch (Exception e) {
          ServerCacheManager.logger.error("代码表缓存管理同步线程更新出现异常！", e);
        }
      }
    }
  }
}