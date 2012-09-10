package com.yonyouhealth.uaph.framework.comm.filetemplet.tempfile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.yonyouhealth.uaph.framework.comm.filetemplet.FileTempletException;
import com.yonyouhealth.uaph.framework.comm.util.FileUtils;

public class PropertiesTempFile extends AbsTempFile {
	private Map templetContent;

	public PropertiesTempFile() {
		this.templetContent = new HashMap();
	}

	public Object getTempContent() {
		return this.templetContent;
	}

	public Object getObject() {
		return this;
	}

	private Properties getProperties(String fileName) {
		Properties props = new Properties();
		InputStream is = null;
		try {
			FileUtils ft = new FileUtils(fileName);
			is = ft.getInputStream();
			try {
				if (is != null)
					is.close();
			} catch (IOException ex) {
				throw new FileTempletException("701", ex);
			}
		} catch (Exception ex) {
			throw new FileTempletException("701", ex);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ex) {
				throw new FileTempletException("701", ex);
			}
		}
		return props;
	}

	public void processProperties(Properties props) {
		if (props != null)
			synchronized (this.templetContent) {
				this.templetContent.putAll(props);
			}
	}

	public Class getObjectType() {
		return super.getClass();
	}

	public boolean isSingleton() {
		return true;
	}
}