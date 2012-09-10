package com.yonyouhealth.uaph.framework.comm.conf;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;

public class ConfLoaderFactory
{
  private static final LogWritter logger = LogFactory.getLogger(ConfLoaderFactory.class);

  private static ConfLoaderFactory instance = null;

  public static synchronized ConfLoaderFactory getInstance() {
    if (instance == null) {
      instance = new ConfLoaderFactory();
    }
    return instance;
  }

  public IConfLoader getLoader(String loadClassName)
  {
    IConfLoader confLoader = null;
    try {
      confLoader = (IConfLoader)Class.forName(loadClassName).newInstance();
    } catch (ClassNotFoundException ex) {
      logger.error("��ȡ���������ʵ������쳣��", ex);
    } catch (IllegalAccessException ex) {
      logger.error("��ȡ���������ʵ������쳣��", ex);
    } catch (InstantiationException ex) {
      logger.error("��ȡ���������ʵ������쳣��", ex);
    }

    return confLoader;
  }
}