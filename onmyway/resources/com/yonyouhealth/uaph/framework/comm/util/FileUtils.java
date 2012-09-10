package com.yonyouhealth.uaph.framework.comm.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;

public class FileUtils
{
  private static final LogWritter logger = LogFactory.getLogger(FileUtils.class);
  private String loadFilename;

  public FileUtils()
  {
  }

  public FileUtils(String loadFilename)
  {
    this.loadFilename = loadFilename;
  }

  public String getAbsolutePath(String loadFilename) {
    ClassLoader loader = super.getClass().getClassLoader();
    URL url = loader.getResource(loadFilename);
    return url.getPath();
  }

  public String getAbsolutePath() {
    ClassLoader loader = super.getClass().getClassLoader();
    URL url = loader.getResource(this.loadFilename);
    return url.getPath();
  }

  public InputStream getInputStream() {
    ClassLoader loader = super.getClass().getClassLoader();
    URL url = loader.getResource(this.loadFilename);

    logger.debug(this.loadFilename + ".url=" + url.toString());
    try
    {
      if (url != null)
        return url.openStream();
    } catch (IOException ex) {
      logger.error("�������ļ�������");
    }
    return null;
  }
}