/**
 * 
 */
package com.bluecloud.core.engine.page;

import java.util.Map;


/**
 * @author leo
 * 
 */
public class PageDepository {

	private static PageDepository pageDepository;

	private Map<String,HtmlPage> htmlPages;
	/**
	 * 
	 */
	private PageDepository() {
	}

	/**
	 * 
	 * @return
	 */
	public static PageDepository getInstance() {
		if (pageDepository == null) {
			pageDepository = new PageDepository();
		}
		return pageDepository;
	}

	/**
	 * 
	 * @param pxmlPath
	 * @return 
	 */
	public HtmlPage getPage(String pxmlPath) {
		HtmlPage htmlPage=htmlPages.get(pxmlPath);
		if(htmlPage!=null){
			
		}else{
			htmlPage=new DefualtHtmlPage();
		}
		return htmlPage;
	}
}
