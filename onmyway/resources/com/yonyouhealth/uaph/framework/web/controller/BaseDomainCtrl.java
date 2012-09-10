package com.yonyouhealth.uaph.framework.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.TempFile;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.print.IPrintEntry;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PdfOutputSetting;
import nc.vo.pub.BusinessException;
import nc.vo.pub.guid.UUIDGenerator;
import nc.vo.pub.pftemplate.SystemplateVO;

import com.yonyouhealth.uaph.framework.comm.exception.CSSBaseCheckedException;
import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.IResData;
import com.yonyouhealth.uaph.framework.web.event.UhwebRes;
import com.yonyouhealth.uaph.framework.web.mvc.UhwebDataSet;

public class BaseDomainCtrl
{
  private IResData res;
  private UhwebDataSet reqDataSet;

  public UhwebDataSet getReqDataSet()
  {
    return this.reqDataSet;
  }

  public void setReqDataSet(UhwebDataSet paramUhwebDataSet)
  {
    this.reqDataSet = paramUhwebDataSet;
  }

  protected IResData getRes()
  {
    if (this.res == null)
      this.res = new UhwebRes();
    return this.res;
  }

  protected IResData delegate(String paramString, IReqData paramIReqData)
    throws CSSBaseCheckedException
  {
//    UhwebReq localUhwebReq1 = (UhwebReq)paramIReqData;
//    String[] arrayOfString = paramString.split("_");
//    localUhwebReq1.setTransactionID(arrayOfString[0]);
//    localUhwebReq1.setMethod(arrayOfString[1]);
//    UhwebReq localUhwebReq2 = localUhwebReq1;
//    IResData localIResData = (IResData)BizDelegate.delegate(localUhwebReq2);
//    return localIResData;
	  return null;
  }

  public HttpServletRequest getHttpReq()
  {
    return ContextAPI.getReq();
  }

  public HttpServletResponse getHttpRes()
  {
    return ContextAPI.getRes();
  }

  public void downLoad(InputStream paramInputStream, String paramString)
  {
    String str = getHttpReq().getHeader("USER-AGENT");
    try
    {
      if (str != null)
        if (-1 != str.indexOf("MSIE"))
          paramString = URLEncoder.encode(paramString, "UTF-8");
        else if (-1 != str.indexOf("Mozilla"))
          paramString = new String(paramString.getBytes("UTF-8"), "ISO8859-1");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
    getHttpRes().setContentType("application/x-download;charset=UTF-8");
    getHttpRes().setHeader("Content-Disposition", "attachment;fileName=\"" + paramString + "\"");
    ServletOutputStream localServletOutputStream = null;
    try
    {
      localServletOutputStream = getHttpRes().getOutputStream();
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    try
    {
      byte[] arrayOfByte = new byte[1024];
      int i = 0;
      while ((i = paramInputStream.read(arrayOfByte)) > 0)
        localServletOutputStream.write(arrayOfByte, 0, i);
      paramInputStream.close();
      localServletOutputStream.flush();
      localServletOutputStream.close();
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
  }
  
  /**
   * 下载Excel
   * 
   * @param wb
   * @param paramString
   */
  public void downLoad(HSSFWorkbook wb, String paramString)
  {
	  String str = getHttpReq().getHeader("USER-AGENT");
	  try
	  {
		  if (str != null)
			  if (-1 != str.indexOf("MSIE"))
				  paramString = URLEncoder.encode(paramString, "UTF-8");
			  else if (-1 != str.indexOf("Mozilla"))
				  paramString = new String(paramString.getBytes("UTF-8"), "ISO8859-1");
	  }
	  catch (UnsupportedEncodingException localUnsupportedEncodingException)
	  {
		  localUnsupportedEncodingException.printStackTrace();
	  }
	  getHttpRes().setContentType("application/x-download;charset=UTF-8");
	  getHttpRes().setHeader("Content-Disposition", "attachment;fileName=\"" + paramString + "\"");
	  ServletOutputStream localServletOutputStream = null;
	  try
	  {
		  localServletOutputStream = getHttpRes().getOutputStream();
	  }
	  catch (Exception localException1)
	  {
		  localException1.printStackTrace();
	  }
	  try
	  {
		  wb.write(localServletOutputStream);
		  localServletOutputStream.flush();
		  localServletOutputStream.close();
	  }
	  catch (Exception localException2)
	  {
		  localException2.printStackTrace();
	  }
  }

  public void downLoad(File paramFile, String paramString)
    throws FileNotFoundException
  {
    downLoad(new FileInputStream(paramFile), paramString);
  }
  
  /**
   * 导出为pdf文件，并提供下载
   * 
   * @param ds
   * @param funNode
   * @param pdfName 
   * @throws BusinessException 
   */
  public void exportPdf(IDataSource ds, String funNode, String pdfName) throws BusinessException {
		IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String condition = "funnode='" + funNode + "' and (pk_corp='@@@@' or pk_corp is null) and templateflag='Y' and tempstyle=3";
		Collection c = qry.retrieveByClause(SystemplateVO.class, condition);
		if (c.size() == 0) {
			throw new BusinessException(String.format("节点[%s]没有设置打印模板！", funNode));
		}
		String fileName = System.getProperty("java.io.tmpdir") + UUIDGenerator.getInstance().generateTimeBasedUUID().toString() + ".pdf";
		File f = null;
		try {
			SystemplateVO[] vos = (SystemplateVO[]) c.toArray(new SystemplateVO[c.size()]);
			
			IPrintEntry prEntity = NCLocator.getInstance().lookup(IPrintEntry.class);
			PdfOutputSetting pos = new PdfOutputSetting();
			pos.setExportDestPath(fileName);
			prEntity.exportPdf(ds , vos[0].getTemplateid(), pos);
			
			f = new File(pos.getExportDestPath());
			downLoad(f, pdfName + ".pdf");
		} catch (FileNotFoundException e) {
			Logger.error(e);
			throw new BusinessException(e);
		} catch (IOException e1) {
			Logger.error(e1);
			throw new BusinessException(e1);
		} finally {
			if (f != null) f.delete();
		}
  }
}