/*
 * Copyright 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluesky.ioc.classloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class ClassHandler {

	/**
	 * @param classloader
	 * 
	 */
	private ClassHandler() {
	}

	/**
	 * <h3>获得Class</h3> 根据className，获得相应的Class
	 * 
	 * @param className
	 *            需要获得的Class的全类名
	 * @param isNewInstance
	 *            是否创建实例
	 * @return 如果在当前的ClassLoader中找不到该类，则会调用其父ClassLoader来查找，如果还找不到，
	 *         抛出ClassNotFoundException
	 *         。否则根据参数isNewInstance，true返回Object，false返回Class<? extends Object>
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	public static Object getClass(String className, boolean isNewInstance)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		ClassLoader classloader = Thread.currentThread()
				.getContextClassLoader();
		Class<?> clazz = classloader.loadClass(className);
		if (isNewInstance) {
			return getClassNewInstrance(clazz);
		}
		return clazz;
	}

	/**
	 * <h3>获得Class的实例对象</h3>
	 * 
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	private static Object getClassNewInstrance(Class<?> clazz)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		if (clazz == null) {
			throw new ClassNotFoundException();
		}
		return clazz.getConstructor((Class[]) null)
				.newInstance((Object[]) null);
	}

	/**
	 * <h3>创建对象</h3> 此方法以有参数的构造函数创建Class Instance
	 * 
	 * @param className
	 *            对象的全类名
	 * @param index
	 *            标识在Class中顺序的第几个构造函数。如果constructorIndex小于0或者大于Class中的构造函数集合值，采用默认值
	 *            。默认值为0
	 * @param initargs
	 *            构造函数的参数值集合
	 * @return 返回className所表示的Class实例Object
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object createObject(String className, int index,
			Object[] initargs) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		Class<?> clazz = (Class<?>) getClass(className, false);
		if (index < 0 || index > clazz.getConstructors().length - 1) {
			index = 0;
		}
		Constructor<?> constructor = clazz.getConstructors()[index];
		return constructor.newInstance(initargs);
	}

	/**
	 * <h3>创建对象</h3> 此方法以有参数的构造函数创建Class Instance
	 * 
	 * @param className
	 *            对象的全类名
	 * @param parameterTypes
	 *            构造函数的参数类型集合
	 * @param initargs
	 *            构造函数的参数值集合
	 * @return 返回className所表示的Class实例Object
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	public static Object createObject(String className,
			Class<?>[] parameterTypes, Object[] initargs)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = (Class<?>) getClass(className, false);
		return clazz.getConstructor(parameterTypes).newInstance(initargs);
	}

	/**
	 * <h3>执行Class的方法</h3>
	 * 
	 * @param obj
	 *            执行的对象
	 * @param methodName
	 *            方法名称
	 * @param args
	 *            方法的参数
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void invokeMethod(Object obj, String methodName, Object args)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Class<? extends Object> clazz = obj.getClass();
		try {
			if (args != null) {
				clazz.getMethod(methodName, args.getClass()).invoke(obj, args);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			for (Method method : clazz.getMethods()) {
				if (method.getName().equals(methodName)
						&& method.getParameterTypes().length == 1) {
					method.invoke(obj, args);
				}
			}
		}
	}

}
