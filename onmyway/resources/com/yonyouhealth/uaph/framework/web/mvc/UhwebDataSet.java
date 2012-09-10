package com.yonyouhealth.uaph.framework.web.mvc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import sun.jdbc.rowset.CachedRowSet;

import com.yonyouhealth.uaph.framework.comm.codecache.ServerCacheManager;
import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.web.comm.DateUtil;
import com.yonyouhealth.uaph.framework.web.mvc.annotations.CacheCode;
import com.yonyouhealth.uaph.framework.web.mvc.beans.AttrBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.FormBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.FormDataBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.SelectBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TDBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TRBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TableBean;
import com.yonyouhealth.uaph.framework.web.mvc.beans.TreeBean;
import com.yonyouhealth.uaph.framework.web.mvc.cachecode.ICacheCodeConfig;
import com.yonyouhealth.uaph.framework.web.mvc.dataobjects.ReqDataObject;
import com.yonyouhealth.uaph.framework.web.mvc.dataobjects.ResDataObject;
import com.yonyouhealth.uaph.framework.web.mvc.util.DataBuilder;

public class UhwebDataSet implements Serializable {
	private static final long serialVersionUID = 6205843046396208302L;
	private static final String AND_FLAG = "#*^@^*#";
	private ReqDataObject reqData = new ReqDataObject();
	private ResDataObject resData;
	protected static final LogWritter log = LogFactory.getLogger(UhwebDataSet.class);
	private Map<String, Object> jstlDataMap = new HashMap();
	BuildSuperClass build = new BuildSuperClass();
	List<?> cacheCodeList = null;

	public Map<String, Object> getJstlDataMap() {
		return this.jstlDataMap;
	}

	public void setJstlDataMap(Map<String, Object> paramMap) {
		this.jstlDataMap = paramMap;
	}

	public UhwebDataSet(String paramString) {
		log.debug("初始化ReqDataObject。");
		this.reqData = new ReqDataObject();
		log.debug("Request Json String:" + paramString);
		this.reqData = DataBuilder.build(paramString, this.reqData);
	}

	public UhwebDataSet(HttpServletRequest paramHttpServletRequest) {
		log.debug("初始化ReqDataObject。");
		this.reqData = new ReqDataObject();
		String str1 = paramHttpServletRequest.getParameter("postData");
		str1 = (str1 != null) ? str1.replace(AND_FLAG, "&") : str1;
		log.debug("Request Json String:" + str1);
		this.reqData = DataBuilder.build(str1, this.reqData);
		if (str1 != null)
			return;
		String str2 = paramHttpServletRequest.getParameter("ctrl");
		String str3 = paramHttpServletRequest.getParameter("tid");
		if (str2 != null)
			this.reqData.getViewData().put("ctrl", str2);
		if (str3 != null)
			this.reqData.getViewData().put("tid", str3);
		if ((str2 != null) || (str3 != null))
			return;
		throw new RuntimeException("解析提交参数是无法获得有效的ctrl或tid，请检查提交参数中是否存在ctrl或tid！");
	}

	public UhwebDataSet() {
		log.debug("初始化ResDataObject。");
		this.resData = new ResDataObject();
	}

	public ResDataObject getResDataObject() {
		return this.resData;
	}

	public void setResDataObject(ResDataObject paramResDataObject) {
		this.resData = paramResDataObject;
	}

	public ReqDataObject getReqDataObject() {
		return this.reqData;
	}

	public void setReqDataObject(ReqDataObject paramReqDataObject) {
		this.reqData = paramReqDataObject;
	}

	public String getCtrl() {
		Object localObject = this.reqData.getViewData().get("ctrl");
		if (localObject != null) {
			int i = localObject.toString().indexOf("_");
			if (i > 0)
				return localObject.toString().split("_")[0];
		}
		return null;
	}

	public String getCtrl_method() {
		String str = this.reqData.getViewData().get("ctrl").toString();
		if ((str != null) && (str.indexOf("_") != -1))
			return str.split("_")[1];
		return "";
	}

	public String getTid() {
		String str = (String) this.reqData.getViewData().get("tid");
		return str;
	}

	public Object getAttr(String paramString) {
		Object localObject = this.reqData.getViewData().get(paramString);
		if (localObject instanceof AttrBean) {
			AttrBean localAttrBean = (AttrBean) localObject;
			return localAttrBean.getAttrMap().get(paramString);
		}
		return localObject;
	}

	public Map<String, String> getFormData(String paramString) {
		HashMap localHashMap = new HashMap();
		Object localObject1 = this.reqData.getViewData().get(paramString);
		Object localObject2 = new HashMap();
		if (localObject1 instanceof FormBean) {
			FormBean localFormBean = (FormBean) localObject1;
			localObject2 = localFormBean.viewData();
			Iterator localIterator = ((Map) localObject2).entrySet().iterator();
			while (localIterator.hasNext()) {
				Map.Entry localEntry = (Map.Entry) localIterator.next();
				localHashMap.put(localEntry.getKey(), ((FormDataBean) localEntry.getValue()).getValue());
			}
			return localHashMap;
		}
		return (Map<String, String>) null;
	}

	public Object getForm(String paramString, Class<?> paramClass) {
		try {
			Object localObject1 = paramClass.newInstance();
			Map localMap = getFormData(paramString);
			if (localMap == null)
				return null;
			Iterator localIterator = localMap.entrySet().iterator();
			while (localIterator.hasNext()) {
				Map.Entry localEntry = (Map.Entry) localIterator.next();
				Field localField = null;
				localField = this.build.getDeclaredField((String) localEntry.getKey(), paramClass);
				if (localField == null)
					log.debug("类【" + paramClass.getName() + "】中字段【" + (String) localEntry.getKey() + "】不存在！");
				setVoValue(localObject1, localField, (String) localEntry.getValue());
			}
			return localObject1;
		} catch (InstantiationException localInstantiationException) {
			throw new RuntimeException(localInstantiationException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		}
	}
	
	/**
	 * 设置Bean的值
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	private void setVoValue(Object localObject, Field localField, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method localMethod = this.build.getMethod(getSetterName(localField.getName()), localObject.getClass(), new Class[] { localField.getType() });
		if (localMethod == null) {
			//by liumin, 2012.8.7 SuperVO的主键比较特殊，为getPrimaryKey和setPrimaryKey
			if (localObject instanceof SuperVO) {
				SuperVO vo = (SuperVO)localObject;
				if (localField.getName().equals(vo.getPKFieldName())) {
					localMethod = this.build.getMethod("setPrimaryKey", localObject.getClass(), new Class[] {localField.getType()});
				}
			}
			
			if (localMethod == null) {
				localMethod = this.build.getMethod(getSetterName2(localField.getName()), localObject.getClass(), new Class[] { localField.getType() });
			}
			
			if (localMethod == null) {
				throw new RuntimeException(String.format("类【%s】的字段【%s】找不到setter。", localObject.getClass().getName(), localField.getName()));
			}
		}
		Object localObject2 = getDataType(localField.getType(), value);
		localMethod.invoke(localObject, new Object[] { localObject2 });
	}
	
	private Object getVoValue(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String str1 = field.getName();
		String str2 = getGetterName(str1);
		Method localMethod = this.build.getMethod(str2, obj);
		
		//by liumin , 2012.8,7 针对SuperVO的主键的getter进行特殊处理
		if (localMethod == null && obj instanceof SuperVO) {
			SuperVO vo = (SuperVO) obj;
			if (str1.equals(vo.getPKFieldName())) {
				localMethod = this.build.getMethod("getPrimaryKey", obj);
			}
		}
		
		if (localMethod == null) {
			localMethod = build.getMethod(getGetterName2(str1), obj);
		}
		
		if (localMethod == null) {
			throw new RuntimeException(String.format("类【%s】的字段【%s】找不到getter。", obj.getClass().getName(), str1));
		}
		return localMethod.invoke(obj, new Object[0]);
		
	}

	/**
	 * 针对以m_开始的field，如m_lang，则其setter为setLang()
	 * 
	 * @param paramString
	 * @return
	 */
	private String getSetterName2(String paramString) {
		if (paramString.startsWith("m_")) paramString = paramString.substring(2);
		else if (paramString.startsWith("_str")) paramString = paramString.substring(4);
		
		return getSetterName(paramString);
	}

	/**
	 * 针对以m_开始的field，如m_lang，则其getter为getLang()
	 * 
	 * @param paramString
	 * @return
	 */
	private String getGetterName2(String paramString) {
		if (paramString.startsWith("m_")) paramString = paramString.substring(2);
		else if (paramString.startsWith("_str")) paramString = paramString.substring(4);
		
		return getGetterName(paramString);
	}

	
	public Object getForm(String paramString) {
		FormBean localFormBean = getFormMetaData(paramString);
		if (localFormBean == null)
			return null;
		String str = (String) localFormBean.getParams().get("beanname");
		if ((str == null) || ("".equals(str)))
			throw new RuntimeException("未获得有效的BeanName，请检查是否将BeanName传入。\n" + this.reqData.getViewData());
		try {
			Class localClass = Class.forName(str);
			return getForm(paramString, localClass);
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new RuntimeException(localClassNotFoundException);
		}
	}

	public FormBean getFormMetaData(String paramString) {
		Object localObject = this.reqData.getViewData().get(paramString);
		if (localObject instanceof FormBean)
			return (FormBean) localObject;
		log.debug("提交的数据中，未能获取有效的Form数据。FormName=" + paramString + "，请检查名称是否有误！\n" + this.reqData.getViewData());
		return null;
	}

	public List<Map<String, String>> getTableData(String paramString) {
		Object localObject = this.reqData.getViewData().get(paramString);
		if (localObject == null) {
			log.debug("为获取到有效的UhwebGrid数据！请检查UhwebGrid名称！TableName=" + paramString);
			return new ArrayList();
		}
		ArrayList localArrayList = new ArrayList();
		if (localObject instanceof TableBean) {
			TableBean localTableBean = (TableBean) localObject;
			List localList1 = localTableBean.getTrList();
			for (int i = 0; i < localList1.size(); ++i) {
				TRBean localTRBean = (TRBean) localList1.get(i);
				HashMap localHashMap = new HashMap();
				List localList2 = localTRBean.getTdList();
				for (int j = 0; j < localList2.size(); ++j) {
					TDBean localTDBean = (TDBean) localList2.get(j);
					String str = localTDBean.getValue();
					if (str == null)
						str = "";
					if ((localTDBean.getCode() != null) && (!localTDBean.getCode().equals("")))
						localHashMap.put(localTDBean.getKey(), localTDBean.getCode());
					else
						localHashMap.put(localTDBean.getKey(), str);
				}
				localArrayList.add(localHashMap);
			}
			return localArrayList;
		}
		return new ArrayList();
	}

	public List<Object> getTable(String paramString, Class<?> paramClass) {
		ArrayList localArrayList = new ArrayList();
		try {
			List localList = getTableData(paramString);
			for (int i = 0; i < localList.size(); ++i) {
				Object localObject1 = paramClass.newInstance();
				Map localMap = (Map) localList.get(i);
				Iterator localIterator = localMap.entrySet().iterator();
				while (localIterator.hasNext()) {
					Map.Entry localEntry = (Map.Entry) localIterator.next();
					Field localField = null;
					localField = this.build.getDeclaredField((String) localEntry.getKey(), paramClass);
					if (localField == null)
						log.debug("类【" + paramClass.getName() + "】中字段【" + (String) localEntry.getKey() + "】不存在！");
					
					setVoValue(localObject1, localField, (String) localEntry.getValue());
				}
				localArrayList.add(localObject1);
			}
			return localArrayList;
		} catch (InstantiationException localInstantiationException) {
			throw new RuntimeException(localInstantiationException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		}
	}

	public List<Object> getTable(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		String str = (String) localTableBean.getParams().get("beanname");
		if ((str == null) || ("".equals(str)))
			throw new RuntimeException("未获得有效的BeanName，请检查是否将BeanName传入。\n" + this.reqData.getViewData());
		try {
			Class localClass = Class.forName(str);
			return getTable(paramString, localClass);
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new RuntimeException(localClassNotFoundException);
		}
	}

	public TableBean getTableMetaData(String paramString) {
		Object localObject = this.reqData.getViewData().get(paramString);
		if (localObject instanceof TableBean)
			return (TableBean) localObject;
		log.debug("提交的数据中，未能获取有效的Table数据。TableName=" + paramString + "，请检查名称是否无误！\n");
		return new TableBean();
	}

	private List<Map<String, String>> getTrListMapForStatus(List<TRBean> paramList) {
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < paramList.size(); ++i) {
			TRBean localTRBean = (TRBean) paramList.get(i);
			HashMap localHashMap = new HashMap();
			List localList = localTRBean.getTdList();
			for (int j = 0; j < localList.size(); ++j) {
				TDBean localTDBean = (TDBean) localList.get(j);
				String str = localTDBean.getValue();
				if (str == null)
					str = "";
				if ((localTDBean.getCode() != null) && (!localTDBean.getCode().equals("")))
					localHashMap.put(localTDBean.getKey(), localTDBean.getCode());
				else
					localHashMap.put(localTDBean.getKey(), str);
			}
			localArrayList.add(localHashMap);
		}
		return localArrayList;
	}

	public List<Map<String, String>> getInsertTableData(String paramString) {
		return getTrListMapForStatus(getInsertTRBean(paramString));
	}

	private List<TRBean> getInsertTRBean(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		List localList = localTableBean.getTrList();
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < localList.size(); ++i) {
			TRBean localTRBean = (TRBean) localList.get(i);
			if (!localTRBean.getOptFlag().equals("insert"))
				continue;
			localArrayList.add(localTRBean);
		}
		return localArrayList;
	}

	public List<?> getInsertTable(String paramString, Class<?> paramClass) {
		ArrayList localArrayList = new ArrayList();
		try {
			List localList1 = getInsertTRBean(paramString);
			for (int i = 0; i < localList1.size(); ++i) {
				TRBean localTRBean = (TRBean) localList1.get(i);
				List localList2 = localTRBean.getTdList();
				Object localObject = fillTable(paramClass, localList2);
				localArrayList.add(localObject);
			}
			return localArrayList;
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new RuntimeException(localNoSuchMethodException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		} catch (InstantiationException localInstantiationException) {
			throw new RuntimeException(localInstantiationException);
		}
	}

	public List<?> getInsertTable(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		String str = (String) localTableBean.getParams().get("beanname");
		if ((str == null) || ("".equals(str))) {
			log.debug("BeanName为空，请确认是否传入beanname属性。");
			return new ArrayList();
		}
		try {
			return getInsertTable(paramString, Class.forName(str));
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new RuntimeException(localClassNotFoundException);
		}
	}

	public List<Map<String, String>> getNonStatusTableData(String paramString) {
		return getTrListMapForStatus(getNonStatusTRBean(paramString));
	}

	private List<TRBean> getNonStatusTRBean(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		List localList = localTableBean.getTrList();
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < localList.size(); ++i) {
			TRBean localTRBean = (TRBean) localList.get(i);
			if ((!localTRBean.getOptFlag().equals("null")) && (!localTRBean.getOptFlag().equals("")) && (localTRBean.getOptFlag() != null))
				continue;
			localArrayList.add(localTRBean);
		}
		return localArrayList;
	}

	public List<?> getNonStatusTable(String paramString, Class<?> paramClass) {
		ArrayList localArrayList = new ArrayList();
		try {
			List localList1 = getNonStatusTRBean(paramString);
			for (int i = 0; i < localList1.size(); ++i) {
				TRBean localTRBean = (TRBean) localList1.get(i);
				List localList2 = localTRBean.getTdList();
				Object localObject = fillTable(paramClass, localList2);
				localArrayList.add(localObject);
			}
			return localArrayList;
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new RuntimeException(localNoSuchMethodException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		} catch (InstantiationException localInstantiationException) {
			throw new RuntimeException(localInstantiationException);
		}
	}

	public List<?> getNonStatusTable(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		String str = (String) localTableBean.getParams().get("beanname");
		if ((str == null) || ("".equals(str)))
			throw new RuntimeException("BeanName为空，请确认是否传入beanname属性。");
		try {
			return getNonStatusTable(paramString, Class.forName(str));
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new RuntimeException(localClassNotFoundException);
		}
	}

	public List<Map<String, String>> getDeleteTableData(String paramString) {
		return getTrListMapForStatus(getDeleteTRBeans(paramString));
	}

	private List<TRBean> getDeleteTRBeans(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		List localList = localTableBean.getTrList();
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < localList.size(); ++i) {
			TRBean localTRBean = (TRBean) localList.get(i);
			if (!localTRBean.getOptFlag().equals("delete"))
				continue;
			localArrayList.add(localTRBean);
		}
		return localArrayList;
	}

	private Object fillTable(Class<?> paramClass, List<TDBean> paramList) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Object localObject1 = paramClass.newInstance();
		for (int i = 0; i < paramList.size(); ++i) {
			TDBean localTDBean = (TDBean) paramList.get(i);
			Field localField = this.build.getDeclaredField(localTDBean.getKey(), paramClass);
			if (localField == null)
				continue;
			if ((localTDBean.getCode() != null) && (!"".equals(localTDBean.getCode())))
				setVoValue(localObject1, localField, localTDBean.getCode());
			else
				setVoValue(localObject1, localField, localTDBean.getValue());
		}
		return localObject1;
	}

	public List<?> getDeleteTable(String paramString, Class<?> paramClass) {
		ArrayList localArrayList = new ArrayList();
		try {
			List localList1 = getDeleteTRBeans(paramString);
			for (int i = 0; i < localList1.size(); ++i) {
				TRBean localTRBean = (TRBean) localList1.get(i);
				List localList2 = localTRBean.getTdList();
				Object localObject = fillTable(paramClass, localList2);
				localArrayList.add(localObject);
			}
			return localArrayList;
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new RuntimeException(localNoSuchMethodException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		} catch (InstantiationException localInstantiationException) {
			throw new RuntimeException(localInstantiationException);
		}
	}

	public List<?> getDeleteTable(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		String str = (String) localTableBean.getParams().get("beanname");
		if ((str == null) || ("".equals(str))) {
			log.debug("BeanName为空，请确认是否传入beanname属性。");
			return new ArrayList();
		}
		try {
			return getDeleteTable(paramString, Class.forName(str));
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new RuntimeException(localClassNotFoundException);
		}
	}

	public List<Map<String, String>> getUpdateTableData(String paramString) {
		return getTrListMapForStatus(getUpdateTRBeans(paramString));
	}

	private List<TRBean> getUpdateTRBeans(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		List localList = localTableBean.getTrList();
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < localList.size(); ++i) {
			TRBean localTRBean = (TRBean) localList.get(i);
			if (!localTRBean.getOptFlag().equals("update"))
				continue;
			localArrayList.add(localTRBean);
		}
		return localArrayList;
	}

	public List<?> getUpdateTable(String paramString, Class<?> paramClass) {
		ArrayList localArrayList = new ArrayList();
		try {
			List localList1 = getUpdateTRBeans(paramString);
			for (int i = 0; i < localList1.size(); ++i) {
				TRBean localTRBean = (TRBean) localList1.get(i);
				List localList2 = localTRBean.getTdList();
				Object localObject = fillTable(paramClass, localList2);
				localArrayList.add(localObject);
			}
			return localArrayList;
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new RuntimeException(localNoSuchMethodException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		} catch (InstantiationException localInstantiationException) {
			throw new RuntimeException(localInstantiationException);
		}
	}

	public List<?> getUpdateTable(String paramString) {
		TableBean localTableBean = getTableMetaData(paramString);
		String str = (String) localTableBean.getParams().get("beanname");
		if ((str == null) || ("".equals(str))) {
			log.debug("BeanName为空，请确认是否传入beanname属性。");
			return new ArrayList();
		}
		try {
			return getUpdateTable(paramString, Class.forName(str));
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new RuntimeException(localClassNotFoundException);
		}
	}

	public TreeBean getTreeMataData(String paramString) {
		Object localObject = this.reqData.getViewData().get(paramString);
		if (localObject instanceof TreeBean)
			return (TreeBean) localObject;
		return null;
	}

	public List<Map<String, Object>> getTree(String paramString) {
		TreeBean localTreeBean = getTreeMataData(paramString);
		return localTreeBean.getDataList();
	}

	public void addTableMap(String paramString, List<Map<String, Object>> paramList) {
		HashMap localHashMap = new HashMap();
		TableBean localTableBean = new TableBean();
		localTableBean.setTableName(paramString);
		ArrayList localArrayList1 = new ArrayList();
		if ((paramList != null) && (paramList.size() > 0)) {
			Iterator localIterator1 = paramList.iterator();
			while (localIterator1.hasNext()) {
				TRBean localTRBean = new TRBean();
				Map localMap = (Map) localIterator1.next();
				ArrayList localArrayList2 = new ArrayList();
				Iterator localIterator2 = localMap.entrySet().iterator();
				while (localIterator2.hasNext()) {
					Map.Entry localEntry = (Map.Entry) localIterator2.next();
					TDBean localTDBean = new TDBean();
					localTDBean.setKey((String) localEntry.getKey());
					localTDBean.setValue((localEntry.getValue() == null) ? "" : localEntry.getValue().toString());
					localArrayList2.add(localTDBean);
				}
				localTRBean.setTdList(localArrayList2);
				localArrayList1.add(localTRBean);
			}
		}
		localTableBean.setTrList(localArrayList1);
		localHashMap.put(paramString, localArrayList1);
		localTableBean.setViewData(localHashMap);
		this.resData.setObjectBean(localTableBean);
		this.resData = DataBuilder.tableBeanToJson(this.resData);
	}

	public void addTable(String paramString, CachedRowSet paramCachedRowSet) {
		log.debug("共" + paramCachedRowSet.size() + "条数据需要处理！");
		try {
			if (!paramCachedRowSet.isBeforeFirst())
				paramCachedRowSet.beforeFirst();
			HashMap localHashMap = new HashMap();
			TableBean localTableBean = new TableBean();
			localTableBean.setTableName(paramString);
			ArrayList localArrayList1 = new ArrayList();
			int i = paramCachedRowSet.getMetaData().getColumnCount();
			while (paramCachedRowSet.next()) {
				TRBean localTRBean = new TRBean();
				ArrayList localArrayList2 = new ArrayList();
				for (int j = 0; j < i; ++j) {
					TDBean localTDBean = new TDBean();
					localTDBean.setKey(paramCachedRowSet.getMetaData().getColumnLabel(j + 1));
					String str = DateUtil.dateToStr(paramCachedRowSet.getObject(j + 1));
					if (str == null)
						str = "";
					localTDBean.setValue(str);
					localArrayList2.add(localTDBean);
				}
				localTRBean.setTdList(localArrayList2);
				localArrayList1.add(localTRBean);
			}
			localTableBean.setTrList(localArrayList1);
			localHashMap.put(paramString, localArrayList1);
			localTableBean.setViewData(localHashMap);
			this.resData.setObjectBean(localTableBean);
			this.resData = DataBuilder.tableBeanToJson(this.resData);
		} catch (SQLException localSQLException) {
			throw new RuntimeException(localSQLException);
		}
	}

	public void addTable(String paramString, CachedRowSet paramCachedRowSet, ICacheCodeConfig paramICacheCodeConfig) {
		log.debug("共" + paramCachedRowSet.size() + "条数据需要处理！");
		try {
			HashMap localHashMap = new HashMap();
			TableBean localTableBean = new TableBean();
			localTableBean.setTableName(paramString);
			ArrayList localArrayList1 = new ArrayList();
			int i = paramCachedRowSet.getMetaData().getColumnCount();
			while (paramCachedRowSet.next()) {
				TRBean localTRBean = new TRBean();
				ArrayList localArrayList2 = new ArrayList();
				for (int j = 0; j < i; ++j) {
					TDBean localTDBean = new TDBean();
					localTDBean.setKey(paramCachedRowSet.getMetaData().getColumnLabel(j + 1));
					String str1 = DateUtil.dateToStr(paramCachedRowSet.getObject(j + 1));
					CacheCode localCacheCode = getCacheCode(paramICacheCodeConfig, "set");
					String str2 = "";
					if (localCacheCode != null) {
						str2 = localCacheCode.codeRowName();
						if (str2.equalsIgnoreCase(paramCachedRowSet.getMetaData().getColumnLabel(j + 1))) {
							localTDBean.setValue(getCacheCode(str1, localCacheCode));
							localTDBean.setCode(str1);
						} else {
							localTDBean.setValue(str1 + "");
						}
					} else {
						List localList = paramICacheCodeConfig.getAllSet();
						if ((localList == null) || (localList.size() <= 0)) {
							localTDBean.setValue(str1 + "");
						} else {
							for (int k = 0; k < localList.size(); ++k) {
								Map localMap = (Map) localList.get(k);
								if (!((String) localMap.get("codeRowName")).equalsIgnoreCase(paramCachedRowSet.getMetaData().getColumnLabel(j + 1)))
									continue;
								localTDBean.setCode(str1);
								str1 = getCacheCodeFromMap(localMap, str1);
							}
							localTDBean.setValue(str1);
						}
					}
					localArrayList2.add(localTDBean);
				}
				localTRBean.setTdList(localArrayList2);
				localArrayList1.add(localTRBean);
			}
			localTableBean.setTrList(localArrayList1);
			localHashMap.put(paramString, localArrayList1);
			localTableBean.setViewData(localHashMap);
			this.resData.setObjectBean(localTableBean);
			this.resData = DataBuilder.tableBeanToJson(this.resData);
		} catch (SQLException localSQLException) {
			throw new RuntimeException(localSQLException);
		}
	}

	public void addTable(String paramString, CachedRowSet paramCachedRowSet, String[] paramArrayOfString) {
		log.debug("共" + paramCachedRowSet.size() + "条数据需要处理！");
		try {
			HashMap localHashMap = new HashMap();
			TableBean localTableBean = new TableBean();
			localTableBean.setTableName(paramString);
			ArrayList localArrayList1 = new ArrayList();
			int i = paramCachedRowSet.getMetaData().getColumnCount();
			List localList = getCacheCodeConfig(paramArrayOfString);
			while (paramCachedRowSet.next()) {
				TRBean localTRBean = new TRBean();
				ArrayList localArrayList2 = new ArrayList();
				for (int j = 0; j < i; ++j) {
					TDBean localTDBean = new TDBean();
					localTDBean.setKey(paramCachedRowSet.getMetaData().getColumnLabel(j + 1));
					String str = DateUtil.dateToStr(paramCachedRowSet.getObject(j + 1));
					if ((localList == null) || (localList.size() <= 0)) {
						localTDBean.setValue(str + "");
					} else {
						for (int k = 0; k < localList.size(); ++k) {
							Map localMap = (Map) localList.get(k);
							if (!((String) localMap.get("codeRowName")).equalsIgnoreCase(paramCachedRowSet.getMetaData().getColumnLabel(j + 1)))
								continue;
							localTDBean.setCode(str);
							str = getCacheCodeFromMap(localMap, str);
						}
						localTDBean.setValue(str);
					}
					localArrayList2.add(localTDBean);
				}
				localTRBean.setTdList(localArrayList2);
				localArrayList1.add(localTRBean);
			}
			localTableBean.setTrList(localArrayList1);
			localHashMap.put(paramString, localArrayList1);
			localTableBean.setViewData(localHashMap);
			this.resData.setObjectBean(localTableBean);
			this.resData = DataBuilder.tableBeanToJson(this.resData);
		} catch (SQLException localSQLException) {
			throw new RuntimeException(localSQLException);
		}
	}

	public void addTable(String paramString, List<?> paramList, String[] paramArrayOfString) {
		try {
			HashMap localHashMap = new HashMap();
			TableBean localTableBean = new TableBean();
			localTableBean.setTableName(paramString);
			ArrayList localArrayList1 = new ArrayList();
			List localList = (paramArrayOfString.length == 0) ? new ArrayList() : getCacheCodeConfig(paramArrayOfString);
			for (int i = 0; i < paramList.size(); ++i) {
				ArrayList localArrayList2 = new ArrayList();
				Object localObject1 = paramList.get(i);
				TRBean localTRBean = new TRBean();
				Field[] arrayOfField = this.build.getAllFields(localObject1);
				for (int j = 0; j < arrayOfField.length; ++j) {
					// by liumin, 2012.5.22 Static的字段不需要处理
					if (Modifier.isStatic(arrayOfField[j].getModifiers()))
						continue;

					String str1 = arrayOfField[j].getName();
					String str2 = getGetterName(str1);
					
					TDBean localTDBean = new TDBean();
					localTDBean.setKey(str1);
					Object localObject2 = getVoValue(localObject1, arrayOfField[j]);
					if (localObject2 == null)
						localObject2 = new String("");
					else if (arrayOfField[j].getType().getName().equals(Calendar.class.getName()))
						localObject2 = DateUtil.dateToStr(new Timestamp(((Calendar) localObject2).getTimeInMillis()));
					CacheCode localCacheCode = getCacheCode(localObject1, str2);
					if (localCacheCode != null) {
						String localObject3 = localCacheCode.codeRowName();
						if (((String) localObject3).equalsIgnoreCase(str1)) {
							localTDBean.setCode("" + localObject2);
							localObject2 = getCacheCode(localObject2, localCacheCode);
						}
					}
					Object localObject3 = localList.iterator();
					while (((Iterator) localObject3).hasNext()) {
						Map localMap = (Map) ((Iterator) localObject3).next();
						if (((String) localMap.get("codeRowName")).equalsIgnoreCase(str1)) {
							String str3 = "" + localObject2;
							localTDBean.setCode(str3);
							localObject2 = getCacheCodeFromMap(localMap, str3);
						}
					}
					localTDBean.setValue(localObject2 + "");
					localArrayList2.add(localTDBean);
				}
				localTRBean.setTdList(localArrayList2);
				localArrayList1.add(localTRBean);
			}
			localTableBean.setTrList(localArrayList1);
			localHashMap.put(paramString, localArrayList1);
			localTableBean.setViewData(localHashMap);
			this.resData.setObjectBean(localTableBean);
			this.resData = DataBuilder.tableBeanToJson(this.resData);
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		}
	}

	public void addForm(String paramString, CachedRowSet paramCachedRowSet, ICacheCodeConfig paramICacheCodeConfig) {
		try {
			FormBean localFormBean = new FormBean();
			int i = paramCachedRowSet.getMetaData().getColumnCount();
			HashMap localHashMap1 = new HashMap();
			localHashMap1.put("name", paramString);
			HashMap localHashMap2 = new HashMap();
			CacheCode localCacheCode = getCacheCode(paramICacheCodeConfig, "set");
			while (paramCachedRowSet.next())
				for (int j = 0; j < i; ++j) {
					FormDataBean localFormDataBean = new FormDataBean();
					String str = DateUtil.dateToStr(paramCachedRowSet.getObject(j + 1));
					HashMap localHashMap3 = new HashMap();
					Object localObject;
					if (localCacheCode != null) {
						localObject = localCacheCode.codeRowName();
						if (((String) localObject).equalsIgnoreCase(paramCachedRowSet.getMetaData().getColumnLabel(j + 1))) {
							localFormDataBean.setValue(getCacheCode(str, localCacheCode));
							localFormDataBean.setCode(str);
						} else {
							localFormDataBean.setValue(str);
						}
					} else {
						localObject = paramICacheCodeConfig.getAllSet();
						if ((localObject == null) || (((List) localObject).size() <= 0)) {
							if (str == null)
								str = "";
							localFormDataBean.setValue(str);
						} else {
							for (int k = 0; k < ((List) localObject).size(); ++k) {
								Map localMap = (Map) ((List) localObject).get(k);
								if (!((String) localMap.get("codeRowName")).equalsIgnoreCase(paramCachedRowSet.getMetaData().getColumnLabel(j + 1)))
									continue;
								localFormDataBean.setCode(str);
								str = getCacheCodeFromMap(localMap, str);
							}
							localFormDataBean.setValue(str);
						}
					}
					localHashMap3.put("value", localFormDataBean.getValue());
					localFormDataBean.setParams(localHashMap3);
					localHashMap2.put(paramCachedRowSet.getMetaData().getColumnLabel(j + 1), localFormDataBean);
				}
			localFormBean.setParams(localHashMap1);
			localFormBean.setViewData(localHashMap2);
			this.resData.setObjectBean(localFormBean);
			this.resData = DataBuilder.formBeanToJson(this.resData);
		} catch (SQLException localSQLException) {
			throw new RuntimeException(localSQLException);
		}
	}

	public void addForm(String paramString, CachedRowSet paramCachedRowSet, String[] paramArrayOfString) {
		try {
			FormBean localFormBean = new FormBean();
			int i = paramCachedRowSet.getMetaData().getColumnCount();
			HashMap localHashMap1 = new HashMap();
			localHashMap1.put("name", paramString);
			HashMap localHashMap2 = new HashMap();
			while (paramCachedRowSet.next())
				for (int j = 0; j < i; ++j) {
					FormDataBean localFormDataBean = new FormDataBean();
					String str = DateUtil.dateToStr(paramCachedRowSet.getObject(j + 1));
					HashMap localHashMap3 = new HashMap();
					List localList = getCacheCodeConfig(paramArrayOfString);
					if ((localList == null) || (localList.size() <= 0)) {
						if (str == null)
							str = "";
						localFormDataBean.setValue(str);
					} else {
						for (int k = 0; k < localList.size(); ++k) {
							Map localMap = (Map) localList.get(k);
							if (!((String) localMap.get("codeRowName")).equalsIgnoreCase(paramCachedRowSet.getMetaData().getColumnLabel(j + 1)))
								continue;
							localFormDataBean.setCode(str);
							str = getCacheCodeFromMap(localMap, str);
						}
						localFormDataBean.setValue(str);
					}
					localHashMap3.put("value", localFormDataBean.getValue());
					localFormDataBean.setParams(localHashMap3);
					localHashMap2.put(paramCachedRowSet.getMetaData().getColumnLabel(j + 1), localFormDataBean);
				}
			localFormBean.setParams(localHashMap1);
			localFormBean.setViewData(localHashMap2);
			this.resData.setObjectBean(localFormBean);
			this.resData = DataBuilder.formBeanToJson(this.resData);
		} catch (SQLException localSQLException) {
			throw new RuntimeException(localSQLException);
		}
	}

	public void addForm(String paramString, CachedRowSet paramCachedRowSet) {
		try {
			FormBean localFormBean = new FormBean();
			int i = paramCachedRowSet.getMetaData().getColumnCount();
			HashMap localHashMap1 = new HashMap();
			localHashMap1.put("name", paramString);
			HashMap localHashMap2 = new HashMap();
			while (paramCachedRowSet.next())
				for (int j = 0; j < i; ++j) {
					FormDataBean localFormDataBean = new FormDataBean();
					HashMap localHashMap3 = new HashMap();
					localFormDataBean.setValue(paramCachedRowSet.getString(j + 1));
					localHashMap3.put("value", paramCachedRowSet.getString(j + 1));
					localFormDataBean.setParams(localHashMap3);
					localHashMap2.put(paramCachedRowSet.getMetaData().getColumnLabel(j + 1), localFormDataBean);
				}
			localFormBean.setParams(localHashMap1);
			localFormBean.setViewData(localHashMap2);
			this.resData.setObjectBean(localFormBean);
			this.resData = DataBuilder.formBeanToJson(this.resData);
		} catch (SQLException localSQLException) {
			throw new RuntimeException(localSQLException);
		}
	}

	public void addForm(String paramString, Map<String, String> paramMap) {
		FormBean localFormBean = new FormBean();
		HashMap localHashMap1 = new HashMap();
		localHashMap1.put("name", paramString);
		HashMap localHashMap2 = new HashMap();
		Iterator localIterator = paramMap.entrySet().iterator();
		while (localIterator.hasNext()) {
			Map.Entry localEntry = (Map.Entry) localIterator.next();
			FormDataBean localFormDataBean = new FormDataBean();
			HashMap localHashMap3 = new HashMap();
			localFormDataBean.setValue("" + localEntry.getValue());
			localHashMap3.put("value", "" + localEntry.getValue());
			localFormDataBean.setParams(localHashMap3);
			localHashMap2.put("" + localEntry.getKey(), localFormDataBean);
		}
		localFormBean.setParams(localHashMap1);
		localFormBean.setViewData(localHashMap2);
		this.resData.setObjectBean(localFormBean);
		this.resData = DataBuilder.formBeanToJson(this.resData);
	}

	public void addForm(String paramString, Object paramObject) {
		try {
			FormBean localFormBean = new FormBean();
			HashMap localHashMap1 = new HashMap();
			localHashMap1.put("name", paramString);
			HashMap localHashMap2 = new HashMap();
			Field[] arrayOfField = this.build.getAllFields(paramObject);
			for (int i = 0; i < arrayOfField.length; ++i) {
				// by liumin, 2012.5.22 Static的字段不需要处理
				if (Modifier.isStatic(arrayOfField[i].getModifiers()))
					continue;

				String str1 = arrayOfField[i].getName();
				String str2 = getGetterName(str1);

				Object localObject = getVoValue(paramObject, arrayOfField[i]);
				if (localObject == null)
					localObject = new String("");
				else if (arrayOfField[i].getType().getName().equals(Calendar.class.getName()))
					localObject = DateUtil.dateToStr(new Timestamp(((Calendar) localObject).getTimeInMillis()));
				CacheCode localCacheCode = getCacheCode(paramObject, str2);
				FormDataBean localFormDataBean = new FormDataBean();
				HashMap localHashMap3 = new HashMap();
				if (localCacheCode != null) {
					String str3 = localCacheCode.codeRowName();
					if (str3.equalsIgnoreCase(str1)) {
						localFormDataBean.setValue(getCacheCode(localObject, localCacheCode));
						localFormDataBean.setCode(localObject.toString());
					} else {
						localFormDataBean.setValue(localObject.toString());
					}
				} else {
					localFormDataBean.setValue(localObject.toString());
				}
				localHashMap3.put("value", localFormDataBean.getValue());
				localFormDataBean.setParams(localHashMap3);
				localHashMap2.put(str1, localFormDataBean);
			}
			localFormBean.setParams(localHashMap1);
			localFormBean.setViewData(localHashMap2);
			this.resData.setObjectBean(localFormBean);
			this.resData = DataBuilder.formBeanToJson(this.resData);
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		}
	}

	public void addAttr(String paramString, Object paramObject) {
		AttrBean localAttrBean = new AttrBean();
		HashMap localHashMap = new HashMap();
		localHashMap.put(paramString, paramObject.toString());
		localAttrBean.setAttrMap(localHashMap);
		this.resData.setObjectBean(localAttrBean);
		this.resData = DataBuilder.attrBeanToJson(this.resData);
	}

	private void addSelect(Map<String, Object> paramMap, CachedRowSet paramCachedRowSet, String paramString) {
		try {
			paramMap.put("uhweb", paramString);
			int i = paramCachedRowSet.getMetaData().getColumnCount();
			SelectBean localSelectBean = new SelectBean();
			while (paramCachedRowSet.next()) {
				HashMap localHashMap = new HashMap();
				for (int j = 0; j < i; ++j)
					localHashMap.put(paramCachedRowSet.getMetaData().getColumnLabel(j + 1).toLowerCase(), paramCachedRowSet.getString(j + 1));
				localSelectBean.add(localHashMap);
			}
			localSelectBean.setViewData(paramMap);
			this.resData.setObjectBean(localSelectBean);
			this.resData = DataBuilder.selectBeanToJson(this.resData);
		} catch (SQLException localSQLException) {
			throw new RuntimeException(localSQLException);
		}
	}

	private void addSelect(Map<String, Object> paramMap, List<Map<String, Object>> paramList, String paramString, String[] paramArrayOfString) {
		paramMap.put("uhweb", paramString);
		SelectBean localSelectBean = new SelectBean();
		List localList = getConfigListMap(paramArrayOfString);
		Iterator localIterator1 = paramList.iterator();
		while (localIterator1.hasNext()) {
			Map localMap1 = (Map) localIterator1.next();
			HashMap localHashMap = new HashMap();
			Iterator localIterator2 = localMap1.entrySet().iterator();
			while (localIterator2.hasNext()) {
				Map.Entry localEntry = (Map.Entry) localIterator2.next();
				String str = ((String) localEntry.getKey()).toLowerCase();
				Iterator localIterator3 = localList.iterator();
				while (localIterator3.hasNext()) {
					Map localMap2 = (Map) localIterator3.next();
					if (localMap2.containsKey(str))
						str = (String) localMap2.get(str);
				}
				localHashMap.put(str, "" + localEntry.getValue());
			}
			localSelectBean.add(localHashMap);
		}
		localSelectBean.setViewData(paramMap);
		this.resData.setObjectBean(localSelectBean);
		this.resData = DataBuilder.selectBeanToJson(this.resData);
	}

	private void addSelect(Map<String, Object> paramMap, Map<String, String> paramMap1, String paramString) {
		paramMap.put("uhweb", paramString);
		SelectBean localSelectBean = new SelectBean();
		Iterator localIterator = paramMap1.entrySet().iterator();
		while (localIterator.hasNext()) {
			HashMap localHashMap = new HashMap();
			Map.Entry localEntry = (Map.Entry) localIterator.next();
			localHashMap.put("code", localEntry.getKey());
			localHashMap.put("caption", localEntry.getValue());
			localSelectBean.add(localHashMap);
		}
		localSelectBean.setViewData(paramMap);
		this.resData.setObjectBean(localSelectBean);
		this.resData = DataBuilder.selectBeanToJson(this.resData);
	}

	private void addSelect(Map<String, Object> paramMap, Object paramObject, String paramString) {
		paramMap.put("uhweb", paramString);
		SelectBean localSelectBean = new SelectBean();
		try {
			Field[] arrayOfField = this.build.getAllFields(paramObject);
			for (int i = 0; i < arrayOfField.length; ++i) {
				// by liumin, 2012.5.22 Static的字段不需要处理
				if (Modifier.isStatic(arrayOfField[i].getModifiers()))
					continue;

				HashMap localHashMap = new HashMap();
				String str1 = arrayOfField[i].getName();
				String str2 = getGetterName(str1);
				Method localMethod = this.build.getMethod(str2, paramObject);
				Object localObject = localMethod.invoke(paramObject, new Object[0]);
				localHashMap.put("code", str1);
				localHashMap.put("caption", localObject);
				localSelectBean.add(localHashMap);
			}
			localSelectBean.setViewData(paramMap);
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new RuntimeException(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new RuntimeException(localInvocationTargetException);
		}
		this.resData.setObjectBean(localSelectBean);
		this.resData = DataBuilder.selectBeanToJson(this.resData);
	}

	public void addCacheSelect(Map<String, Object> paramMap, String paramString1, String paramString2, String[] paramArrayOfString) {
		paramMap.put("uhweb", paramString1);
		SelectBean localSelectBean = new SelectBean();
		List localList1 = getConfigListMap(paramArrayOfString);
		ServerCacheManager localServerCacheManager = (ServerCacheManager) ServerCacheManager.getInstance();
		try {
			List localList2 = localServerCacheManager.getCacheData(paramString2);
			if (localList2 == null) {
				log.debug("表" + paramString2 + "没有缓存数据");
				return;
			}
			Iterator localIterator1 = localList2.iterator();
			while (localIterator1.hasNext()) {
				Map localMap1 = (Map) localIterator1.next();
				HashMap localHashMap = new HashMap();
				Iterator localIterator2 = localMap1.entrySet().iterator();
				while (localIterator2.hasNext()) {
					Map.Entry localEntry = (Map.Entry) localIterator2.next();
					Iterator localIterator3 = localList1.iterator();
					while (localIterator3.hasNext()) {
						Map localMap2 = (Map) localIterator3.next();
						if (localMap2.containsKey(((String) localEntry.getKey()).toLowerCase()))
							localHashMap.put(localMap2.get(((String) localEntry.getKey()).toLowerCase()), localEntry.getValue());
					}
				}
				if (localHashMap.size() != 0)
					localSelectBean.add(localHashMap);
			}
			localSelectBean.setViewData(paramMap);
		} catch (Exception localException) {
			log.debug("获取缓存数据失败", localException);
		}
		this.resData.setObjectBean(localSelectBean);
		this.resData = DataBuilder.selectBeanToJson(this.resData);
	}

	private List<Map<String, String>> getConfigListMap(String[] paramArrayOfString) {
		ArrayList localArrayList = new ArrayList();
		for (String str : paramArrayOfString) {
			HashMap localHashMap = new HashMap();
			String[] arrayOfString2 = str.split(",", 0);
			if (arrayOfString2.length != 2)
				throw new RuntimeException("配置信息获取错误，必须为两个参数！传入的参数：" + arrayOfString2.length);
			localHashMap.put(arrayOfString2[0], arrayOfString2[1]);
			localArrayList.add(localHashMap);
		}
		return localArrayList;
	}

	public void addSelectWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebSelect");
	}

	public void addSelectWithCacheData(String paramString1, String paramString2, String[] paramArrayOfString) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString1);
		addCacheSelect(localHashMap, "UhwebSelect", paramString2, paramArrayOfString);
	}

	public void addSelectWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebSelect");
	}

	public void addSelectWithDataName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramMap, "UhwebSelect");
	}

	public void addSelectWithWidgetName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramMap, "UhwebSelect");
	}

	public void addSelectWithDataName(String paramString, List<Map<String, Object>> paramList, String[] paramArrayOfString) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramList, "UhwebSelect", paramArrayOfString);
	}

	public void addSelectWithWidgetName(String paramString, List<Map<String, Object>> paramList, String[] paramArrayOfString) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramList, "UhwebSelect", paramArrayOfString);
	}

	public void addSelectWithDataName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramObject, "UhwebSelect");
	}

	public void addSelectWithWidgetName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramObject, "UhwebSelect");
	}

	public void addRadioWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebRadio");
	}

	public void addRadioWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebRadio");
	}

	public void addRadioWithDataName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramMap, "UhwebRadio");
	}

	public void addRadioWithWidgetName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramMap, "UhwebRadio");
	}

	public void addRadioWithDataName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramObject, "UhwebRadio");
	}

	public void addRadioWithWidgetName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramObject, "UhwebRadio");
	}

	public void addCheckBoxWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebCheckBox");
	}

	public void addCheckBoxWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebCheckBox");
	}

	public void addCheckBoxWithDataName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramMap, "UhwebCheckBox");
	}

	public void addCheckBoxWithWidgetName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramMap, "UhwebCheckBox");
	}

	public void addCheckBoxWithDataName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramObject, "UhwebCheckBox");
	}

	public void addCheckBoxWithWidgetName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramObject, "UhwebCheckBox");
	}

	public void addListWithWidgetName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebList");
	}

	public void addListWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramCachedRowSet, "UhwebList");
	}

	public void addListWithDataName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramMap, "UhwebList");
	}

	public void addListWithWidgetName(String paramString, Map<String, String> paramMap) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramMap, "UhwebList");
	}

	public void addListWithDataName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		addSelect(localHashMap, paramObject, "UhwebList");
	}

	public void addListWithWidgetName(String paramString, Object paramObject) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		addSelect(localHashMap, paramObject, "UhwebList");
	}

	public void addTree(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		buildTreeType(localHashMap, "UhwebTree", paramString, paramCachedRowSet);
	}

	public void addTree(String paramString, CachedRowSet paramCachedRowSet, boolean paramBoolean) {
		HashMap localHashMap = new HashMap();
		if (paramBoolean)
			localHashMap.put("loaddata", "widget");
		localHashMap.put("name", paramString);
		buildTreeType(localHashMap, "UhwebTree", paramString, paramCachedRowSet);
	}

	public void addTree(String paramString, List<Map<String, Object>> paramList) {
		TreeBean localTreeBean = new TreeBean();
		localTreeBean.setDataList(paramList);
		HashMap localHashMap = new HashMap();
		localHashMap.put("uhweb", "UhwebTree");
		localHashMap.put("name", paramString);
		localTreeBean.setViewData(localHashMap);
		this.resData.setObjectBean(localTreeBean);
		this.resData = DataBuilder.treeBeanToJson(this.resData);
	}

	public void addMultiSelectWithName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString);
		buildTreeType(localHashMap, "UhwebSelect", paramString, paramCachedRowSet);
	}

	public void addMultiSelectWithDataName(String paramString, CachedRowSet paramCachedRowSet) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("dataName", paramString);
		buildTreeType(localHashMap, "UhwebSelect", paramString, paramCachedRowSet);
	}

	private void buildTreeType(Map<String, Object> paramMap, String paramString1, String paramString2, CachedRowSet paramCachedRowSet) {
		paramMap.put("uhweb", paramString1);
		if (null == paramCachedRowSet)
			return;
		try {
			ResultSetMetaData localResultSetMetaData = paramCachedRowSet.getMetaData();
			paramCachedRowSet.beforeFirst();
			SelectBean localSelectBean = new SelectBean();
			int i = localResultSetMetaData.getColumnCount();
			while (paramCachedRowSet.next()) {
				HashMap localHashMap = new HashMap();
				for (int j = 1; j <= i; ++j) {
					Object localObject1 = paramCachedRowSet.getObject(localResultSetMetaData.getColumnName(j));
					if (localObject1 instanceof Date)
						localObject1 = localObject1.toString();
					localHashMap.put(localResultSetMetaData.getColumnLabel(j).toLowerCase(), localObject1);
				}
				localSelectBean.add(localHashMap);
			}
			localSelectBean.setViewData(paramMap);
			this.resData.setObjectBean(localSelectBean);
			try {
				paramCachedRowSet.close();
				paramCachedRowSet = null;
			} catch (SQLException localSQLException1) {
				throw new RuntimeException(localSQLException1);
			}
		} catch (SQLException localSQLException2) {
		} finally {
			if (paramCachedRowSet != null)
				try {
					paramCachedRowSet.close();
					paramCachedRowSet = null;
				} catch (SQLException localSQLException3) {
					throw new RuntimeException(localSQLException3);
				}
		}
	}

	private String getSetterName(String paramString) {
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("set");
		String str1 = paramString.substring(0, 1).toUpperCase();
		localStringBuffer.append(str1);
		String str2 = paramString.substring(1);
		localStringBuffer.append(str2);
		return localStringBuffer.toString();
	}

	private String getGetterName(String paramString) {
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("get");
		String str1 = paramString.substring(0, 1).toUpperCase();
		localStringBuffer.append(str1);
		String str2 = paramString.substring(1);
		localStringBuffer.append(str2);
		return localStringBuffer.toString();
	}

	public Object _getDataType(Class<?> paramClass, String paramString) {
		return getDataType(paramClass, paramString);
	}

	private Object getDataType(Class<?> paramClass, String paramString) {
		String str = paramClass.getName();
		if ((paramString == null) || ("null".equalsIgnoreCase(paramString)) || ("".equals(paramString)))
			return null;

		//<--boqi
		if (str.equals("nc.vo.pub.lang.UFDate")) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			try {
				java.util.Date date = fmt.parse(paramString);
				return UFDate.getDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (str.equals("nc.vo.pub.lang.UFDateTime")) {
			return new UFDateTime(paramString);
		}
		if (str.equals("nc.vo.pub.lang.UFTime")) {
			return new UFTime(paramString);
		}
		if (str.equals("nc.vo.pub.lang.UFDouble")) {
			return new UFDouble(paramString);
		}
		if (str.equals("nc.vo.pub.lang.UFBoolean")) {
			return UFBoolean.valueOf(paramString);
		}
		//-->boqi
		
		if (str.equals("java.lang.String"))
			return paramString;
		if (str.equals("java.lang.Integer"))
			return Integer.valueOf(Integer.parseInt(paramString));
		if (str.equals("java.lang.Float"))
			return Float.valueOf(paramString);
		if (str.equals("java.lang.Long"))
			return Long.valueOf(paramString);
		if (str.equals("java.lang.Double"))
			return Double.valueOf(paramString);
		if (str.equals("java.sql.Date"))
			return DateUtil.parseToDate(paramString);
		if (str.equals("java.sql.Timestamp"))
			return DateUtil.parseToTimestamp(paramString);
		if (str.equals("java.util.Calendar"))
			return DateUtil.parseToCalendar(paramString);
		if (str.equals("java.math.BigDecimal")) {
			BigDecimal localBigDecimal = new BigDecimal(paramString);
			return localBigDecimal;
		}
		if (str.equals("int"))
			return Integer.valueOf(Integer.parseInt(paramString));
		if (str.equals("float"))
			return Float.valueOf(Float.parseFloat(paramString));
		if (str.equals("double"))
			return Double.valueOf(Double.parseDouble(paramString));
		if (str.equals("char"))
			return Character.valueOf(paramString.charAt(0));

		return null;
	}

	public String getJson() {
		if (getResDataObject() == null)
			return "";
		if (getResDataObject().isCustomJson())
			return DataBuilder.resCusJsonData(getResDataObject());
		return DataBuilder.resJsonData(getResDataObject());
	}

	public void addPage(String paramString) {
		this.resData.putToJson("page", paramString);
	}

	public void addRootAttr(String paramString, Object paramObject) {
		this.resData.putToJson(paramString, paramObject);
	}

	public void addValidator(boolean paramBoolean, String paramString) {
		this.resData.putToJson("success", Boolean.valueOf(paramBoolean));
		this.resData.putToJson("valiMsg", paramString);
	}

	public void addValidator(boolean paramBoolean) {
		this.resData.putToJson("success", Boolean.valueOf(paramBoolean));
	}

	private String getCacheCode(Object paramObject, CacheCode paramCacheCode) {
		if ((paramObject == null) || ("".equals(paramObject)))
			return "";
		Object localObject1 = new HashMap();
		if (paramCacheCode != null) {
			String str1 = paramCacheCode.tableName();
			try {
				localObject1 = ServerCacheManager.getInstance().getCacheTableData(str1);
			} catch (Exception localException1) {
				throw new RuntimeException(localException1);
			}
			String str2 = paramCacheCode.codeName().toUpperCase();
			String str3 = paramCacheCode.valueName().toUpperCase();
			if (str2.equals(""))
				str2 = (String) ((Map) localObject1).get("codeName".toUpperCase());
			if (str3.equals(""))
				str3 = (String) ((Map) localObject1).get("valueName".toUpperCase());
			try {
				List localList = null;
				if (localList == null)
					localList = ServerCacheManager.getCacheUserInterface().getCacheData(str1);
				if ((str2 == null) || (str3 == null) || ("".equals(str3)) || ("".equals(str2)))
					throw new RuntimeException("codeName = " + str2 + ",valueName = " + str3 + ",中codeName或者valueName为空！请检查源注释或者数据库中缓存表注册信息配置！");
				for (int i = 0; i < localList.size(); ++i) {
					Object localObject2 = new HashMap();
					localObject2 = (Map) localList.get(i);
					if (((Map) localObject2).get(str2.toUpperCase()).equals(paramObject))
						return ((Map) localObject2).get(str3.toUpperCase()).toString();
				}
			} catch (Exception localException2) {
				throw new RuntimeException(localException2);
			}
		} else {
			return paramObject.toString();
		}
		return (String) (String) paramObject.toString();
	}

	private CacheCode getCacheCode(Object paramObject, String paramString) {
		CacheCode localCacheCode = null;
		try {
			Method localMethod = this.build.getDeclaredMethod(paramObject, paramString);
			if (localMethod != null)
				localCacheCode = (CacheCode) localMethod.getAnnotation(CacheCode.class);
			return localCacheCode;
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (Exception localException) {
			throw new RuntimeException(localException);
		}
	}

	private List<Map<String, String>> getCacheCodeConfig(String[] paramArrayOfString) {
		ArrayList localArrayList = new ArrayList();
		if (paramArrayOfString.length <= 0)
			throw new RuntimeException("配置信息为空！");
		for (String str : paramArrayOfString) {
			HashMap localHashMap = new HashMap();
			String[] arrayOfString2 = str.split(",", 0);
			if (arrayOfString2.length != 2)
				throw new RuntimeException("配置信息获取错误，必须为两个参数！传入的参数：" + arrayOfString2.length);
			localHashMap.put("tableName", arrayOfString2[0]);
			localHashMap.put("codeRowName", arrayOfString2[1]);
			localArrayList.add(localHashMap);
		}
		return localArrayList;
	}

	private String getCacheCodeFromMap(Map<String, String> paramMap, String paramString) {
		if ((paramString == null) || ("".equals(paramString)))
			return "";
		String str1 = (String) paramMap.get("tableName");
		String str2 = "";
		String str3 = "";
		Map localMap;
		try {
			localMap = ServerCacheManager.getInstance().getCacheTableData(str1);
		} catch (Exception localException1) {
			throw new RuntimeException(localException1);
		}
		if (localMap != null) {
			str2 = (String) localMap.get("codeName".toUpperCase());
			str3 = (String) localMap.get("valueName".toUpperCase());
		}
		try {
			this.cacheCodeList = ServerCacheManager.getCacheUserInterface().getCacheData(str1);
			for (int i = 0; i < this.cacheCodeList.size(); ++i) {
				Object localObject = new HashMap();
				localObject = (Map) this.cacheCodeList.get(i);
				if ((str2 == null) || (str3 == null) || ("".equals(str3)) || ("".equals(str2)))
					throw new RuntimeException("codeName = " + str2 + ",valueName = " + str3 + ",中codeName或者valueName为空！请检查源注释或者数据库中缓存表注册信息配置！");
				if (((Map) localObject).get(str2.toUpperCase()).toString().equals(paramString))
					return ((Map) localObject).get(str3.toUpperCase()) + "";
			}
		} catch (Exception localException2) {
			throw new RuntimeException(localException2);
		}
		return (String) paramString;
	}

	public Map<String, Object> getPaginationParams() {
		return this.reqData.getViewData();
	}

	public static List<Map<String, Object>> crsToList(CachedRowSet paramCachedRowSet) throws SQLException {
		ArrayList localArrayList = new ArrayList();
		int i = paramCachedRowSet.getMetaData().getColumnCount();
		paramCachedRowSet.beforeFirst();
		while (paramCachedRowSet.next()) {
			HashMap localHashMap = new HashMap();
			for (int j = 0; j < i; ++j)
				localHashMap.put(paramCachedRowSet.getMetaData().getColumnLabel(j + 1), paramCachedRowSet.getString(j + 1));
			localArrayList.add(localHashMap);
		}
		return localArrayList;
	}

	public void addJSTL(String paramString, Object paramObject) {
		this.jstlDataMap = getJstlDataMap();
		if (paramObject == null)
			return;
		if (paramObject instanceof CachedRowSet)
			try {
				this.jstlDataMap.put(paramString, crsToList((CachedRowSet) paramObject));
			} catch (SQLException localSQLException) {
				throw new RuntimeException(localSQLException);
			}
		else
			this.jstlDataMap.put(paramString, paramObject);
	}

	public void addChart(String paramString1, String paramString2) {
		HashMap localHashMap = new HashMap();
		localHashMap.put("name", paramString1);
		localHashMap.put("charts", paramString2);
		localHashMap.put("uhweb", "UhwebChart");
		this.resData.addJsonDatas(localHashMap);
	}
}