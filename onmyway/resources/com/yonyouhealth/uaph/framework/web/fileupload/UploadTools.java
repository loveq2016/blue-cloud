package com.yonyouhealth.uaph.framework.web.fileupload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;

public class UploadTools {
	private String tempFilePath;
	private int bufferSize;
	private int fileMaxSize;
	private int uploadMaxSize;
	private String encoding;
	private boolean UserSetting = false;
	private List<FileItem> fileItem = new ArrayList();
	private Map param = new HashMap();
	private FileItem fi;
	protected static final LogWritter logger = LogFactory.getLogger(UploadTools.class);

	public void upload(HttpServletRequest request) throws FileUploadException,
			UnsupportedEncodingException {
		if (!this.UserSetting)
			initField();
		initDirs();
		DiskFileItemFactory localDiskFileItemFactory = new DiskFileItemFactory();
		localDiskFileItemFactory.setSizeThreshold(this.bufferSize);
		localDiskFileItemFactory.setRepository(new File(this.tempFilePath));
		ServletFileUpload localServletFileUpload = new ServletFileUpload(localDiskFileItemFactory);
		localServletFileUpload.setHeaderEncoding(this.encoding);
		localServletFileUpload.setSizeMax(this.uploadMaxSize);
		localServletFileUpload.setFileSizeMax(this.fileMaxSize);
		List localList = localServletFileUpload.parseRequest(request);
		Iterator localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			this.fi = ((FileItem) localIterator.next());
			if (!this.fi.isFormField())
				this.fileItem.add(this.fi);
			this.param.put(this.fi.getFieldName(), this.fi.getString(this.encoding));
		}
	}

	private void initField() {
		this.tempFilePath = UploadConstants.TEMP_FILE_PATH;
		this.bufferSize = UploadConstants.BUFFER_SIZE;
		this.fileMaxSize = UploadConstants.FILE_MAX_SIZE;
		this.encoding = UploadConstants.ENCODING;
		this.uploadMaxSize = 4194304;
	}

	private void initDirs() {
		logger.debug("��ʼ��Ŀ¼�����Ŀ¼�Ƿ���ڡ�\nTempFilePath��" + this.tempFilePath);
		File localFile = new File(this.tempFilePath);
		if (localFile.exists())
			return;
		localFile.mkdirs();
	}

	public void setField(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2) {
		if ((paramString1 == null) || (paramString1.equals("")))
			this.tempFilePath = UploadConstants.TEMP_FILE_PATH;
		else
			this.tempFilePath = paramString1;
		if (paramInt1 <= 0)
			this.bufferSize = UploadConstants.BUFFER_SIZE;
		else
			this.bufferSize = paramInt1;
		if (paramInt2 <= 0)
			this.fileMaxSize = UploadConstants.FILE_MAX_SIZE;
		else
			this.fileMaxSize = paramInt2;
		if (paramInt3 <= 0)
			this.uploadMaxSize = 4194304;
		else
			this.uploadMaxSize = paramInt3;
		if ((paramString2 == null) || (paramString2.equals("")))
			this.encoding = UploadConstants.ENCODING;
		else
			this.encoding = paramString2;
		this.UserSetting = true;
	}

	public void setField(String paramString1, int paramInt1, int paramInt2, String paramString2) {
		if ((paramString1 == null) || (paramString1.equals("")))
			this.tempFilePath = UploadConstants.TEMP_FILE_PATH;
		else
			this.tempFilePath = paramString1;
		if (paramInt1 <= 0)
			this.bufferSize = UploadConstants.BUFFER_SIZE;
		else
			this.bufferSize = paramInt1;
		if (paramInt2 <= 0)
			this.fileMaxSize = UploadConstants.FILE_MAX_SIZE;
		else
			this.fileMaxSize = paramInt2;
		if ((paramString2 == null) || (paramString2.equals("")))
			this.encoding = UploadConstants.ENCODING;
		else
			this.encoding = paramString2;
		this.UserSetting = true;
	}

	public List<FileItem> getFileList() {
		return this.fileItem;
	}

	public String getParam(String paramString) {
		return (String) this.param.get(paramString);
	}

	public void clean() {
		if (!this.fi.isInMemory())
			return;
		this.fi.delete();
	}
}