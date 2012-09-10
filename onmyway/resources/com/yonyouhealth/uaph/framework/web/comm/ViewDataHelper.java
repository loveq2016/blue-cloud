package com.yonyouhealth.uaph.framework.web.comm;

//import uaph.uhweb.event.Event;
//import uaph.uhweb.event.EventManager;
//import uaph.uhweb.event.annotation.HandleType;
//import uaph.uhweb.event.plugins.CommonMonitorEvent;
//import uaph.uhweb.monitor.util.UserData;

public class ViewDataHelper
{
//  private static UserData object;
//  private static Event event;
//  static String in;
//  protected static final LogWritter log = LogFactory.getLogger(ViewDataHelper.class);
//
//  public static void dealViewData(SwordDataSet paramSwordDataSet)
//    throws Exception
//  {
//    log.debug("��ʼ����proxy��������tid=" + paramSwordDataSet.getTid());
//    SwordReq localSwordReq = new SwordReq(paramSwordDataSet.getTid(), ContextAPI.getReqDataSet());
//    Object localObject1;
//    Object localObject2;
//    try
//    {
//      Class[] arrayOfClass = { IRequestEvent.class };
//      localObject1 = BizDelegate.class.getMethod("delegate", arrayOfClass);
//      before(localObject1, Thread.currentThread(), localSwordReq.getTransactionID() + "_" + localSwordReq.getMethod(), BizDelegate.class.getName());
//      localObject2 = (SwordRes)BizDelegate.delegate(localSwordReq);
//      after(BizDelegate.class.getName());
//      SwordDataSet localSwordDataSet = ((SwordRes)localObject2).getResDataSet();
//      ThreadLocalManager.add("resDataSet", localSwordDataSet);
//    }
//    catch (Exception localException1)
//    {
//      localObject1 = new Class[] { IRequestEvent.class };
//      localObject2 = null;
//      try
//      {
//        localObject2 = BizDelegate.class.getMethod("delegate", localObject1);
//      }
//      catch (Exception localException2)
//      {
//      }
//      afterThrowing((Method)localObject2, Thread.currentThread(), localException1, localSwordReq.getTransactionID() + "_" + localSwordReq.getMethod(), BizDelegate.class.getName());
//      throw localException1;
//    }
//    log.debug("����proxy����tid=" + paramSwordDataSet.getTid());
//  }
//
//  private static void before(Object paramObject1, Object paramObject2, String paramString1, String paramString2)
//  {
//    Random localRandom = new Random();
//    in = Integer.valueOf(localRandom.nextInt()).toString();
//    object = new UserData();
//    object.set("method.property.thread", paramObject2);
//    object.set("customer.tid", paramString1);
//    object.set("method.name", paramString2);
//    try
//    {
//      object.set("method.property.method.name", paramObject1);
//    }
//    catch (Exception localException)
//    {
//      localException.printStackTrace();
//    }
//    event = new CommonMonitorEvent(paramString2, "sword.event.method", in, HandleType.synchronous, object);
//    EventManager.getInstance().fireEvent(event);
//  }
//
//  private static void after(String paramString)
//  {
//    event = new CommonMonitorEvent(paramString, "sword.event.method", in, HandleType.synchronous, object);
//    EventManager.getInstance().fireEvent(event);
//  }
//
//  private static void afterThrowing(Method paramMethod, Object paramObject, Throwable paramThrowable, String paramString1, String paramString2)
//  {
//    Random localRandom = new Random();
//    String str = Integer.valueOf(localRandom.nextInt()).toString();
//    UserData localUserData = new UserData();
//    localUserData.set("method.property.thread", paramObject);
//    localUserData.set("exception.property", paramThrowable);
//    localUserData.set("method.property.method.name", paramMethod);
//    localUserData.set("customer.tid", paramString1);
//    CommonMonitorEvent localCommonMonitorEvent = new CommonMonitorEvent(paramString2, "sword.event.exception", str, HandleType.asynchronous, localUserData);
//    EventManager.getInstance().fireEvent(localCommonMonitorEvent);
//  }
}