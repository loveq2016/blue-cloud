package com.yonyouhealth.uaph.framework.comm.util;

public class RefUtils
{
  public static String getDataType(Object obj)
  {
    if (obj == null) {
      return null;
    }

    String type = obj.getClass().getName();

    int pos = type.lastIndexOf(".");
    if (pos >= 0) {
      type = type.substring(pos + 1);
    }

    return type;
  }
}