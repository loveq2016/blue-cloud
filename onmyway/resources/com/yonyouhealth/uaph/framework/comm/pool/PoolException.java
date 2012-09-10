package com.yonyouhealth.uaph.framework.comm.pool;

import java.util.List;

import com.yonyouhealth.uaph.framework.comm.exception.CSSBaseRuntimeException;


public class PoolException extends CSSBaseRuntimeException
{
  private static final long serialVersionUID = -5894178392384941082L;

  public PoolException(String resCode)
  {
    super(resCode);
  }

  public PoolException(String resCode, Throwable ex) {
    super(resCode, ex);
  }

  public PoolException(String resCode, List<?> params) {
    super(resCode, params);
  }

  public PoolException(String resCode, List<?> params, Throwable ex) {
    super(resCode, params, ex);
  }

  public PoolException(String resCode, String param1) {
    super(resCode, param1);
  }

  public PoolException(String resCode, String param1, Throwable ex) {
    super(resCode, param1, ex);
  }

  public PoolException(String resCode, String param1, String param2) {
    super(resCode, param1, param2);
  }

  public PoolException(String resCode, String param1, String param2, Throwable ex)
  {
    super(resCode, param1, param2, ex);
  }
}