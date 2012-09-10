package com.yonyouhealth.uaph.framework.comm.filetemplet.tempfile;

import java.util.Iterator;

public class StringTempFile extends AbsTempFile
{
  public Object getTempContent()
  {
    String result = "";
    for (Iterator iter = getContentList().iterator(); iter.hasNext(); ) {
      result = result + iter.next();
    }
    return result;
  }
}