package com.yonyouhealth.uaph.framework.web.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.core.event.CSSBaseRequestEvent;
import com.yonyouhealth.uaph.framework.persistence.pagination.IPaginationReqEvent;
import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.download.FileExport;
import com.yonyouhealth.uaph.framework.web.fileupload.UploadTools;
import com.yonyouhealth.uaph.framework.web.mvc.UhwebDataSet;
import com.yonyouhealth.uaph.framework.web.mvc.beans.FormBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TableBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TreeBean;

/**
 * 请求信息封装类
 */
public class UhwebReq extends CSSBaseRequestEvent implements IPaginationReqEvent, IReqData {
	protected static final LogWritter log = LogFactory.getLogger(UhwebReq.class);
	private static final long serialVersionUID = 4421299729184631995L;
	private UhwebDataSet reqDataSet;
	private Map<String, Object> mapPageParam = null;
	private Map<String, Object> bizParamMap = new HashMap();
	private UploadTools ut;

	public UhwebReq(String paramString) {
		super(paramString, null);
//		ITicket localITicket = WebSecurityManager.getTicket(new HttpServletRequest[0]);
//		if (localITicket == null)
//			this.sessionID = "debug_ejb_sessionid";
//		else
//			this.sessionID = localITicket.getUserSessionId();
	}

	public UhwebReq(String paramString, UhwebDataSet paramUhwebDataSet) {
		this(paramString);
		this.reqDataSet = paramUhwebDataSet;
		this.mapPageParam = new HashMap();
		this.mapPageParam.put("queryType", paramUhwebDataSet.getAttr("queryType"));
		this.mapPageParam.put("rows", paramUhwebDataSet.getAttr("rows"));
		this.mapPageParam.put("pageNum", paramUhwebDataSet.getAttr("pageNum"));
		this.mapPageParam.put("sortFlag", paramUhwebDataSet.getAttr("sortFlag"));
		this.mapPageParam.put("sortName", paramUhwebDataSet.getAttr("sortName"));
		this.mapPageParam.put("bizParams", paramUhwebDataSet.getAttr("bizParams"));
		this.mapPageParam.put("widgetname", paramUhwebDataSet.getAttr("widgetname"));
	}

	public UhwebDataSet getReqDataSet() {
		return this.reqDataSet;
	}

	public void setReqDataSet(UhwebDataSet paramUhwebDataSet) {
		this.reqDataSet = paramUhwebDataSet;
	}

	public Object getAttr(String paramString) {
		return this.reqDataSet.getAttr(paramString);
	}

	public List<?> getDeleteTable(String paramString, Class<?> paramClass) {
		return this.reqDataSet.getDeleteTable(paramString, paramClass);
	}

	public List<?> getDeleteTable(String paramString) {
		return this.reqDataSet.getDeleteTable(paramString);
	}

	public List<Map<String, String>> getDeleteTableData(String paramString) {
		return this.reqDataSet.getDeleteTableData(paramString);
	}

	public Object getForm(String paramString, Class<?> paramClass) {
		return this.reqDataSet.getForm(paramString, paramClass);
	}

	public Object getForm(String paramString) {
		return this.reqDataSet.getForm(paramString);
	}

	public Map<String, String> getFormData(String paramString) {
		return this.reqDataSet.getFormData(paramString);
	}

	public FormBean getFormMetaData(String paramString) {
		return this.reqDataSet.getFormMetaData(paramString);
	}

	public List<?> getInsertTable(String paramString, Class<?> paramClass) {
		return this.reqDataSet.getInsertTable(paramString, paramClass);
	}

	public List<?> getInsertTable(String paramString) {
		return this.reqDataSet.getInsertTable(paramString);
	}

	public List<Map<String, String>> getInsertTableData(String paramString) {
		return this.reqDataSet.getInsertTableData(paramString);
	}

	public List<Object> getTable(String paramString, Class<?> paramClass) {
		return this.reqDataSet.getTable(paramString, paramClass);
	}

	public List<Object> getTable(String paramString) {
		return this.reqDataSet.getTable(paramString);
	}

	public List<Map<String, String>> getTableData(String paramString) {
		return this.reqDataSet.getTableData(paramString);
	}

	public TableBean getTableMetaData(String paramString) {
		return this.reqDataSet.getTableMetaData(paramString);
	}

	public List<Map<String, Object>> getTree(String paramString) {
		return this.reqDataSet.getTree(paramString);
	}

	public TreeBean getTreeMataData(String paramString) {
		return this.reqDataSet.getTreeMataData(paramString);
	}

	public List<?> getUpdateTable(String paramString, Class<?> paramClass) {
		return this.reqDataSet.getUpdateTable(paramString, paramClass);
	}

	public List<?> getUpdateTable(String paramString) {
		return this.reqDataSet.getUpdateTable(paramString);
	}

	public List<Map<String, String>> getUpdateTableData(String paramString) {
		return this.reqDataSet.getUpdateTableData(paramString);
	}

	public Map<String, Object> getPaginationParams() {
		return this.mapPageParam;
	}

	private void getUploadInstance() {
		this.ut = ((UploadTools) this.reqDataSet.getReqDataObject().getViewData().get("uploadOject"));
		if (this.ut != null)
			return;
		this.ut = new UploadTools();
	}

	public List<FileItem> getUploadList() throws UnsupportedEncodingException, FileUploadException {
		HttpServletRequest localHttpServletRequest = ContextAPI.getReq();
		getUploadInstance();
		this.ut.upload(localHttpServletRequest);
		return this.ut.getFileList();
	}

	public InputStream getUploadStream() throws UnsupportedEncodingException, FileUploadException {
		HttpServletRequest localHttpServletRequest = ContextAPI.getReq();
		getUploadInstance();
		this.ut.upload(localHttpServletRequest);
		List localList = this.ut.getFileList();
		FileItem localFileItem = null;
		if (localList.size() == 1) {
			localFileItem = (FileItem) localList.get(0);
			InputStream localInputStream;
			try {
				localInputStream = localFileItem.getInputStream();
			} catch (IOException localIOException) {
				throw new RuntimeException("获取上传文件输入流错误！");
			}
			return localInputStream;
		}
		return null;
	}

	public void cleanUpload() {
		if (this.ut == null)
			return;
		this.ut.clean();
	}

	public List<FileItem> getUploadList(String paramString1, int paramInt1, int paramInt2, String paramString2)
			throws UnsupportedEncodingException, FileUploadException {
		getUploadInstance();
		this.ut.setField(paramString1, paramInt1, paramInt2, paramString2);
		getUploadList();
		return this.ut.getFileList();
	}

	public InputStream getUploadStream(String paramString1, int paramInt1, int paramInt2, String paramString2)
			throws UnsupportedEncodingException, FileUploadException {
		getUploadInstance();
		this.ut.setField(paramString1, paramInt1, paramInt2, paramString2);
		return getUploadStream();
	}

	public List<FileItem> getUploadList(String paramString1, int paramInt1, int paramInt2, int paramInt3,
			String paramString2) throws UnsupportedEncodingException, FileUploadException {
		getUploadInstance();
		this.ut.setField(paramString1, paramInt1, paramInt2, paramInt3, paramString2);
		getUploadList();
		return this.ut.getFileList();
	}

	public InputStream getUploadStream(String paramString1, int paramInt1, int paramInt2, int paramInt3,
			String paramString2) throws UnsupportedEncodingException, FileUploadException {
		getUploadInstance();
		this.ut.setField(paramString1, paramInt1, paramInt2, paramInt3, paramString2);
		return getUploadStream();
	}

	public void downLoad(String paramString1, String paramString2, HttpServletResponse paramHttpServletResponse,
			String paramString3) throws IOException {
		FileExport localFileExport = new FileExport(paramString1, paramString2);
		localFileExport.exportFile(paramHttpServletResponse, paramString3);
	}

	public void setBizParams(String paramString, Object paramObject) {
		this.bizParamMap.put(paramString, paramObject);
	}

	public void setBizParams(Map<String, Object> paramMap) {
		this.bizParamMap = paramMap;
	}

	public List<?> getNonStatusTable(String paramString, Class<?> paramClass) {
		return this.reqDataSet.getNonStatusTable(paramString, paramClass);
	}

	public List<?> getNonStatusTable(String paramString) {
		return this.reqDataSet.getNonStatusTable(paramString);
	}

	public List<Map<String, String>> getNonStatusTableData(String paramString) {
		return this.reqDataSet.getNonStatusTableData(paramString);
	}

	public Object getBizParam(String paramString) {
		return this.bizParamMap.get(paramString);
	}
}