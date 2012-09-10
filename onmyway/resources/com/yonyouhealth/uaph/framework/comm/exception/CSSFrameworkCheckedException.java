package com.yonyouhealth.uaph.framework.comm.exception;

import java.util.List;

public class CSSFrameworkCheckedException extends CSSBaseCheckedException
{
  private static final long serialVersionUID = -3181613337965136931L;

  public CSSFrameworkCheckedException(String code)
  {
    super(code);
  }

  public CSSFrameworkCheckedException(String code, Throwable ex) {
    super(code, ex);
  }

  public CSSFrameworkCheckedException(String code, List<?> params) {
    super(code, params);
  }

  public CSSFrameworkCheckedException(String code, List<?> params, Throwable ex) {
    super(code, params, ex);
  }
}