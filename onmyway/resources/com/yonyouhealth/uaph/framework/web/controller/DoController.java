package com.yonyouhealth.uaph.framework.web.controller;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import nc.bs.logging.Logger;

import com.yonyouhealth.uaph.framework.comm.pool.ThreadLocalManager;
import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.UhwebReq;
import com.yonyouhealth.uaph.framework.web.event.UhwebRes;
import com.yonyouhealth.uaph.framework.web.exception.BizRuntimeException;

/**
 * Ctrl类处理流程
 */
public class DoController implements Serializable {
	private static final long serialVersionUID = 6891117434018521184L;
//	protected static final LogWritter log = LogFactory.getLogger(DoController.class);

	/**
	 * 执行Ctrl的流程,即执行指定的Ctrl类的Ctrl方法，相当于事件处理函数的通用调用入口
	 * @param ctrlName Ctrl的标记名,(即Ctrl类的注记名)
	 * @param ctrlMethod Ctrl方法名
	 */
	public static void execute(String ctrlName, String ctrlMethod) {
		try {
			Map localMap = new ControllerLoader().ctrlMap();
			
			Class clazz = Class.forName((String) localMap.get(ctrlName));
			Method method = clazz.getMethod(ctrlMethod, new Class[] { IReqData.class });
			if (method == null) {
				Logger.error("类" + ctrlName + "中没有为空的" + ctrlMethod + "方法");
				throw new RuntimeException("类" + ctrlName + "中没有为空的" + ctrlMethod + "方法");
			}
			
			Object ctrlObj = clazz.newInstance();
			UhwebRes res = (UhwebRes) method.invoke(ctrlObj, new Object[] { new UhwebReq(null,
					ContextAPI.getReqDataSet()) });
			if (res == null)
				ThreadLocalManager.add("resDataSet", null);
			else
				ThreadLocalManager.add("resDataSet", res.getResDataSet());
			
		} catch (ClassNotFoundException e) {
			Logger.error("找不到类" + ctrlName, e);
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			Logger.error("找不到类" + ctrlName + "的" + ctrlMethod + "方法", e);
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			Logger.error("类" + ctrlName + "实例化对象实例化失败", e);
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			Logger.error(ctrlMethod + "方法执行时失败", e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			Logger.error(ctrlMethod + "方法执行时失败", e);
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			if ((e.getCause() != null)
					&& (e.getCause() instanceof BizRuntimeException))
				return;
			Logger.error(ctrlMethod + "方法执行时失败", e);
			throw new RuntimeException(e);
		}
	}

	public static Object create(String className) {
		try {
			Class clazz = Class.forName(className);
			Object r = clazz.newInstance();
			return r;
		} catch (ClassNotFoundException e) {
			Logger.error("找不到类" + className, e);
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			Logger.error("类" + className + "实例化对象实例化失败", e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			Logger.error("类" + className + "实例化对象实例化失败", e);
			throw new RuntimeException(e);
		}
	}
}