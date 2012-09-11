/**
 * 
 */
package com.bluecloud.mvc.web.http;

import java.util.HashMap;
import java.util.Map;

import com.bluecloud.mvc.web.data.FormData;

/**
 * @author leo
 *
 */
public final class RequestData {

	private Map<String,FormData> formDatas;
	/**
	 * 
	 */
	protected RequestData() {
	}

	protected void parse(BCWebRequest bcWebRequest) {
		formDatas=new HashMap<String,FormData>();
		
	}

	/**
	 * 
	 * @param beanName
	 * @return
	 */
	public Map<String, String> getFormData(String beanName) {
		FormData fd=formDatas.get(beanName);
		if(fd!=null){
			return fd.data();
		}
		return null;
	}

}
