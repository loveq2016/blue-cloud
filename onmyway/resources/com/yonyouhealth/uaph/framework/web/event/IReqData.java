package com.yonyouhealth.uaph.framework.web.event;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import com.yonyouhealth.uaph.framework.core.event.IRequestEvent;
import com.yonyouhealth.uaph.framework.web.mvc.beans.FormBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TableBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TreeBean;

public abstract interface IReqData extends IRequestEvent
{
  public abstract List<Object> getTable(String paramString, Class<?> paramClass);

  public abstract List<Object> getTable(String paramString);

  public abstract TableBean getTableMetaData(String paramString);

  public abstract List<Map<String, String>> getTableData(String paramString);

  public abstract Map<String, String> getFormData(String paramString);

  public abstract Object getForm(String paramString, Class<?> paramClass);

  public abstract Object getForm(String paramString);

  public abstract FormBean getFormMetaData(String paramString);

  public abstract TreeBean getTreeMataData(String paramString);

  public abstract List<Map<String, Object>> getTree(String paramString);

  public abstract Object getAttr(String paramString);

  public abstract List<Map<String, String>> getInsertTableData(String paramString);

  public abstract List<?> getInsertTable(String paramString, Class<?> paramClass);

  public abstract List<?> getInsertTable(String paramString);

  public abstract List<Map<String, String>> getDeleteTableData(String paramString);

  public abstract List<?> getDeleteTable(String paramString, Class<?> paramClass);

  public abstract List<?> getDeleteTable(String paramString);

  public abstract List<Map<String, String>> getUpdateTableData(String paramString);

  public abstract List<?> getUpdateTable(String paramString, Class<?> paramClass);

  public abstract List<?> getUpdateTable(String paramString);

  public abstract void setBizParams(String paramString, Object paramObject);

  public abstract void setBizParams(Map<String, Object> paramMap);

  public abstract List<FileItem> getUploadList()
    throws UnsupportedEncodingException, FileUploadException;

  public abstract InputStream getUploadStream()
    throws UnsupportedEncodingException, FileUploadException;

  public abstract List<FileItem> getUploadList(String paramString1, int paramInt1, int paramInt2, String paramString2)
    throws UnsupportedEncodingException, FileUploadException;

  public abstract List<FileItem> getUploadList(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2)
    throws UnsupportedEncodingException, FileUploadException;

  public abstract InputStream getUploadStream(String paramString1, int paramInt1, int paramInt2, String paramString2)
    throws UnsupportedEncodingException, FileUploadException;

  public abstract InputStream getUploadStream(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2)
    throws UnsupportedEncodingException, FileUploadException;

  public abstract List<Map<String, String>> getNonStatusTableData(String paramString);

  public abstract List<?> getNonStatusTable(String paramString, Class<?> paramClass);

  public abstract List<?> getNonStatusTable(String paramString);

  public abstract Object getBizParam(String paramString);
}