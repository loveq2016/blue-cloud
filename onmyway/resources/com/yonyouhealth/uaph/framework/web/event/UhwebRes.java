package com.yonyouhealth.uaph.framework.web.event;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sun.jdbc.rowset.CachedRowSet;

import com.yonyouhealth.uaph.framework.comm.pool.ThreadLocalManager;
import com.yonyouhealth.uaph.framework.core.event.CSSBaseResponseEvent;
import com.yonyouhealth.uaph.framework.web.comm.CommParas;
import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.mvc.UhwebDataSet;
import com.yonyouhealth.uaph.framework.web.mvc.cachecode.ICacheCodeConfig;

/**
 * 响应信息封装类
 */
public class UhwebRes extends CSSBaseResponseEvent implements IResData {
	private static final long serialVersionUID = 6953677318612572553L;
	private UhwebDataSet resDataSet = new UhwebDataSet();

	public UhwebDataSet getResDataSet() {
		return this.resDataSet;
	}

	public IResData joinRes(IResData paramIResData) {
		UhwebRes localUhwebRes = (UhwebRes) paramIResData;
		UhwebDataSet localUhwebDataSet = localUhwebRes.getResDataSet();
		List localList = localUhwebDataSet.getResDataObject().getJsonDatas();
		getResDataSet().getResDataObject().getJsonDatas().addAll(localList);
		Map localMap = localUhwebRes.getResDataSet().getJstlDataMap();
		getResDataSet().getJstlDataMap().putAll(localMap);
		return this;
	}

	public void addAttr(String paramString, Object paramObject) {
		this.resDataSet.addAttr(paramString, paramObject);
	}

	public void addForm(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addForm(paramString, paramCachedRowSet);
	}

	public void addForm(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addForm(paramString, paramMap);
	}

	public void addForm(String paramString, Object paramObject) {
		this.resDataSet.addForm(paramString, paramObject);
	}

	public void addForm(String paramString, CachedRowSet paramCachedRowSet, ICacheCodeConfig paramICacheCodeConfig) {
		this.resDataSet.addForm(paramString, paramCachedRowSet, paramICacheCodeConfig);
	}

	public void addMultiSelectWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addMultiSelectWithDataName(paramString, paramCachedRowSet);
	}

	public void addMultiSelectWithName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addMultiSelectWithName(paramString, paramCachedRowSet);
	}

	public void addPage(String paramString) {
		this.resDataSet.addPage(paramString);
	}

	public void addRootAttr(String paramString, Object paramObject) {
		this.resDataSet.addRootAttr(paramString, paramObject);
	}

	public void addSelectWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addSelectWithDataName(paramString, paramCachedRowSet);
	}

	public void addSelectWithDataName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addSelectWithDataName(paramString, paramMap);
	}

	public void addSelectWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addSelectWithWidgetName(paramString, paramCachedRowSet);
	}

	public void addSelectWithWidgetName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addSelectWithWidgetName(paramString, paramMap);
	}

	public void addTableMap(String paramString, List<Map<String, Object>> paramList) {
		this.resDataSet.addTableMap(paramString, paramList);
	}

	public void addTable(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addTable(paramString, paramCachedRowSet);
	}

	public void addTable(String paramString, CachedRowSet paramCachedRowSet, ICacheCodeConfig paramICacheCodeConfig) {
		this.resDataSet.addTable(paramString, paramCachedRowSet, paramICacheCodeConfig);
	}

	public void addTable(String paramString, List<?> paramList, String[] paramArrayOfString) {
		this.resDataSet.addTable(paramString, paramList, paramArrayOfString);
	}

	public void addTable(String paramString, List<?> paramList) {
		this.resDataSet.addTable(paramString, paramList, new String[0]);
	}

	public void addTree(String paramString, CachedRowSet paramCachedRowSet, boolean paramBoolean) {
		this.resDataSet.addTree(paramString, paramCachedRowSet, paramBoolean);
	}

	public void addTree(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addTree(paramString, paramCachedRowSet);
	}

	public void addTree(String paramString, List<Map<String, Object>> paramList) {
		this.resDataSet.addTree(paramString, paramList);
	}

	public void addValidator(boolean paramBoolean, String paramString) {
		this.resDataSet.addValidator(paramBoolean, paramString);
	}

	public void addValidator(boolean paramBoolean) {
		this.resDataSet.addValidator(paramBoolean);
	}

	public String getJson() {
		return this.resDataSet.getJson();
	}

	public void setCustomRes(boolean paramBoolean) {
		ThreadLocalManager.add("customRes", Boolean.valueOf(paramBoolean));
	}

	public void addTable(String paramString, CachedRowSet paramCachedRowSet, String[] paramArrayOfString) {
		this.resDataSet.addTable(paramString, paramCachedRowSet, paramArrayOfString);
	}

	public void addMessage(Object paramObject) {
		this.resDataSet.addRootAttr("message", paramObject);
	}

	public void setCusJson(boolean paramBoolean) {
		this.resDataSet.getResDataObject().setCustomJson(paramBoolean);
	}

	public void addJSTL(String paramString, Object paramObject) {
		this.resDataSet.addJSTL(paramString, paramObject);
	}

	public void addCheckBoxWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addCheckBoxWithDataName(paramString, paramCachedRowSet);
	}

	public void addCheckBoxWithDataName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addCheckBoxWithDataName(paramString, paramMap);
	}

	public void addCheckBoxWithDataName(String paramString, Object paramObject) {
		this.resDataSet.addCheckBoxWithDataName(paramString, paramObject);
	}

	public void addCheckBoxWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addCheckBoxWithWidgetName(paramString, paramCachedRowSet);
	}

	public void addCheckBoxWithWidgetName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addCheckBoxWithWidgetName(paramString, paramMap);
	}

	public void addCheckBoxWithWidgetName(String paramString, Object paramObject) {
		this.resDataSet.addCheckBoxWithWidgetName(paramString, paramObject);
	}

	public void addListWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addListWithDataName(paramString, paramCachedRowSet);
	}

	public void addListWithDataName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addListWithDataName(paramString, paramMap);
	}

	public void addSelectWithDataName(String paramString, List<Map<String, Object>> paramList, String[] paramArrayOfString) {
		this.resDataSet.addSelectWithDataName(paramString, paramList, paramArrayOfString);
	}

	public void addListWithDataName(String paramString, Object paramObject) {
		this.resDataSet.addListWithDataName(paramString, paramObject);
	}

	public void addListWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addListWithWidgetName(paramString, paramCachedRowSet);
	}

	public void addListWithWidgetName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addListWithWidgetName(paramString, paramMap);
	}

	public void addSelectWithWidgetName(String paramString, List<Map<String, Object>> paramList,
			String[] paramArrayOfString) {
		this.resDataSet.addSelectWithWidgetName(paramString, paramList, paramArrayOfString);
	}

	public void addListWithWidgetName(String paramString, Object paramObject) {
		this.resDataSet.addListWithWidgetName(paramString, paramObject);
	}

	public void addRadioWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addRadioWithDataName(paramString, paramCachedRowSet);
	}

	public void addRadioWithDataName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addRadioWithDataName(paramString, paramMap);
	}

	public void addRadioWithDataName(String paramString, Object paramObject) {
		this.resDataSet.addRadioWithDataName(paramString, paramObject);
	}

	public void addRadioWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		this.resDataSet.addRadioWithWidgetName(paramString, paramCachedRowSet);
	}

	public void addRadioWithWidgetName(String paramString, Map<String, String> paramMap) {
		this.resDataSet.addRadioWithWidgetName(paramString, paramMap);
	}

	public void addRadioWithWidgetName(String paramString, Object paramObject) {
		this.resDataSet.addRadioWithWidgetName(paramString, paramObject);
	}

	public void addChart(String paramString1, String paramString2) {
		this.resDataSet.addChart(paramString1, paramString2);
	}

	public void addSelectWithCacheData(String paramString1, String paramString2, String[] paramArrayOfString) {
		this.resDataSet.addSelectWithCacheData(paramString1, paramString2, paramArrayOfString);
	}

	public Locale getLocale() {
		HttpServletRequest localHttpServletRequest = ContextAPI.getReq();
		Locale localLocale = localHttpServletRequest.getLocale();
		HttpSession localHttpSession = localHttpServletRequest.getSession();
		Object localObject = localHttpSession.getAttribute(CommParas.CUSTOMER_LOCALE);
		if (localObject != null)
			localLocale = (Locale) Locale.class.cast(localObject);
		if (localLocale == null)
			localLocale = Locale.getDefault();
		return localLocale;
	}
}