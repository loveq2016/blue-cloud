package com.yonyouhealth.uaph.framework.comm.log;

import org.slf4j.LoggerFactory;

public class LogFactory
{
  public static LogWritter getLogger(Class clazz)
  {
    return new LogWritter(LoggerFactory.getLogger(clazz));
  }

  public static LogWritter getLogger(String name)
  {
    return new LogWritter(LoggerFactory.getLogger(name));
  }
}