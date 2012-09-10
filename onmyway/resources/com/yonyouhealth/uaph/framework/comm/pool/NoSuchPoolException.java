package com.yonyouhealth.uaph.framework.comm.pool;

public class NoSuchPoolException extends PoolException
{
  private static final long serialVersionUID = 592217447649135635L;

  public NoSuchPoolException(String resCode, String param1)
  {
    super(resCode, param1);
  }

  public NoSuchPoolException(String resCode) {
    super(resCode);
  }
}