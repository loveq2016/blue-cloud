package com.yonyouhealth.uaph.framework.comm.filetemplet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import com.yonyouhealth.uaph.framework.comm.conf.ConfManager;
import com.yonyouhealth.uaph.framework.comm.filetemplet.ruler.DefaultPropertiesRuler;
import com.yonyouhealth.uaph.framework.comm.filetemplet.tempfile.PropertiesTempFile;
import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.comm.util.FileUtils;

public class FileTempletFactory
{
  private static final LogWritter logger = LogFactory.getLogger(FileTempletFactory.class);
  PropertiesTempFile tempfile;
  DefaultPropertiesRuler ruler;
  private static FileTempletFactory instance = null;

  private FileTempletFactory()
  {
    this.ruler = new DefaultPropertiesRuler();
    this.tempfile = new PropertiesTempFile();

    init();
  }

  private void init() {
    String files = (String)ConfManager.getValueByKey("exception-properties");
    if (files == null) {
      return;
    }

    StringTokenizer st = new StringTokenizer(files, ",");
    while (st.hasMoreElements()) {
      String propFile = (String)st.nextElement();
      Properties props = getProperties(propFile);
      addTempletProperties(props);
    }
  }

  private Properties getProperties(String fileName)
  {
    Properties props = new Properties();
    InputStream is = null;
    try {
      FileUtils ft = new FileUtils(fileName);
      is = ft.getInputStream();
      if (is != null)
        props.load(is);
    }
    catch (IOException ex)
    {
      logger.error("�����쳣�����ļ�" + fileName + "�����쳣!", ex);
    }

    return props;
  }

  public static FileTempletFactory sigleton()
  {
    if (instance == null) {
      instance = new FileTempletFactory();
    }

    return instance;
  }

  public Object getContent(Map params)
  {
    this.ruler.setParams(params);
    return this.ruler.make(this.tempfile);
  }

  public void addTempletProperties(Properties props)
  {
    this.tempfile.processProperties(props);
  }

  public DefaultPropertiesRuler getRuler() {
    return this.ruler;
  }

  public void setRuler(DefaultPropertiesRuler ruler) {
    this.ruler = ruler;
  }

  public PropertiesTempFile getTempfile() {
    return this.tempfile;
  }
  public void setTempfile(PropertiesTempFile tempfile) {
    this.tempfile = tempfile;
  }
}