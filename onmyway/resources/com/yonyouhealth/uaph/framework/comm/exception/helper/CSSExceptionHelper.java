package com.yonyouhealth.uaph.framework.comm.exception.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyouhealth.uaph.framework.comm.filetemplet.FileTempletManager;

public class CSSExceptionHelper
  implements Serializable
{
  private static final long serialVersionUID = -8420003610577985529L;
  String code;
  List params;
  static final String FILETEMPLET_NAME = "exception";
  private String exceptionStackInfo;
  private String exceptionContent;

  public CSSExceptionHelper()
  {
    this.params = new ArrayList();

    this.exceptionStackInfo = "";

    this.exceptionContent = null;
  }
  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void addParam(String param) {
    this.params.add(param);
  }

  public void addParam(List param) {
    this.params.addAll(param);
  }

  public List getParam() {
    return this.params;
  }

  public void cleanParam() {
    this.params.clear();
  }

  public String getContent() {
    if (this.exceptionContent != null) {
      return this.exceptionContent;
    }

    Map mapparam = new HashMap();
    mapparam.put(this.code, this.params);
    Map mapresult = (Map)FileTempletManager.getContent("exception", mapparam);

    this.exceptionContent = ((String)mapresult.get(this.code));

    return this.exceptionContent;
  }

  public void parseExceptionStackInfo(Throwable ex) {
    StringBuffer sb = new StringBuffer();
    if (ex == null) {
      return;
    }

    sb.append(ex.getClass().getName()).append(":").append(ex.getMessage()).append("\r\n");

    StackTraceElement[] stack = ex.getStackTrace();
    for (int i = 0; i < stack.length; ++i) {
      sb.append("\t").append(stack[i]).append("\r\n");
    }

    this.exceptionStackInfo = sb.toString();
  }

  public String getExceptionStackInfo() {
    return this.exceptionStackInfo;
  }
}