package com.yonyouhealth.uaph.framework.comm.exception;

import java.util.List;

public class CSSBizCheckedException extends CSSBaseCheckedException
{
  private static final long serialVersionUID = -983233274161862190L;

  public CSSBizCheckedException(String code)
  {
    super(code);
  }

  public CSSBizCheckedException(String code, Throwable ex) {
    super(code, ex);
  }

  public CSSBizCheckedException(String code, List<?> params) {
    super(code, params);
  }

  public CSSBizCheckedException(String code, List<?> params, Throwable ex) {
    super(code, params, ex);
  }
}