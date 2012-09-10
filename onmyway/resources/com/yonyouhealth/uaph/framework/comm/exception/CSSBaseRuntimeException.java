package com.yonyouhealth.uaph.framework.comm.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.yonyouhealth.uaph.framework.comm.exception.helper.CSSExceptionHelper;

public class CSSBaseRuntimeException extends RuntimeException
  implements ICSSBaseException
{
  private static final long serialVersionUID = 769267092244034084L;
  private CSSExceptionHelper helper = new CSSExceptionHelper();

  private String content = "";
  private Throwable rootCause;

  public CSSBaseRuntimeException(String code)
  {
    super(code);
    this.helper.setCode(code);
    this.content = this.helper.getContent();
  }

  public CSSBaseRuntimeException(String code, Throwable ex) {
    super(code, ex);
    this.helper.setCode(code);
    this.helper.parseExceptionStackInfo(ex);
    this.content = this.helper.getContent();
  }

  public CSSBaseRuntimeException(String code, List<?> params) {
    super(code);
    this.helper.setCode(code);
    this.helper.addParam(params);
    this.content = this.helper.getContent();
  }

  public CSSBaseRuntimeException(String code, List<?> params, Throwable ex) {
    super(code, ex);
    this.helper.setCode(code);
    this.helper.addParam(params);
    this.helper.parseExceptionStackInfo(ex);
    this.content = this.helper.getContent();
  }

  public CSSBaseRuntimeException(String code, String param1, Throwable ex) {
    super(code, ex);
    this.helper.setCode(code);
    this.helper.addParam(param1);
    this.helper.parseExceptionStackInfo(ex);
    this.content = this.helper.getContent();
  }

  public CSSBaseRuntimeException(String code, String param1, String param2, Throwable ex) {
    super(code, ex);
    this.helper.setCode(code);
    this.helper.addParam(param1);
    this.helper.addParam(param2);
    this.helper.parseExceptionStackInfo(ex);
    this.content = this.helper.getContent();
  }

  public CSSBaseRuntimeException(String code, String param1) {
    super(code);
    this.helper.setCode(code);
    this.helper.addParam(param1);
    this.content = this.helper.getContent();
  }

  public CSSBaseRuntimeException(String code, String param1, String param2) {
    super(code);
    this.helper.setCode(code);
    this.helper.addParam(param1);
    this.helper.addParam(param2);
    this.content = this.helper.getContent();
  }

  public Throwable getRootCause() {
    return this.rootCause;
  }

  public void setRootCause(Throwable newRootCause) {
    this.rootCause = newRootCause;
  }

  public String getMessage()
  {
    if (this.rootCause == null) {
      return super.getMessage() + "\t\n������:" + getParam();
    }
    return super.getMessage() + "; --->Ƕ���쳣: \n\t" + this.rootCause.getMessage() + "\t\n������:" + getParam();
  }

  public void printStackTrace(PrintStream ps)
  {
    if (this.rootCause == null) {
      super.printStackTrace(ps);
    } else {
      ps.println(this);
      this.rootCause.printStackTrace(ps);
    }
  }

  public void printStackTrace(PrintWriter pw)
  {
    if (this.rootCause == null) {
      super.printStackTrace(pw);
    } else {
      pw.println(this);
      this.rootCause.printStackTrace(pw);
    }
  }

  public void printStackTrace()
  {
    printStackTrace(System.err);
  }

  public ICSSBaseException addParam(String param) {
    CSSExceptionHelper helperTmp = new CSSExceptionHelper();
    helperTmp.setCode(this.helper.getCode());
    List params = this.helper.getParam();
    if (params == null) {
      params = new ArrayList();
    }
    params.add(param);
    helperTmp.addParam(params);

    String tmp = this.helper.getContent();
    this.helper = helperTmp;
    this.content = tmp;
    return this;
  }

  public ICSSBaseException addParam(List<?> params) {
    CSSExceptionHelper helperTmp = new CSSExceptionHelper();
    helperTmp.setCode(this.helper.getCode());
    helperTmp.addParam(params);

    String tmp = helperTmp.getContent();
    this.helper = helperTmp;
    this.content = tmp;
    return this;
  }

  public List<?> getParam() {
    return this.helper.getParam();
  }

  public void cleanParam() {
    this.helper.cleanParam();
  }

  public String getCode() {
    return this.helper.getCode();
  }

  public void setCode(String code) {
    this.helper.setCode(code);
  }

  public String getContent() {
    return this.helper.getContent();
  }

  public String getExceptionStackInfo() {
    return this.helper.getExceptionStackInfo();
  }
}