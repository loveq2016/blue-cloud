package com.yonyouhealth.uaph.framework.web.comm;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil
{
  public static final String[] dateFormat = { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd HH:mm:ss.SSSS", "yyyy-MM-dd", "yyyy年MM月dd日 hh时mm分ss秒", "yyyy年MM月dd日" };
  public static final String[] dateRegex = { "[1|2][0-9][0-9]{2}-[0|1][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]", "[1|2][0-9][0-9]{2}-[0|1][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,4}", "[1|2][0-9][0-9]{2}-[0|1][0-9]-[0-3][0-9]", "[1|2][0-9][0-9]{2}年[0|1][0-9]月[0-3][0-9]日 [0-2][0-9]时[0-5][0-9]分[0-5][0-9]秒", "[1|2][0-9][0-9]{2}年[0|1][0-9]月[0-3][0-9]日" };

  public static java.sql.Date parseToDate(String paramString)
  {
    for (int i = 0; i < dateRegex.length; ++i)
    {
      if (!paramString.matches(dateRegex[i]))
        continue;
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(dateFormat[i]);
      try
      {
        java.sql.Date localDate = new java.sql.Date(localSimpleDateFormat.parse(paramString).getTime());
        return localDate;
      }
      catch (ParseException localParseException)
      {
        throw new RuntimeException("字符串转换日期出错！" + paramString);
      }
    }
    throw new RuntimeException("输入的字符串不合法！请检查。" + paramString);
  }

  public static Timestamp parseToTimestamp(String paramString)
  {
    return new Timestamp(parseToDate(paramString).getTime());
  }

  public static Calendar parseToCalendar(String paramString)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(parseToDate(paramString).getTime());
    return localCalendar;
  }

  public static String dateToStr(Object paramObject)
  {
    if (paramObject == null)
      return "";
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(dateFormat[1]);
    Object localObject;
    if (paramObject instanceof Calendar)
    {
      localObject = (Calendar)paramObject;
      return localSimpleDateFormat.format(new java.util.Date(((Calendar)localObject).getTimeInMillis()));
    }
    if (paramObject instanceof Timestamp)
    {
      localObject = (Timestamp)paramObject;
      return localSimpleDateFormat.format(new java.util.Date(((Timestamp)localObject).getTime()));
    }
    if (paramObject instanceof java.util.Date)
    {
      localObject = (java.util.Date)paramObject;
      return localSimpleDateFormat.format((java.util.Date)localObject);
    }
    return (String)paramObject.toString();
  }

  public static enum MilliSecond
  {
    S, SS, SSS, SSSS;
  }

  public static enum Second
  {
    s, ss;
  }

  public static enum Minute
  {
    m, mm;
  }

  public static enum Hour
  {
    h, hh;
  }

  public static enum Day
  {
    d, dd;
  }

  public static enum Month
  {
    M, MM;
  }

  public static enum DataYear
  {
    yy, yyyy;
  }
}