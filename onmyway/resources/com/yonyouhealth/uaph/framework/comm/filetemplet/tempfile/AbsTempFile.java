package com.yonyouhealth.uaph.framework.comm.filetemplet.tempfile;

import java.util.ArrayList;
import java.util.List;

import com.yonyouhealth.uaph.framework.comm.filetemplet.ITempFile;

public abstract class AbsTempFile
  implements ITempFile
{
  List contentList;

  public AbsTempFile()
  {
    this.contentList = new ArrayList();
  }
  public List getContentList() {
    return this.contentList;
  }

  public void setContentList(List contentList) {
    this.contentList = contentList;
  }
}