/**
 * 
 */
package com.bluecloud.mvc.web.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leo
 *2012-9-11
 */
public class FormData {

	Map<String ,String> formData;
	/**
	 * 
	 */
	public FormData() {
		formData=new HashMap<String ,String>();
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> data() {
		return formData;
	}

}
