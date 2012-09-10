package com.yonyouhealth.uaph.framework.comm.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import com.yonyouhealth.uaph.framework.comm.exception.helper.CSSExceptionHelper;

public class CSSBaseCheckedException extends Exception
  implements ICSSBaseException
{
  private static final long serialVersionUID = -6964284094568438323L;
  private Throwable rootCause;
  private CSSExceptionHelper helper = new CSSExceptionHelper();

  private String content = "";

  public Throwable getRootCause()
  {
    return this.rootCause;
  }

  public void setRootCause(Throwable rootCause) {
    this.rootCause = rootCause;
  }

  public CSSBaseCheckedException(String code)
  {
    super(code);
    this.helper.setCode(code);
    this.content = this.helper.getContent();
  }

  public CSSBaseCheckedException(String code, Throwable ex) {
    super(code, ex);
    this.rootCause = ex;
    this.helper.setCode(code);
    this.helper.parseExceptionStackInfo(ex);
    this.content = this.helper.getContent();
  }

  public CSSBaseCheckedException(String code, List<?> params) {
    super(code);
    this.helper.setCode(code);
    this.helper.addParam(params);
    this.content = this.helper.getContent();
  }

  public CSSBaseCheckedException(String code, List<?> params, Throwable ex) {
    super(code, ex);
    this.helper.setCode(code);
    this.helper.addParam(params);
    this.helper.parseExceptionStackInfo(ex);
    this.content = this.helper.getContent();
  }

  public CSSBaseCheckedException(String code, String param1, Throwable ex) {
    super(code, ex);
    this.helper.setCode(code);
    this.helper.addParam(param1);
    this.helper.parseExceptionStackInfo(ex);
    this.content = this.helper.getContent();
  }

  public CSSBaseCheckedException(String code, String param) {
    super(code);
    this.helper.setCode(code);
    this.helper.addParam(param);
    this.content = this.helper.getContent();
  }

  public List<?> getParam() {
    return this.helper.getParam();
  }

  public String getCode() {
    return this.helper.getCode();
  }

  public String getContent() {
    return this.content;
  }

  public String getExceptionStackInfo() {
    return this.helper.getExceptionStackInfo();
  }

  public String getMessage()
  {
    if (this.rootCause == null) {
      return super.getMessage();
    }
    return super.getMessage() + "; nested exception is: \n\t" + this.rootCause.toString();
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
}