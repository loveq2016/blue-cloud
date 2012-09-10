package com.yonyouhealth.uaph.framework.web.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;

public class FileExport
{
  protected static final LogWritter logger = LogFactory.getLogger(FileExport.class);
  private String contentTypeName;
  private String encodeType = "UTF-8";
  private String fileName;
  private String contentType;
  private static int bufferSize = 2048;
  private static Map contentTypeMap = new HashMap();

  public FileExport(String paramString1, String paramString2)
  {
    initContentTypeMap();
    this.contentTypeName = paramString2;
    this.contentType = ((String)contentTypeMap.get(paramString2));
    this.fileName = paramString1;
  }

  private FileExport()
  {
  }

  public void exportFile(HttpServletResponse paramHttpServletResponse, byte[] paramArrayOfByte)
    throws IOException
  {
    if (this.contentTypeName.equals(".zip"))
    {
      logger.debug("ִ��doZippedExport(response, bytes, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
      doZippedExport(paramHttpServletResponse, paramArrayOfByte, this.encodeType);
    }
    else
    {
      logger.debug("ִ��doExport(response, bytes, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
      doExport(paramHttpServletResponse, paramArrayOfByte, this.encodeType);
    }
  }

  public void exportFile(HttpServletResponse paramHttpServletResponse, InputStream paramInputStream)
    throws IOException
  {
    if (this.contentTypeName.equals(".zip"))
    {
      logger.debug("ִ��doZippedExport(response, in, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
      doZippedExport(paramHttpServletResponse, paramInputStream, this.encodeType);
    }
    else
    {
      logger.debug("ִ��doExport(response, in, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
      doExport(paramHttpServletResponse, paramInputStream, this.encodeType);
    }
  }

  public void exportFile(HttpServletResponse paramHttpServletResponse, File paramFile)
    throws IOException
  {
    if (this.contentTypeName.equals(".zip"))
    {
      logger.debug("ִ��doZippedExport(response, file, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
      doZipExport(paramHttpServletResponse, paramFile, this.encodeType);
    }
    else
    {
      logger.debug("��file:" + paramFile.getName() + " ת����InputStream��������");
      FileInputStream localFileInputStream = new FileInputStream(paramFile);
      logger.debug("ִ��doExport(response, in, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
      doExport(paramHttpServletResponse, localFileInputStream, this.encodeType);
    }
  }

  public void exportFile(HttpServletResponse paramHttpServletResponse, List paramList)
    throws IOException
  {
    this.contentTypeName = ".zip";
    this.contentType = ((String)contentTypeMap.get(this.contentTypeName));
    logger.debug("ִ�� doExport4FileList(response, files, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
    doExport4FileList(paramHttpServletResponse, paramList, this.encodeType);
  }

  public void exportFile(HttpServletResponse paramHttpServletResponse, String paramString)
    throws IOException
  {
    FileInputStream localFileInputStream;
    if (this.contentTypeName.equals(".zip"))
    {
      localFileInputStream = new FileInputStream(paramString);
      doZippedExport(paramHttpServletResponse, localFileInputStream, this.encodeType);
    }
    else
    {
      logger.debug("���·��" + paramString + "���ļ���InputStream.......");
      localFileInputStream = new FileInputStream(paramString);
      logger.debug("ִ��doExport(response, in, encodeType)����������ΪfileName:" + this.fileName + "�� contentTypeName:" + this.contentTypeName + "�� contentType:" + this.contentType);
      doExport(paramHttpServletResponse, localFileInputStream, this.encodeType);
    }
  }

  private void initContentTypeMap()
  {
    if (!contentTypeMap.isEmpty())
      return;
    contentTypeMap.put(".csv", "text/comma-separated-values;");
    contentTypeMap.put(".zip", "application/x-zip-compressed;");
    contentTypeMap.put(".xml", "text/xml;");
    contentTypeMap.put(".txt", "text/plain;");
    contentTypeMap.put(".dat", "application/octet-stream;");
    contentTypeMap.put(".xls", "application/vnd.ms-excel;");
  }

  private void doExport4FileList(HttpServletResponse paramHttpServletResponse, List paramList, String paramString)
    throws IOException
  {
    paramHttpServletResponse.reset();
    paramHttpServletResponse.setContentType(this.contentType + " charset=" + paramString);
    paramHttpServletResponse.setHeader("Content-disposition", "attachment;filename=" + this.fileName + this.contentTypeName);
    ZipOutputStream localZipOutputStream = null;
    try
    {
      localZipOutputStream = new ZipOutputStream(paramHttpServletResponse.getOutputStream());
      int i = 0;
      for (i = 0; i < paramList.size(); ++i)
      {
        File localFile = (File)paramList.get(i);
        zip(localZipOutputStream, localFile, localFile.getName());
      }
      localZipOutputStream.flush();
    }
    catch (Exception localException)
    {
      logger.error("��fileList����Ϊzip�ļ�ʱ���ִ��� ", localException);
      localException.printStackTrace();
    }
    finally
    {
      closeStream(localZipOutputStream);
    }
  }

  private void doExport(HttpServletResponse paramHttpServletResponse, byte[] paramArrayOfByte, String paramString)
    throws IOException
  {
	  int i;
    paramHttpServletResponse.reset();
    paramHttpServletResponse.setContentType(this.contentType + " charset=" + paramString);
    paramHttpServletResponse.setHeader("Content-disposition", "attachment;filename=" + this.fileName + this.contentTypeName);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(paramArrayOfByte));
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(paramHttpServletResponse.getOutputStream());
      byte[] arrayOfByte = new byte[bufferSize];
      while ((i = localBufferedInputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1)
      {
        
        localBufferedOutputStream.write(arrayOfByte, 0, i);
      }
      localBufferedOutputStream.flush();
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    finally
    {
      closeStream(localBufferedOutputStream, localBufferedInputStream);
    }
  }

  private void doExport(HttpServletResponse paramHttpServletResponse, InputStream paramInputStream, String paramString)
    throws IOException
  {
	  int i;
    paramHttpServletResponse.reset();
    paramHttpServletResponse.setContentType(this.contentType + " charset=" + paramString);
    paramHttpServletResponse.setHeader("Content-disposition", "attachment;filename=" + this.fileName + this.contentTypeName);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream);
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(paramHttpServletResponse.getOutputStream());
      byte[] arrayOfByte = new byte[bufferSize];
      while ((i = localBufferedInputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1)
      {
       
        localBufferedOutputStream.write(arrayOfByte, 0, i);
      }
      localBufferedOutputStream.flush();
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    finally
    {
      closeStream(localBufferedOutputStream, paramInputStream);
    }
  }

  private synchronized void doZippedExport(HttpServletResponse paramHttpServletResponse, InputStream paramInputStream, String paramString)
    throws IOException
  {
    if (paramInputStream == null)
    {
      logger.info("[doZippedExport] Nothing to be exported");
      return;
    }
    paramHttpServletResponse.reset();
    paramHttpServletResponse.setContentType("application/x-zip-compressed; charset=" + paramString);
    paramHttpServletResponse.setHeader("Content-disposition", "filename=" + this.fileName + ".zip");
    ZipOutputStream localZipOutputStream = null;
    BufferedInputStream localBufferedInputStream = null;
    try
    {
      localZipOutputStream = new ZipOutputStream(paramHttpServletResponse.getOutputStream());
      logger.info("fileName:" + this.fileName);
      localZipOutputStream.putNextEntry(new ZipEntry(this.fileName));
      localBufferedInputStream = new BufferedInputStream(paramInputStream);
      int i = 0;
      byte[] arrayOfByte = new byte[bufferSize];
      while ((i = localBufferedInputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1)
        localZipOutputStream.write(arrayOfByte, 0, i);
      localZipOutputStream.flush();
    }
    catch (Exception localException)
    {
      logger.error("[doZippedExport] Exception ", localException);
    }
    finally
    {
      closeStream(localZipOutputStream, localBufferedInputStream);
    }
  }

  private synchronized void doZipExport(HttpServletResponse paramHttpServletResponse, File paramFile, String paramString)
    throws IOException
  {
    paramHttpServletResponse.reset();
    paramHttpServletResponse.setContentType("application/x-zip-compressed; charset=" + paramString);
    paramHttpServletResponse.setHeader("Content-disposition", "filename=" + this.fileName + ".zip");
    ZipOutputStream localZipOutputStream = null;
    try
    {
      localZipOutputStream = new ZipOutputStream(paramHttpServletResponse.getOutputStream());
      zip(localZipOutputStream, paramFile, paramFile.getName());
      localZipOutputStream.flush();
    }
    catch (Exception localException)
    {
      logger.error("[doZippedExport] Exception ", localException);
      localException.printStackTrace();
    }
    finally
    {
      closeStream(localZipOutputStream);
    }
  }

  private void zip(ZipOutputStream paramZipOutputStream, File paramFile, String paramString)
    throws Exception
  {
    logger.debug("Zipping  " + paramFile.getName());
//     localObject;
    int i;
    if (paramFile.isDirectory())
    {
    	File[] localObject = paramFile.listFiles();
      paramZipOutputStream.putNextEntry(new ZipEntry(paramString + '/'));
      paramString = paramString + '/';
      for (i = 0; i < localObject.length; ++i)
        zip(paramZipOutputStream, localObject[i], paramString + localObject[i].getName());
    }
    else
    {
      paramZipOutputStream.putNextEntry(new ZipEntry(paramString));
      FileInputStream localObject = new FileInputStream(paramFile);
      while ((i = ((FileInputStream)localObject).read()) != -1)
        paramZipOutputStream.write(i);
      ((FileInputStream)localObject).close();
    }
  }

  private synchronized void doZippedExport(HttpServletResponse paramHttpServletResponse, byte[] paramArrayOfByte, String paramString)
    throws IOException
  {
    paramHttpServletResponse.reset();
    paramHttpServletResponse.setContentType("application/x-zip-compressed; charset=" + paramString);
    paramHttpServletResponse.setHeader("Content-disposition", "filename=" + this.fileName + ".zip");
    ZipOutputStream localZipOutputStream = null;
    BufferedInputStream localBufferedInputStream = null;
    try
    {
      localZipOutputStream = new ZipOutputStream(paramHttpServletResponse.getOutputStream());
      localZipOutputStream.putNextEntry(new ZipEntry(this.fileName));
      localBufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(paramArrayOfByte));
      int i = 0;
      byte[] arrayOfByte = new byte[bufferSize];
      while ((i = localBufferedInputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1)
        localZipOutputStream.write(arrayOfByte, 0, i);
      localZipOutputStream.flush();
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    finally
    {
      closeStream(localZipOutputStream, localBufferedInputStream);
    }
  }

  private void closeStream(OutputStream paramOutputStream, InputStream paramInputStream)
  {
    try
    {
      if (paramOutputStream != null)
      {
        paramOutputStream.close();
        paramOutputStream = null;
      }
      if (paramInputStream != null)
      {
        paramInputStream.close();
        paramInputStream = null;
      }
    }
    catch (IOException localIOException)
    {
      logger.error("Exception while closing Stream", localIOException);
      localIOException.printStackTrace();
    }
  }

  private void closeStream(OutputStream paramOutputStream)
  {
    try
    {
      if (paramOutputStream != null)
      {
        paramOutputStream.close();
        paramOutputStream = null;
      }
    }
    catch (IOException localIOException)
    {
      logger.error("Exception while closing OutputStream", localIOException);
      localIOException.printStackTrace();
    }
  }

  public String getEncodeType()
  {
    return this.encodeType;
  }

  public void setEncodeType(String paramString)
  {
    this.encodeType = paramString;
  }

  public String getContentTypeName()
  {
    return this.contentTypeName;
  }

  public void setContentTypeName(String paramString)
  {
    this.contentTypeName = paramString;
    this.contentType = ((String)contentTypeMap.get(paramString));
  }

  public String getContentType()
  {
    return this.contentType;
  }

  public void setContentType(String paramString)
  {
    this.contentType = paramString;
  }

  public void setBufferSize(int paramInt)
  {
    bufferSize = paramInt;
  }

  public static int getBufferSize()
  {
    return bufferSize;
  }

  public void setFileName(String paramString)
  {
    this.fileName = paramString;
  }

  public String getFileName()
  {
    return this.fileName;
  }
}