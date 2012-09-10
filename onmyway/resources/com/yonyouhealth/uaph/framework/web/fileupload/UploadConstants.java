package com.yonyouhealth.uaph.framework.web.fileupload;

/**
 * �ϴ��ļ���صĳ���
 */
public class UploadConstants {
	public static String FILE_PATH = "E:/uploads/";
	public static String TEMP_FILE_PATH = "temp/";
	public static int BUFFER_SIZE = 4096;
	public static int FILE_MAX_SIZE = 2097152;
	public static final int UPLOAD_MAX_SIZE = 4194304;
	public static String ENCODING = "UTF-8";
	public static final String KEY_UPLOAD_FILE_MAX_SIZE = "uhwebFileMaxSize";
	public static final String KEY_UPLOAD_MAX_SIZE = "uhwebUploadMaxSize";
	
	//[start] ����
	public static final String KET_UPLOAD_FILE_ENCODING = "uhwebFileEncoding";
	public static final String KET_UPLOAD_BUFFER_SIZE = "uhwebUploadBufferSize";
	public static final String KET_UPLOAD_TEMP_FILE_PATH = "uhwebUploadFileTempPath";
	//[end]
}