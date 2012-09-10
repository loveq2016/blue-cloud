package com.yonyouhealth.uaph.framework.comm.conf;

public class XmlKeyValuePairs extends AbsKeyValuePairs
{
  private String key;
  private Object content;

  public XmlKeyValuePairs(String key, Object content)
  {
    this.key = key;
    this.content = content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  public Object getContent() {
    return this.content;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }
}