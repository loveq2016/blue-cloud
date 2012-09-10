package com.yonyouhealth.uaph.framework.persistence.pagination;

import java.util.HashMap;
import java.util.Map;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.comm.pool.ThreadLocalManager;

public class PaginationManager
{
  public static final String TOTAL_NUMBER = "rows";
  public static final String PAGE_NUMBER = "pageNum";
  public static final String QUERY_FLAG = "queryType";
  public static final String QUERY_FIRST = "first";
  public static final String QUERY_PAGE = "page";
  public static final String QUERY_SORT = "sort";
  public static final String TOTAL_RECORDER = "totalRows";
  public static final String BIZ_PARAMS = "bizParams";
  public static final String SORT_FLAG = "sortFlag";
  public static final String SORT_NAME = "sortName";
  public static final String PAGINATION_CONFIG = "paginationConfig";
  public static final String WIDGET_NAME = "widgetname";
  protected static final LogWritter log = LogFactory.getLogger(PaginationManager.class);

  public static void load(String widgetName)
  {
    log.debug("��ȡ��ҳ��Ϣ�������ƣ�" + widgetName);

    Map rdoData = getRdoDatas();

    createTicket();

    createWidgetConfig(widgetName);

    if (isPageQuery(rdoData)) {
      initPageInfo(widgetName, rdoData);
      if (isSortQuery(rdoData))
        initSortQuery(widgetName, rdoData);
    }
    else {
      setDefault(widgetName, rdoData);
    }

    setCommonConfig(widgetName, rdoData);
  }

  private static Map<String, Object> getRdoDatas() {
    log.debug("��ȡ�ύ��ݡ�");
    IPaginationReqEvent rsds = (IPaginationReqEvent)ThreadLocalManager.get("paginationParam");

    return rsds.getPaginationParams();
  }

  private static void createTicket() {
    Map ticket = new HashMap();
    if (ThreadLocalManager.get("paginationConfig") == null) {
      log.debug("������ҳ������Ϣ��");
      ThreadLocalManager.add("paginationConfig", ticket);
    }
  }

  private static void createWidgetConfig(String widgetName) {
    Map ticketMap = getTicket();
    Map configMap = getConfig(widgetName);
    if (configMap == null) {
      log.debug("������ҳ���������ƣ�" + widgetName);
      configMap = new HashMap();
      ticketMap.put(widgetName, configMap);
    }
  }

  private static boolean isSortQuery(Map<String, Object> rdoData) {
    Object paginationFlag = rdoData.get("sortName");

    return paginationFlag != null;
  }

  private static boolean isPageQuery(Map<String, Object> rdo)
  {
    Object paginationFlag = rdo.get("queryType");

    return (paginationFlag != null) && (paginationFlag.equals("page"));
  }

  private static void setDefault(String widgetName, Map<String, Object> rdo)
  {
    log.debug("ʹ��Ĭ�Ϸ�ҳ���������ƣ�" + widgetName);
    Map configMap = getConfig(widgetName);
    if (configMap.get("pageNum") == null) {
      configMap.put("pageNum", Integer.valueOf(1));
    }
    if (configMap.get("rows") == null)
      configMap.put("rows", Integer.valueOf(20));
  }

  private static void initPageInfo(String widgetName, Map<String, Object> rdo)
  {
    log.debug("���ύ������л�ȡ��ҳ�����ʼ����ҳ��Ϣ�������ƣ�" + widgetName);
    Map configMap = getConfig(widgetName);
    Object pageNum = rdo.get("pageNum");
    Object totalNum = rdo.get("rows");
    if ((pageNum == null) || (totalNum == null)) {
      throw new RuntimeException("��ȡ��ҳ��Ϣʱʧ�ܣ������ҳ��Ϣ�Ƿ���ȷ�������ƣ�" + widgetName);
    }

    configMap.put("pageNum", pageNum);
    configMap.put("rows", totalNum);
  }

  private static void initSortQuery(String widgetName, Map<String, Object> rdoData)
  {
    Map configMap = getConfig(widgetName);

    if (!widgetName.equals(rdoData.get("widgetname"))) {
      return;
    }
    log.debug("��ʼ�����������Ϣ�������ƣ�" + widgetName);
    Object sortFlag = rdoData.get("sortFlag");
    Object sortName = rdoData.get("sortName");
    if ((sortFlag.toString() != null) && (!sortFlag.toString().equals("null"))) {
      configMap.put("sortFlag", sortFlag);
    }
    if ((sortName.toString() != null) && (!sortName.toString().equals("null")))
      configMap.put("sortName", sortName);
  }

  private static Object getTicketValue(String widgetName, String key)
  {
    return getConfig(widgetName).get(key);
  }

  private static Map<String, Object> getConfig(String widgetName) {
    Map ticketMap = getTicket();
    return (Map)ticketMap.get(widgetName);
  }

  private static Map<String, Map<String, Object>> getTicket()
  {
    Map ticketMap = (Map)ThreadLocalManager.get("paginationConfig");

    return ticketMap;
  }

  public static void setParams(String widgetName, String key, Object value)
  {
    createTicket();

    createWidgetConfig(widgetName);
    Map ticketMap = getTicket();
    Map configMap = getConfig(widgetName);
    configMap.put(key, value);
    ticketMap.put(widgetName, configMap);
  }

  public static int getTotalNum(String widgetName) {
    return ((Integer)getTicketValue(widgetName, "rows")).intValue();
  }

  public static int getPageNum(String widgetName) {
    return ((Integer)getTicketValue(widgetName, "pageNum")).intValue();
  }

  public static String getSortFlag(String widgetName) {
    Object obj = getTicketValue(widgetName, "sortFlag");
    if (obj == null) {
      return null;
    }
    return (String)obj;
  }

  public static String getSortName(String widgetName) {
    Object obj = (String)getTicketValue(widgetName, "sortName");
    if (obj == null) {
      return null;
    }
    return (String)obj;
  }

  public static void setTotalRecorder(String widgetName, int num) {
    Map ticketMap = getTicket();
    Map configMap = (Map)ticketMap.get(widgetName);
    configMap.put("totalRows", Integer.valueOf(num));
  }

  public static int getTotalRecorder(String widgetName) {
    Map ticketMap = getTicket();
    Map configMap = (Map)ticketMap.get(widgetName);
    return ((Integer)configMap.get("totalRows")).intValue();
  }

  private static void setCommonConfig(String widgetName, Map<String, Object> rdoData)
  {
    getConfig(widgetName).put("bizParams", rdoData.get("bizParams"));
  }

  public static enum SortFlag
  {
    desc, asc;
  }
}