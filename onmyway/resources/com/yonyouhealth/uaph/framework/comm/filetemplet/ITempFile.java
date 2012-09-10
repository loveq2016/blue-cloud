package com.yonyouhealth.uaph.framework.comm.filetemplet;

import java.util.List;

public abstract interface ITempFile
{
  public abstract Object getTempContent();

  public abstract void setContentList(List paramList);
}