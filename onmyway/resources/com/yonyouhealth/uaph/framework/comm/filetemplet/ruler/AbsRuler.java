package com.yonyouhealth.uaph.framework.comm.filetemplet.ruler;

import com.yonyouhealth.uaph.framework.comm.filetemplet.IRuler;

public abstract class AbsRuler
  implements IRuler
{
  private Object params;

  public Object getParams()
  {
    return this.params;
  }
  public void setParams(Object params) {
    this.params = params;
  }
}