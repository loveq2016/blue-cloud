package com.yonyouhealth.uaph.framework.comm.log;

import org.slf4j.Logger;
import org.slf4j.spi.LocationAwareLogger;

public class LogWritter
{
  private static final String FQCN = LogWritter.class.getName();
  private LocationAwareLogger logger;

  public LogWritter(Logger logger)
  {
    this.logger = ((LocationAwareLogger)logger);
  }

  public void debug(String message) {
	  nc.bs.logging.Logger.debug(message);
//    if (this.logger.isDebugEnabled())
//      this.logger.log(null, FQCN, 10, message, null);
  }

  public void debug(String message, Throwable ex)
  {
	  nc.bs.logging.Logger.debug(message);
//    if (this.logger.isDebugEnabled())
//      this.logger.log(null, FQCN, 10, message, ex);
  }

  public void info(String message)
  {
	  nc.bs.logging.Logger.info(message);
//    if (this.logger.isInfoEnabled())
//      this.logger.log(null, FQCN, 20, message, null);
  }

  public void info(String message, Throwable ex)
  {
	  nc.bs.logging.Logger.info(message);
//    if (this.logger.isInfoEnabled())
//      this.logger.log(null, FQCN, 20, message, ex);
  }

  public void error(String message)
  {
	  nc.bs.logging.Logger.error(message);
//    if (this.logger.isErrorEnabled())
//      this.logger.log(null, FQCN, 40, message, null);
  }

  public void error(String message, Throwable ex)
  {
	  nc.bs.logging.Logger.error(message, ex);
//    if (this.logger.isErrorEnabled())
//      this.logger.log(null, FQCN, 40, message, ex);
  }
}