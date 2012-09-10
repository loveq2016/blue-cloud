package com.yonyouhealth.uaph.framework.web.mvc.beans;

import java.util.List;
import java.util.Map;

public class TRBean extends AbsDataBean<List<TDBean>>
{
  private static final long serialVersionUID = -4612366409609892207L;
  private List<TDBean> tdList;

  public List<TDBean> getTdList()
  {
    return this.tdList;
  }

  public void setTdList(List<TDBean> paramList)
  {
    this.tdList = paramList;
  }

  public Map<String, List<TDBean>> viewData()
  {
    return this.viewData;
  }
}