/*
 * Copyright 2010 the original author or authors.
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
package com.bluesky.ioc.core;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.bluesky.ioc.classloader.ClassHandler;
import com.bluesky.ioc.exception.KernelComponentDependerException;
import com.bluesky.ioc.metadata.ComponentMetadata;
import com.bluesky.ioc.metadata.ConstructorMetadata;
import com.bluesky.ioc.metadata.EntryMetadata;
import com.bluesky.ioc.metadata.PropertyMetadata;
import com.bluesky.ioc.metadata.ServiceMetadata;
import com.bluesky.ioc.metadata.ServicesMetadata;
import com.bluesky.logging.Log;
import com.bluesky.logging.LogFactory;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class KernelComponentDepender implements ComponentDepender {

	private Log log = LogFactory.getLog(KernelComponentDepender.class);

	/**
	 * 
	 */
	public KernelComponentDepender() {
	}

	/**
	 * <h3>获得Component的依赖对象</h3>
	 * 
	 * @param service
	 *            Component的Service元数据
	 * @param context
	 *            已经解决依赖的Component上下文
	 * @return
	 * @throws KernelComponentDependerException
	 */
	private Object getServiceDepend(ServiceMetadata service,
			ComponentContext context) throws KernelComponentDependerException {
		String bind = service.getBind();
		if (bind != null) {
			ComponentObject componentObject = ((KernelComponentContext) context)
					.getReadyComponent(bind);
			if (componentObject == null) {
				componentObject = ((KernelComponentContext) context)
						.getDependComponent(bind);
			}
			if (componentObject != null) {
				if (componentObject.isNew()) {
					return this.cloneComponent(service, componentObject,
							context);
				} else {
					return componentObject.getObject();
				}
			}
			return null;
		} else {
			try {
				int size = service.getProperties().isEmpty() ? 0 : service
						.getProperties().size();
				if (size > 0) {
					Class<?>[] parameterTypes = new Class[size];
					Object[] initargs = new Object[size];
					for (int i = 0; i < size; i++) {
						PropertyMetadata property = service.getProperties()
								.get(i);
						parameterTypes[i] = property.getType();
						initargs[i] = property.getValue();
					}
					return ClassHandler.createObject(service.getClazz(),
							parameterTypes, initargs);
				} else {
					return ClassHandler.getClass(service.getClazz(), true);
				}
			} catch (Exception e) {
				throw new KernelComponentDependerException(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.core.ComponentDepender#injectConstructor
	 * (com.css.sword.esb.server.microkernel.metadata.ComponentMetadata,
	 * com.css.sword.esb.server.microkernel.core.ComponentContext)
	 */
	public Object injectConstructor(ComponentMetadata componentMetadata,
			ComponentContext context) throws KernelComponentDependerException {
		Object componentObject = null;
		ConstructorMetadata constructor = componentMetadata.getConstructor();
		int size = constructor.getServices().size();
		Object[] constructorObjects = new Object[size];
		try {
			for (int index = 0; index < size; index++) {
				ServiceMetadata service = constructor.getServices().get(index);
				constructorObjects[index] = getServiceDepend(service, context);
			}
			componentObject = ClassHandler.createObject(componentMetadata
					.getClazz(), constructor.getIndex(), constructorObjects);
		} catch (Exception e) {
			throw new KernelComponentDependerException(e);
		}
		return componentObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.core.ComponentDepender#inject(java
	 * .lang.Object,
	 * com.css.sword.esb.server.microkernel.metadata.ServiceMetadata,
	 * com.css.sword.esb.server.microkernel.core.ComponentContext)
	 */
	public void inject(Object componentObject, ServiceMetadata service,
			ComponentContext context) throws KernelComponentDependerException {
		Object injectObject = this.getServiceDepend(service, context);
		String bind = service.getBind();
		String methodName = null;
		try {
			if (bind != null && service.getName() == null) {
				methodName = this.getSetMethodName(bind);
			} else {
				methodName = this.getSetMethodName(service.getName());
			}
			ClassHandler
					.invokeMethod(componentObject, methodName, injectObject);
		} catch (Exception e) {
			throw new KernelComponentDependerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.core.ComponentDepender#injectList
	 * (java.lang.Object,
	 * com.css.sword.esb.server.microkernel.metadata.ServicesMetadata,
	 * com.css.sword.esb.server.microkernel.core.ComponentContext)
	 */
	public void injectList(Object componentObject, ServicesMetadata services,
			ComponentContext context) throws KernelComponentDependerException {
		String limited = services.getLimited();
		String type = services.getType();
		String name = services.getName();
		int size = services.getSize();
		String methodName = getSetMethodName(name);
		Object injectObject = null;
		try {
			if (limited != null) {
				injectObject = haveLimited(limited, type, size, services,
						context);
			} else {
				injectObject = notLimited(type, services, context, size);
			}
			ClassHandler
					.invokeMethod(componentObject, methodName, injectObject);
		} catch (Exception e) {
			throw new KernelComponentDependerException(e);
		}
	}

	/**
	 * <h3>获得set方法名称</h3> 将service节点的[name]属性转为：setName
	 * 
	 * @param name
	 *            需要转换的方法参数，service节点的bind或者name属性、services节点的name属性
	 * @return set的方法名
	 */
	private String getSetMethodName(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * <h3>获得没有泛型限定的列表注入对象</h3>
	 * 
	 * @param type
	 *            列表类型
	 * @param services
	 *            注入的属性列表元数据信息
	 * @param context
	 *            已经解决依赖的Component集合
	 * @param size
	 *            Component个数
	 * @return
	 * @throws KernelComponentDependerException
	 */
	private Object notLimited(String type, ServicesMetadata services,
			ComponentContext context, int size)
			throws KernelComponentDependerException {
		if (type.equals("List")) {
			return this.collectionType(new ArrayList<Object>(size), services,
					context);
		}
		if (type.equals("Set")) {
			return this.collectionType(new HashSet<Object>(size), services,
					context);
		}
		if (type.equals("Map")) {
			Map<Object, Object> map = new HashMap<Object, Object>(size);
			for (ServiceMetadata service : services.getServices()) {
				Object serviceObject = this.getServiceDepend(service, context);
				if (service.getName() != null) {
					map.put(service.getName(), serviceObject);
				} else if (service.getName() == null
						&& service.getBind() != null) {
					map.put(service.getBind(), serviceObject);
				}
			}
			return map;
		}
		if (type.equals("Array")) {
			return this.collectionType(new ArrayList<Object>(size), services,
					context).toArray(new Object[size]);
		}
		return null;
	}

	/**
	 * <h3>获得有泛型限定的列表注入对象</h3>
	 * 
	 * @param limited
	 *            泛型限定类名
	 * @param type
	 *            列表类型
	 * @param size
	 *            Component个数
	 * @param services
	 *            注入的属性列表元数据信息
	 * @param context
	 *            已经解决依赖的Component集合
	 * @return
	 * @throws KernelComponentDependerException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	private Object haveLimited(String limited, String type, int size,
			ServicesMetadata services, ComponentContext context)
			throws KernelComponentDependerException, IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, InvocationTargetException,
			NoSuchMethodException {
		Class<?> limitedClass = (Class<?>) ClassHandler
				.getClass(limited, false);
		if (type.equals("List")) {
			return this.collectionType(new ArrayList<Object>(size), services,
					context, limitedClass, limited);
		}
		if (type.equals("Set")) {
			return this.collectionType(new HashSet<Object>(size), services,
					context, limitedClass, limited);
		}
		if (type.equals("Map")) {
			Map<Object, Object> map = new HashMap<Object, Object>(size);
			for (ServiceMetadata service : services.getServices()) {
				Object serviceObject = this.getServiceDepend(service, context);
				if (limitedClass.isAssignableFrom(serviceObject.getClass())) {
					if (service.getName() != null) {
						map.put(service.getName(), serviceObject);
					} else if (service.getName() == null
							&& service.getBind() != null) {
						map.put(service.getBind(), serviceObject);
					}
				} else {
					if (log.isErrorEnabled()) {
						log.error("注入失败，因为类型限定：" + limited);
					}
					throw new ClassCastException(service.getClazz());
				}
			}
			return map;
		}
		if (type.equals("Array")) {
			Object array = Array.newInstance(limitedClass, size);
			int index = 0;
			for (ServiceMetadata service : services.getServices()) {
				Object serviceObject = this.getServiceDepend(service, context);
				if (limitedClass.isAssignableFrom(serviceObject.getClass())) {
					Array.set(array, index, serviceObject);
					index++;
				} else {
					if (log.isErrorEnabled()) {
						log.error("注入失败，类型限定：" + limited);
					}
					throw new ClassCastException(service.getClazz());
				}
			}
			return array;
		}
		return null;
	}

	/**
	 * <h3>List、Set或者数组类型的注入</h3>
	 * 
	 * @param collection
	 *            List、Set或者数组
	 * @param services
	 *            注入的属性列表元数据信息
	 * @param context
	 *            已经解决依赖的Component集合
	 * @return
	 * @throws KernelComponentDependerException
	 * @throws KernelComponentDependerException
	 */
	private Collection<Object> collectionType(Collection<Object> collection,
			ServicesMetadata services, ComponentContext context)
			throws KernelComponentDependerException {
		for (ServiceMetadata service : services.getServices()) {
			Object serviceObject = this.getServiceDepend(service, context);
			collection.add(serviceObject);
		}
		return collection;
	}

	/**
	 * <h3>List、Set或者数组类型的注入</h3>
	 * 
	 * @param collection
	 *            List、Set或者数组的集合
	 * @param services
	 *            注入的属性列表元数据信息
	 * @param context
	 *            已经解决依赖的Component集合
	 * @param limitedClass
	 *            泛型限定的Class
	 * @param limited
	 *            泛型限定类名
	 * @return
	 * @throws KernelComponentDependerException
	 */
	private Collection<Object> collectionType(Collection<Object> collection,
			ServicesMetadata services, ComponentContext context,
			Class<?> limitedClass, String limited)
			throws KernelComponentDependerException {
		for (ServiceMetadata service : services.getServices()) {
			Object serviceObject = this.getServiceDepend(service, context);
			if (limitedClass.isAssignableFrom(serviceObject.getClass())) {
				collection.add(serviceObject);
			} else {
				if (log.isErrorEnabled()) {
					log.error("注入失败，类型限定：" + limited);
				}
				throw new ClassCastException(service.getClazz());
			}
		}
		return collection;
	}

	/**
	 * <h3>克隆Component对象</h3>
	 * 
	 * @param component
	 *            绑定的Component元数据
	 * @param cloneComponent
	 *            被克隆的Component对象
	 * @param context
	 *            已经解决依赖的Component集合
	 * @return
	 * @throws KernelComponentDependerException
	 */
	private Object cloneComponent(ServiceMetadata component,
			ComponentObject cloneComponent, ComponentContext context)
			throws KernelComponentDependerException {
		ComponentMetadata cloneComponentMetadata = cloneComponent
				.getComponentMetadate();
		Object componentObject = null;
		try {
			// 是否存在Constructor依赖注入，如果存在，Constructor注入依赖。
			ConstructorMetadata cloneConstructor = cloneComponentMetadata
					.getConstructor();
			if (cloneConstructor != null) {
				List<ServiceMetadata> oldConstructorServices = new ArrayList<ServiceMetadata>();
				int cloneServiceSize = cloneConstructor.getServices().size();
				int entrySize = component.getEntries().size();
				// 如果注入的参数个数不等于依赖的Component的constructor的service个数，不能替换新的初始化参数
				if (cloneServiceSize == entrySize) {
					int serviceIndex = 0;
					List<ServiceMetadata> cloneConstructorServices = cloneConstructor
							.getServices();
					this.copyCloneConstructorService(oldConstructorServices,
							cloneConstructorServices);// 保留constructor的service副本
					for (EntryMetadata entry : component.getEntries()) {
						int eps = entry.getProperties().size();
						List<PropertyMetadata> cloneServiceProperties = cloneConstructorServices
								.get(serviceIndex).getProperties();
						List<EntryMetadata> cloneServiceEntries = cloneConstructorServices
								.get(serviceIndex).getEntries();
						if (!cloneServiceProperties.isEmpty()) {// 在被替换的service节点下只有property节点
							this.cloneServiceProperties(cloneServiceProperties,
									eps, entry);
						} else if (!cloneServiceEntries.isEmpty()) {// 在被替换的service节点下只有entry节点
							this.cloneServiceEntries(cloneServiceEntries, eps,
									entry);
						}
						serviceIndex++;
					}
				}
				componentObject = injectConstructor(cloneComponentMetadata,
						context);
				// 还原Component的元数据信息
				this.restoreCloneComponent(cloneComponentMetadata,
						oldConstructorServices);
			} else {
				componentObject = ClassHandler.getClass(cloneComponentMetadata
						.getClazz(), true);
			}
			// 是否存在属性依赖
			for (ServiceMetadata service : cloneComponentMetadata.getServices()) {
				inject(componentObject, service, context);
			}
			// 是否存在属性列表依赖
			for (ServicesMetadata services : cloneComponentMetadata
					.getListService()) {
				injectList(componentObject, services, context);
			}
		} catch (Exception e) {
			throw new KernelComponentDependerException(e);
		}
		return componentObject;
	}

	/**
	 * <h3>拷贝constructor的service副本</h3> 用于在克隆Component之后，还原Component的元数据信息
	 * 
	 * @param oldConstructorServices
	 * @param cloneConstructorServices
	 */
	private void copyCloneConstructorService(
			List<ServiceMetadata> oldConstructorServices,
			List<ServiceMetadata> cloneConstructorServices) {
		for (ServiceMetadata oldServiceMetadata : cloneConstructorServices) {
			oldConstructorServices.add(oldServiceMetadata
					.cloneServiceMetadata());
		}
	}

	/**
	 * <h3>还原克隆的Component元数据</h3>
	 * 
	 * @param cloneComponentMetadata
	 * @param oldConstructorServices
	 */
	private void restoreCloneComponent(
			ComponentMetadata cloneComponentMetadata,
			List<ServiceMetadata> oldConstructorServices) {
		for (int i = 0; i < oldConstructorServices.size(); i++) {
			cloneComponentMetadata.getConstructor().getServices().set(i,
					oldConstructorServices.get(i));
		}
	}

	/**
	 * <h3>克隆service的entry元数据信息</h3>
	 * 
	 * @param cloneServiceEntries
	 * @param eps
	 * @param entry
	 */
	private void cloneServiceEntries(List<EntryMetadata> cloneServiceEntries,
			int eps, EntryMetadata entry) {
		int propertyIndex = 0;
		int sps = 0;
		for (EntryMetadata serviceEntry : cloneServiceEntries) {
			sps += serviceEntry.getProperties().size();
		}
		// 如果被替换的property元素多于替换的property元素，则使用替换的个数，否则使用被替换的个数
		int nps = sps - eps >= 0 ? eps : sps;
		for (int entryIndex = 0; entryIndex < cloneServiceEntries.size(); entryIndex++) {
			List<PropertyMetadata> cloneEntryProperties = cloneServiceEntries
					.get(entryIndex).getProperties();
			for (int entryPropertyIndex = 0; entryPropertyIndex < cloneEntryProperties
					.size(); entryPropertyIndex++) {
				if (propertyIndex < nps) {
					PropertyMetadata newPM = entry.getProperties().get(
							propertyIndex);
					cloneEntryProperties.set(entryPropertyIndex, newPM);
				} else {
					return;
				}
				propertyIndex++;
			}
		}
	}

	/**
	 * <h3>克隆service的property元数据信息</h3>
	 * 
	 * @param cloneServiceProperties
	 * @param eps
	 * @param entry
	 */
	private void cloneServiceProperties(
			List<PropertyMetadata> cloneServiceProperties, int eps,
			EntryMetadata entry) {
		int propertyIndex = 0;
		int sps = cloneServiceProperties.size();
		// 如果被替换的property元素多于替换的property元素，则使用替换的个数，否则使用被替换的个数
		int nps = sps - eps >= 0 ? eps : sps;
		while (propertyIndex < nps) {
			cloneServiceProperties.set(propertyIndex, entry.getProperties()
					.get(propertyIndex));
			propertyIndex++;
		}
	}
}
