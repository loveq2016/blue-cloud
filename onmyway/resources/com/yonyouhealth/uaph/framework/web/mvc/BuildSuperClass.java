package com.yonyouhealth.uaph.framework.web.mvc;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Administrator
 *
 */
public class BuildSuperClass implements Serializable {
	private static final long serialVersionUID = 989252069386956400L;
	/**
	 * 当前操作的类实现的素有接口
	 */
	protected List<Class<?>> interfaceByClass = new CopyOnWriteArrayList();
	/**
	 * 存储某个对象的字段元数据对象的列表
	 */
	protected transient List<Field> annotationFields = new CopyOnWriteArrayList();


	/**
	 * @param entObj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void buildField(Object entObj) throws IllegalArgumentException, IllegalAccessException {
		Class clz = entObj.getClass();
		if (this.annotationFields == null)
			this.annotationFields = new CopyOnWriteArrayList();
		else
			this.annotationFields.clear();
		
		Field[] arr = clz.getDeclaredFields();
		this.annotationFields.addAll(Arrays.asList(arr));
		
		traverseClass(clz);
		Iterator iter = this.interfaceByClass.iterator();
		while (iter.hasNext()) {
			AddJson localAddJson;
			Class localClass2 = (Class) iter.next();
			Field[] arrayOfField2 = localClass2.getDeclaredFields();
//			Object localObject1 = arrayOfField2;
			int i = arrayOfField2.length;
			for (int j = 0; j < i; ++j) {
				Field localObject3 = arrayOfField2[j];
				localAddJson = (AddJson) localObject3.getAnnotation(AddJson.class);
				if (localAddJson != null)
					this.annotationFields.add(localObject3);
			}
			
			
			Annotation[] localObject1 = localClass2.getDeclaredAnnotations();
			Annotation[] localObject2 = localObject1;
			int j = localObject2.length;
			for (int k = 0; k < j; ++k) {
				Annotation item = localObject2[k];
				if (AddJson.class.isAssignableFrom(item.annotationType())) {
					Field[] arrayOfField3 = localClass2.getDeclaredFields();
					this.annotationFields.addAll(Arrays.asList(arrayOfField3));
				}
			}
		}
	}

	/**
	 * 获取指定对象中的指定方法名的方法反射对象
	 * @param methodName
	 * @param entObj
	 * @return
	 */
	public Method getMethod(String methodName, Object entObj) {
		Class clz = entObj.getClass();
		this.interfaceByClass.clear();
		traverseClass(clz);
		try {
			Method method = clz.getMethod(methodName, new Class[0]);
			if (method == null) {
				Iterator iter = this.interfaceByClass.iterator();
				while (iter.hasNext()) {
					Class clz2 = (Class) iter.next();
					method = clz2.getMethod(methodName, new Class[0]);
				}
			}
			return method;
		} catch (SecurityException localSecurityException) {
			throw new RuntimeException(localSecurityException);
		} catch (NoSuchMethodException localNoSuchMethodException) {
//			throw new RuntimeException(localNoSuchMethodException);
			return null;
		}
	}

	public Method getDeclaredMethod(Object paramObject, String paramString) {
		this.interfaceByClass.clear();
		traverseClass(paramObject.getClass());
		Class localClass1 = paramObject.getClass();
		Method localMethod = null;
		try {
			localMethod = localClass1.getDeclaredMethod(paramString, new Class[0]);
			return localMethod;
		} catch (Exception localException1) {
			Iterator localIterator = this.interfaceByClass.iterator();
			if (!localIterator.hasNext()) {
//				break label94;
				return localMethod;
			}
			Class localClass2 = (Class) localIterator.next();
			try {
				localMethod = localClass2.getDeclaredMethod(paramString, new Class[0]);
			} catch (Exception localException2) {
			}
		}
		label94: return localMethod;
	}

	public Field getDeclaredField(String paramString, Class<?> paramClass) {
		this.interfaceByClass.clear();
		traverseClass(paramClass);
		Field localField = null;
		try {
			localField = paramClass.getDeclaredField(paramString);
		} catch (Exception localException1) {
			Iterator localIterator = this.interfaceByClass.iterator();
			while (localIterator.hasNext()) {
				Class localClass = (Class) localIterator.next();
				try {
					localField = localClass.getDeclaredField(paramString);
				} catch (Exception localException2) {
				}
			}
		}
		return localField;
	}

	public Method getMethod(String paramString, Class<?> paramClass, Class<?>[] paramArrayOfClass) {
		this.interfaceByClass.clear();
		traverseClass(paramClass);
		Method localMethod = null;
		try {
			localMethod = paramClass.getMethod(paramString, paramArrayOfClass);
			return localMethod;
		} catch (Exception localException1) {
			Iterator localIterator = this.interfaceByClass.iterator();
			if (!localIterator.hasNext()) {
//				break label80;
				return localMethod;
			}
			Class localClass = (Class) localIterator.next();
			try {
				localMethod = localClass.getMethod(paramString, paramArrayOfClass);
			} catch (Exception localException2) {
			}
		}
		label80: return localMethod;
	}

	/**
	 * 获取指定的实体对象中的字段元数据对象集合
	 * @param entObj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Field[] getAllFields(Object entObj) throws IllegalArgumentException, IllegalAccessException {
		buildField(entObj);
		return (Field[]) this.annotationFields.toArray(new Field[0]);
	}

	/**
	 * 遍历指定的类对象，去寻找其实现的所有接口信息
	 * @param clz
	 */
	protected void traverseClass(Class<?> clz) {
		if ((clz == null) || (clz == Object.class))
			return;
		
		if (!this.interfaceByClass.contains(clz))
			this.interfaceByClass.add(clz);
		
		traverseClass(clz.getSuperclass());
		Class[] clzArr = clz.getInterfaces();
		for (Class localClass : clzArr)
			traverseClass(localClass);
	}
}