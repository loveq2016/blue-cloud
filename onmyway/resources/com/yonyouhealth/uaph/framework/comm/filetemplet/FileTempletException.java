package com.yonyouhealth.uaph.framework.comm.filetemplet;

import java.util.List;

import com.yonyouhealth.uaph.framework.comm.exception.CSSBaseRuntimeException;


public class FileTempletException extends CSSBaseRuntimeException
{
  private static final long serialVersionUID = 4024118715801869710L;

  public FileTempletException(String resCode)
  {
    super(resCode);
  }

  public FileTempletException(String resCode, Throwable ex) {
    super(resCode, ex);
  }

  public FileTempletException(String resCode, List<?> params) {
    super(resCode, params);
  }

  public FileTempletException(String resCode, List<?> params, Throwable ex) {
    super(resCode, params, ex);
  }

  public FileTempletException(String resCode, String param1) {
    super(resCode, param1);
  }

  public FileTempletException(String resCode, String param1, Throwable ex) {
    super(resCode, param1, ex);
  }

  public FileTempletException(String resCode, String param1, String param2) {
    super(resCode, param1, param2);
  }

  public FileTempletException(String resCode, String param1, String param2, Throwable ex)
  {
    super(resCode, param1, param2, ex);
  }
}