package com.yonyouhealth.uaph.framework.comm.codecache;

import java.io.Serializable;

public class CacheFilter
  implements Serializable
{
  public static final String FILTER_OPERATOR_EQUAL = "=";
  public static final String FILTER_OPERATOR_NOT_EQUAL = "<>";
  public static final String FILTER_OPERATOR_GREATER_THAN = ">";
  public static final String FILTER_OPERATOR_LESS_THEN = "<";
  public static final String FILTER_OPERATOR_IN = "in";
  public static final String FILTER_OPERATOR_NOT_IN = "not in";
  private String fieldName;
  private Object fieldValue;
  private String filterOperator;

  public CacheFilter()
  {
    this.filterOperator = "=";
  }

  public String getFieldName() {
    return this.fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public Object getFieldValue() {
    return this.fieldValue;
  }

  public void setFieldValue(Object fieldValue) {
    this.fieldValue = fieldValue;
  }

  public String getFilterOperator() {
    return this.filterOperator;
  }

  public void setFilterOperator(String filterOperator) {
    this.filterOperator = filterOperator;
  }
}