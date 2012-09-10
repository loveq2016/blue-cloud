package com.yonyouhealth.uaph.framework.web.controller;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.bs.pub.InitDataException;

import com.yonyouhealth.uaph.framework.web.controller.annotations.CTRL;

/**
 * Ctrl类加载器,注意必须存在"/persistence.xml"文件，该文件作为标志了
 */
public class ControllerLoader {
//	private static final LogWritter logger = LogFactory.getLogger(ControllerLoader.class);
	/**
	 * 控制类后缀,所有的控制类必须以ctrl作为文件名的结尾
	 */
	private static final String CTRL_CLASS_SUFFIX = "ctrl.class";
	/**
	 * 承载控制类信息的容器,[类上面的注解中的名字，该类的全限定名字]
	 */
	public static Map<String, String> ctrlMap = null;

	/**
	 * 搜寻Ctrl并注册，以"/persistence.xml"文件为起始
	 * @return
	 */
	public Map<String, String> ctrlMap() {
		if (ctrlMap == null) {
			ctrlMap = new HashMap();
			
			//zzj add
			//增加对于系统级别的控制器的预加载
			preLoadCtrlClass();
			//
			
			Logger.info("==========开始扫描Ctrl==========");
			
			//todo：uap要特殊处理这块
			//得到要扫描的根目录，即src(classes)根目录
//			String rootClassPath = getClass().getClassLoader().getResource("/persistence.xml").getPath(); 
//			rootClassPath = rootClassPath.replaceFirst("/persistence.xml", "") + "/";
			
			
//------------------------UAP使用的-------------------------------------			
			RuntimeEnv env = RuntimeEnv.getInstance();
			String rootClassPath;
			if (env.isDevelopMode()) {
				//扫描classpath
//				String[] paths = System.getProperty("java.class.path").split(";");
				String[] paths = getClassPath();
				
				for (String path: paths) {
					if (path.toLowerCase().endsWith(".jar") || path.toLowerCase().endsWith(".zip")) {
							try {
								scanFile(path, getClass().getClassLoader());
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} else {
						if ((!path.endsWith("\\") && (!path.endsWith("/"))))
							path = path + "/";
						controllerScan(path, getClass().getClassLoader());
					}
				}
			} else {
			
				rootClassPath = env.getNCHome() + "/modules/";
				Logger.info("----Class Path----");
				Logger.info(rootClassPath);
				Logger.info("------------------");
				
				controllerScan(rootClassPath, getClass().getClassLoader());

				Logger.info("==========扫描ctrl结束==========");
			}
		}
		return ctrlMap;
	}

	private String[] getClassPath() {
		URLClassLoader clzLoader = (URLClassLoader) getClass().getClassLoader(); 
		List<String> listPath = new ArrayList<String> ();
		for (URL url: clzLoader.getURLs()) {
			if (url.getPath().startsWith("file:")) {
				listPath.add(url.getPath().substring(7));
			} else {
				listPath.add(url.getPath());
			}
		}
		return listPath.toArray(new String[0]);
	}

	private void scanFile(String fileName, ClassLoader classLoader) throws MalformedURLException, IOException, Exception {
		URL localURL = new URL("jar:file:" + fileName.replace('\\', '/') + "!/");
		URLConnection conn = localURL.openConnection();
		JarFile jarFile = null;
//							String str1 = null;
		if (conn instanceof JarURLConnection) {
			JarURLConnection jarConn = (JarURLConnection) conn;
			jarConn.setUseCaches(false);
			jarFile = jarConn.getJarFile();
//								str1 = jarConn.getJarFileURL().toExternalForm();
			
			Enumeration iter = jarFile.entries();
			while (iter.hasMoreElements()) {
				JarEntry entry = (JarEntry) iter.nextElement();
				String name = entry.getName();
				if ((name.toLowerCase().endsWith(CTRL_CLASS_SUFFIX)) && (name.indexOf("$") == -1)) {
					String ctrlClassName = name.substring(0, name.lastIndexOf(46)).replace('/', '.');//46='.'
					addDomainInfoFromJar(ctrlClassName, classLoader);
				}
			}
		}
	}
	
	/**
	 * 系统级别的控制器的预加载
	 */
	private void preLoadCtrlClass() {
		ctrlMap.put("ValidateCodeCtrl", "com.yonyouhealth.uaph.framework.web.web.commonCtrl.ValidateCodeCtrl");
	}
	
	/**
	 * 控制器扫描
	 * @param classPath 要扫描的根目录
	 * @param classLoader 类加载器
	 */
	private void controllerScan(String classPath, ClassLoader classLoader) {
		try {
			classPath = URLDecoder.decode(classPath, "UTF-8");
			
			//classpath对应的目录和该目录下的列表
			File file = new File(classPath);
			File[] fileList = file.listFiles();
			if (fileList == null && classPath.startsWith("/")) {
				classPath = classPath.substring(1);
				file = new File(classPath);
				fileList = file.listFiles();
			}
			
			//dir 和 class 文件
			if (fileList != null) {
				for (int i = 0; i < fileList.length; ++i) {
					File f = fileList[i];
					if (f.isDirectory()) {
						scanClassInDirectory(f, classPath, classLoader);
					}
					else if (f.getCanonicalPath().toLowerCase().endsWith(CTRL_CLASS_SUFFIX)) { //只找ctrl结尾的
						addDomainInfoFromAnnotation(f, classPath, classLoader);
					}
				}
			}
			
			//在和classes并列的lib文件夹中放置存放了Ctrl类的jar包
			//lib库中存放Ctrl类的jar包必须以-ctrl.jar或-ctrl.zip结尾,jar包中的ctrl类以ctrl结尾且不能含有$符号
			String tempClassPath = classPath;
			if (classPath.endsWith("/classes/")) {
				tempClassPath = classPath.substring(0, classPath.lastIndexOf("classes/"));
			}
			File libPath = new File(tempClassPath + "lib");
			File[] arr = libPath.listFiles();
			if (arr != null) {
				for (int i = 0; i < arr.length; ++i) {
					File f = arr[i];
					if (f.getName().toLowerCase().endsWith("-ctrl.jar") || f.getName().toLowerCase().endsWith("-ctrl.zip")) {
						URL localURL = new URL("jar:file:" + f.getCanonicalPath().replace('\\', '/') + "!/");
						URLConnection conn = localURL.openConnection();
						JarFile jarFile = null;
//						String str1 = null;
						if (conn instanceof JarURLConnection) {
							JarURLConnection jarConn = (JarURLConnection) conn;
							jarConn.setUseCaches(false);
							jarFile = jarConn.getJarFile();
//							str1 = jarConn.getJarFileURL().toExternalForm();
							
							Enumeration iter = jarFile.entries();
							while (iter.hasMoreElements()) {
								JarEntry entry = (JarEntry) iter.nextElement();
								String name = entry.getName();
								if ((name.toLowerCase().endsWith(CTRL_CLASS_SUFFIX)) && (name.indexOf("$") == -1)) {
									String ctrlClassName = name.substring(0, name.lastIndexOf(46)).replace('/', '.');//46='.'
									addDomainInfoFromJar(ctrlClassName, classLoader);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.error("err:" + e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 在指定的目录中扫描类(含子目录--通过递归)
	 * @param dir
	 * @param classPath
	 * @param classLoader
	 * @throws Exception
	 */
	private void scanClassInDirectory(File dir, String classPath, ClassLoader classLoader)
			throws Exception {
		File[] arr = dir.listFiles();
		if (arr != null) {
			for (File f : arr) {
				if (f.isDirectory()) {
					scanClassInDirectory(f, classPath, classLoader);
				} else if (f.getCanonicalPath().toLowerCase().endsWith(CTRL_CLASS_SUFFIX)) {
					addDomainInfoFromAnnotation(f, classPath, classLoader);
				}
				else if (f.getName().toLowerCase().endsWith("-ctrl.jar") || f.getName().toLowerCase().endsWith("-ctrl.zip")) {
					URL localURL = new URL("jar:file:" + f.getCanonicalPath().replace('\\', '/') + "!/");
					URLConnection conn = localURL.openConnection();
					JarFile jarFile = null;
//					String str1 = null;
					if (conn instanceof JarURLConnection) {
						JarURLConnection jarConn = (JarURLConnection) conn;
						jarConn.setUseCaches(false);
						jarFile = jarConn.getJarFile();
//						str1 = jarConn.getJarFileURL().toExternalForm();
						
						Enumeration iter = jarFile.entries();
						while (iter.hasMoreElements()) {
							JarEntry entry = (JarEntry) iter.nextElement();
							String name = entry.getName();
							if ((name.toLowerCase().endsWith(CTRL_CLASS_SUFFIX)) && (name.indexOf("$") == -1)) {
								String ctrlClassName = name.substring(0, name.lastIndexOf(46)).replace('/', '.');//46='.'
								addDomainInfoFromJar(ctrlClassName, classLoader);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 将类的注解中的信息增加到域中
	 * @param f Ctrl类文件
	 * @param classPath
	 * @param classLoader
	 * @throws Exception
	 */
	private void addDomainInfoFromAnnotation(File f, String classPath, ClassLoader classLoader)
			throws Exception {
		String path = f.getCanonicalPath().replace('\\', '/');
		if ((path.charAt(0) != classPath.charAt(0)) && (classPath.startsWith("/"))) {
			classPath = classPath.substring(1).replace("./", "");
		}
		
		String fileName = path.substring(classPath.length(), path.lastIndexOf('.')).replace('/', '.');
		try {
			Class clazz = classLoader.loadClass(fileName);
			
			//该类的特点
			if ((clazz.isAnnotationPresent(CTRL.class)) && (BaseDomainCtrl.class.isAssignableFrom(clazz))) {
				CTRL localCTRL = (CTRL) clazz.getAnnotation(CTRL.class);
				String clsAnnotation = localCTRL.value(); //注解上的value值，如@CTRL("ValidateCodeCtrl")，就是对当前类的一个标记,应该以CTRL结尾
				ctrlMap.put(clsAnnotation, clazz.getName());
				
				Logger.info("扫描到CTRL=[" + clazz.getName() + "]");
			}
		} catch (NoClassDefFoundError e) {
			
		}
	}

	/**
	 * 将JAR包中的控制类注册上
	 * @param ctrlClassName
	 * @param classLoader
	 * @throws Exception
	 */
	private void addDomainInfoFromJar(String ctrlClassName, ClassLoader classLoader) throws Exception {
		Logger.info("尝试载入[" + ctrlClassName + "]");
		try {
			Class localClass = classLoader.loadClass(ctrlClassName);
			if ((localClass.isAnnotationPresent(CTRL.class)) && (BaseDomainCtrl.class.isAssignableFrom(localClass))) {
				CTRL localCTRL = (CTRL) localClass.getAnnotation(CTRL.class);
				String str = localCTRL.value();
				ctrlMap.put(str, localClass.getName());
				
				Logger.info("扫描到CTRL=[" + localClass.getName() + "]");
			}
		} catch (NoClassDefFoundError e) {
			
		}
	}

	//[start] 无用的
	
	// private void controllerScan(String paramString, ClassLoader
	// paramClassLoader)
	// {
	// ctrlMap = new HashMap();
	// try
	// {
	// paramString = URLDecoder.decode(paramString, "UTF-8");
	// File localFile1 = new File(paramString);
	// File[] arrayOfFile1 = localFile1.listFiles();
	// if ((arrayOfFile1 == null) && (paramString.startsWith("/")))
	// {
	// paramString = paramString.substring(1);
	// localFile1 = new File(paramString);
	// arrayOfFile1 = localFile1.listFiles();
	// }
	// Object localObject2;
	// if (arrayOfFile1 != null)
	// for (localObject2 : arrayOfFile1)
	// if (((File)localObject2).isDirectory())
	// {
	// scanClassInDirectory((File)localObject2, paramString, paramClassLoader);
	// }
	// else
	// {
	// if
	// (!((File)localObject2).getCanonicalPath().toLowerCase().endsWith(CTRL_CLASS_SUFFIX))
	// continue;
	// addDomainInfoFromAnnotation((File)localObject2, paramString,
	// paramClassLoader);
	// }
	// ??? = paramString;
	// if (paramString.endsWith("/classes/"))
	// ??? = paramString.substring(0, paramString.lastIndexOf("classes/"));
	// File localFile2 = new File((String)??? + "lib");
	// File[] arrayOfFile2 = localFile2.listFiles();
	// if (arrayOfFile2 != null)
	// for (Object localObject3 : arrayOfFile2)
	// {
	// if ((!localObject3.getName().toLowerCase().endsWith("-ctrl.jar")) &&
	// (!localObject3.getName().toLowerCase().endsWith("-ctrl.zip")))
	// continue;
	// URL localURL = new URL("jar:file:" +
	// localObject3.getCanonicalPath().replace('\\', '/') + "!/");
	// URLConnection localURLConnection = localURL.openConnection();
	// JarFile localJarFile = null;
	// String str1 = null;
	// if (!(localURLConnection instanceof JarURLConnection))
	// continue;
	// JarURLConnection localJarURLConnection =
	// (JarURLConnection)localURLConnection;
	// localJarURLConnection.setUseCaches(false);
	// localJarFile = localJarURLConnection.getJarFile();
	// str1 = localJarURLConnection.getJarFileURL().toExternalForm();
	// Enumeration localEnumeration = localJarFile.entries();
	// while (localEnumeration.hasMoreElements())
	// {
	// JarEntry localJarEntry = (JarEntry)localEnumeration.nextElement();
	// String str2 = localJarEntry.getName();
	// if ((str2.toLowerCase().endsWith(CTRL_CLASS_SUFFIX)) && (str2.indexOf("$") ==
	// -1))
	// {
	// String str3 = str2.substring(0, str2.lastIndexOf('.')).replace('/', '.');
	// addDomainInfoFromJar(str3, paramClassLoader);
	// }
	// }
	// }
	// }
	// catch (Exception localException)
	// {
	// logger.error(localException.getMessage(), localException);
	// throw new RuntimeException(localException.getMessage());
	// }
	// }
	
	//[end]
}