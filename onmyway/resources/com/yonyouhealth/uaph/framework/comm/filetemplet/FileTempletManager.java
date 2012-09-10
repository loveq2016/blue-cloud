package com.yonyouhealth.uaph.framework.comm.filetemplet;

import java.util.Map;

public class FileTempletManager
{
  public static final String FILETEMPLET_NAME = "filetemplet";

  public static Object getContent(String tfkey, Map params)
  {
    return FileTempletFactory.sigleton().getContent(params);
  }
}