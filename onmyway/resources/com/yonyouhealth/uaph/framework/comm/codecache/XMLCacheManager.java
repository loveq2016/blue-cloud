package com.yonyouhealth.uaph.framework.comm.codecache;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;


public class XMLCacheManager
{
  private static final LogWritter logger = LogFactory.getLogger(XMLCacheManager.class);

  private static DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
  private DocumentBuilder builder;
  private Document doc;
  private String localXMLCacheRoot = "";

  private Map versionList = new HashMap();

  public XMLCacheManager()
  {
    try {
      this.builder = dbFact.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      logger.error("创建文档builder出现异常！", e);
    }
  }

  public void init() {
    File rootdir = new File(this.localXMLCacheRoot);
    if (!rootdir.exists()) {
      rootdir.mkdirs();
    }
    File file = new File(rootdir, "versionList.xml");
    if (!file.exists())
      return;
    try
    {
      this.doc = this.builder.parse(file);
      Element rootElement = this.doc.getDocumentElement();
      NodeList fileIter = rootElement.getChildNodes();
      for (int i = 0; i < fileIter.getLength(); ++i) {
        Node fileElement = fileIter.item(i);
        String name = fileElement.getAttributes().getNamedItem("Name").getNodeValue();
        String value = fileElement.getAttributes().getNamedItem("Version").getNodeValue();

        name = name.toLowerCase();
        Integer version = new Integer(value);
        this.versionList.put(name, version);
        logger.debug("加载本地文件缓存中的初始版本信息:" + name + " (version)= " + value);
      }
    }
    catch (Exception ex) {
      logger.error("代码表缓存管理程序版本标识初始化解析出现异常!", ex);
    }
  }

  public String getLocalXMLCacheRoot() {
    return this.localXMLCacheRoot;
  }

  public void setLocalXMLCacheRoot(String localXMLCacheRoot) {
    this.localXMLCacheRoot = localXMLCacheRoot;
  }

  public void updateLocalXMLCache(Map data) throws Exception
  {
    Iterator iter = data.values().iterator();
    while (iter.hasNext()) {
      CacheTable table = (CacheTable)iter.next();
      String tableName = table.getTableName().toLowerCase();
      int version = table.getVersion();

      Object obj = this.versionList.get(tableName);
      int verOld = -1;
      if (obj == null) {
        verOld = -1;
      }
      else {
        verOld = ((Integer)obj).intValue();
      }
      if (version > verOld) {
        this.versionList.put(tableName, new Integer(version));

        saveCacheTable(tableName, table);
      }

    }

    createVersionListFile();
  }

  private void saveCacheTable(String tableName, CacheTable table)
    throws Exception
  {
    this.doc = this.builder.newDocument();
    Element root = this.doc.createElement("table");
    root.setAttribute("name", tableName);
    this.doc.appendChild(root);
    List list = table.getCacheData();

    if ((table.getCacheType() == 1) && (((table.getCacheData() == null) || (table.getCacheData().size() == 0))))
    {
      list = ServerCacheManager.getCacheUserInterface().getCacheDataFromDB(tableName, "");
    }

    for (int index = 0; index < list.size(); ++index) {
      Map row = (Map)list.get(index);
      Element datarow = this.doc.createElement("row");
      Iterator iter = row.keySet().iterator();
      while (iter.hasNext()) {
        String key = "" + iter.next();
        String value = "" + row.get(key);

        datarow.setAttribute(key, value);
      }
      root.appendChild(datarow);
    }

    try
    {
      File rootdir = new File(this.localXMLCacheRoot);
      if (!rootdir.exists()) {
        rootdir.mkdirs();
      }
      File file = new File(rootdir, tableName.toUpperCase() + ".xml");

      Transformer trans = TransformerFactory.newInstance().newTransformer();
      trans.transform(new DOMSource(this.doc), new StreamResult(new FileOutputStream(file, false)));
    }
    catch (Exception e) {
      logger.error("将Document对象输出为xml时出现异常!", e);
    }
  }

  private void createVersionListFile()
  {
    this.doc = this.builder.newDocument();
    Element root = this.doc.createElement("files");
    this.doc.appendChild(root);

    Iterator iter = this.versionList.keySet().iterator();
    while (iter.hasNext()) {
      String tableName = "" + iter.next();
      Integer version = (Integer)this.versionList.get(tableName);

      Element file = this.doc.createElement("File");
      file.setAttribute("Name", tableName.toLowerCase());
      file.setAttribute("Version", "" + version);
      root.appendChild(file);
    }

    try
    {
      File rootdir = new File(this.localXMLCacheRoot);
      if (!rootdir.exists()) {
        rootdir.mkdirs();
      }
      File file = new File(rootdir, "versionList.xml");

      Transformer trans = TransformerFactory.newInstance().newTransformer();
      trans.transform(new DOMSource(this.doc), new StreamResult(new FileOutputStream(file, false)));
    }
    catch (Exception e) {
      logger.error("将Document对象输出为xml时出现异常!", e);
    }
  }
}