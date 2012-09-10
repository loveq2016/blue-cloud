package com.yonyouhealth.uaph.framework.web.mvc.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheCode
{
  public abstract String tableName();

  public abstract String codeRowName();

  public abstract String codeName();

  public abstract String valueName();
}