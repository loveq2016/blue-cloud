package com.yonyouhealth.uaph.framework.comm.conf;

import java.util.ArrayList;

public abstract interface IConfLoader
{
  public abstract ArrayList analyse(ConfTreeNode paramConfTreeNode);
}