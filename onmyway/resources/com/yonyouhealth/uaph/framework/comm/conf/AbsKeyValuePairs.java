package com.yonyouhealth.uaph.framework.comm.conf;

public abstract class AbsKeyValuePairs
  implements IKeyValuePairs
{
  private String key;
  private Object content;

  public String getKey()
  {
    return this.key;
  }

  public Object getContent() {
    return this.content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  public void setKey(String key) {
    this.key = key;
  }
}