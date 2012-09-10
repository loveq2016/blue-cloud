package com.yonyouhealth.uaph.framework.comm.pool;

public class OccupiedLocationException extends PoolException
{
  private static final long serialVersionUID = -2558929808161001935L;

  public OccupiedLocationException(String resCode, String param1)
  {
    super(resCode, param1);
  }
}