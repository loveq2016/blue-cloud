package com.yonyouhealth.uaph.framework.persistence.pagination;

import java.util.Map;

public abstract interface IPaginationReqEvent
{
  public static final String PAGINATION_PARAM = "paginationParam";

  public abstract Map<String, Object> getPaginationParams();
}