package com.yonyouhealth.uaph.framework.core.event;

public class AppExceptionResponseEvent
  implements IResponseEvent
{
  protected Throwable ex;
  protected String exceptionMessage;
  private long costTime;

  public AppExceptionResponseEvent()
  {
    this.exceptionMessage = "";
  }

  public String getExceptionMessage()
  {
    return this.exceptionMessage;
  }

  public boolean isOne()
  {
    return true;
  }

  public void setException(Throwable ex) {
    this.ex = ex;
  }

  public Throwable getException() {
    return this.ex;
  }

  public boolean isSuccess() {
    return false;
  }

  public void setExceptionMessage(String exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  public void setCostTime(long costTime) {
    this.costTime = costTime;
  }

  public long getCostTime() {
    return this.costTime;
  }
}