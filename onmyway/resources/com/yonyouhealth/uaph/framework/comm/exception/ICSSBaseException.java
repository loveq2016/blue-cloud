package com.yonyouhealth.uaph.framework.comm.exception;

import java.util.List;

public abstract interface ICSSBaseException
{
  public abstract List<?> getParam();

  public abstract String getContent();

  public abstract String getExceptionStackInfo();

  public abstract String getCode();
}