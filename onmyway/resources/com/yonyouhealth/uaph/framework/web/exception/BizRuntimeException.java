package com.yonyouhealth.uaph.framework.web.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;

public class BizRuntimeException extends RuntimeException
{
  private static final long serialVersionUID = -5257929274394630566L;
  protected static final LogWritter log = LogFactory.getLogger(BizRuntimeException.class);
  private String logEnable = Logger.getLogger("com.css").getLevel().toString();

  public BizRuntimeException()
  {
    printOut(new String[] { "======����ҵ������ʱ�쳣�������ݿ���̨��Ϣ������룡======" });
    printStackTrace();
  }

  public BizRuntimeException(String[] paramArrayOfString)
  {
    printOut(new String[] { "======����ҵ������ʱ�쳣�������ݿ���̨��Ϣ������룡======" });
    printOut(paramArrayOfString);
    printStackTrace();
  }

  private void printOut(String[] paramArrayOfString)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("HH:mm:ss,SSS");
    localSimpleDateFormat.format(new Date(System.currentTimeMillis()));
    StackTraceElement localStackTraceElement = getStackTrace()[0];
    if (!this.logEnable.equals("DEBUG"))
      return;
    for (int i = 0; i < paramArrayOfString.length; ++i)
      System.out.println(localSimpleDateFormat.format(new Date(System.currentTimeMillis())) + " ERROR " + localStackTraceElement.getClassName() + ":" + localStackTraceElement.getLineNumber() + " - " + paramArrayOfString[i]);
  }
}