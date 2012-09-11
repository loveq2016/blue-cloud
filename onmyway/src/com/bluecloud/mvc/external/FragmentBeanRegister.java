/**
 * 
 */
package com.bluecloud.mvc.external;

import java.util.HashMap;
import java.util.Map;

import com.bluecloud.mvc.external.bean.FragmentBean;

/**
 * @author leo
 *2012-9-11
 */
public final class FragmentBeanRegister {
	Map<String, Class<? extends FragmentBean>> beans;

	/**
	 * 
	 */
	public FragmentBeanRegister() {
		beans = new HashMap<String, Class<? extends FragmentBean>>();
	}
	
	/**
	 * 
	 * @param beanName 
	 * @param beanClass
	 */
	public void add(String beanName, Class<? extends FragmentBean> beanClass) {
		if(beanName!=null&&!beanName.trim().equals("")){
			beans.put(beanName, beanClass);
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public FragmentBean find(String name) {
		Class<? extends FragmentBean> beanClass=beans.get(name);
		if(beanClass==null){
			return null;
		}
		FragmentBean bean = null;
		try {
			bean = beanClass.newInstance();
			return bean;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
