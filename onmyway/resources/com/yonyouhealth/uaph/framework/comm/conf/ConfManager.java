package com.yonyouhealth.uaph.framework.comm.conf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.comm.pool.PoolManager;


public class ConfManager {
	private static boolean flag = false;

	private static final ConcurrentHashMap<Locale, Future<ResourceBundle>> cache = new ConcurrentHashMap();

	private static final LogWritter logger = LogFactory
			.getLogger(ConfManager.class);

	public static void load() {
		if (!flag) {
			ConfParser traverse = new ConfParser();
			traverse.parser();
			flag = true;
		}

//		String runningmodel = (String) getValueByKey("running-model");

//		uaph.uhweb.core.domain.server.Server.runningmodel = runningmodel;

//		ValidateLc vl = new ValidateLc();
//		String license = vl.checkLicense();
//		if (license != null) {
//			System.out.println(vl.getErrorMsg());
//			System.exit(-1);
//		}
	}

	public static String getMessage(final Locale locale, String key) {
		Future f = (Future) cache.get(locale);
		if (f == null) {
			Callable eval = new Callable() {
				public ResourceBundle call() throws Exception {
					ResourceBundle bundle = null;
					ClassLoader cl = Thread.currentThread()
							.getContextClassLoader();

					if (cl == null) {
						cl = ConfManager.class.getClassLoader();
					}
					URL url = cl.getResource("/");
					File f = new File(url.toURI());
					File[] list = f.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return (name.endsWith("properties"))
									&& (name.contains("uhwebMessage"));
						}
					});
					List<String> l = new ArrayList<String>();
					for (File file : list) {
						int index = file.getName().indexOf("-");
						if ((index == -1)
								|| (l.contains(file.getName().substring(0,
										file.getName().indexOf("_"))))) {
							continue;
						}
						if (file.getName().indexOf("core") != -1) {
							l.add(0,
									file.getName().substring(0,
											file.getName().indexOf("_")));
						} else {
							l.add(file.getName().substring(0,
									file.getName().indexOf("_")));
						}
					}
					LinkedHashMap bundles = new LinkedHashMap();

					for (String baseName : l) {
						bundle = ResourceBundle.getBundle(baseName,
								locale, cl);
						bundles.put(bundle, baseName);
					}
					Properties properties = new Properties();

//					for (Map.Entry entry : bundles.entrySet()) {
					for (Iterator iterator = bundles.entrySet().iterator(); iterator.hasNext();) {
						Map.Entry entry = (Map.Entry) iterator.next();
						
						ResourceBundle bu = (ResourceBundle) entry.getKey();
						Enumeration keys = bu.getKeys();
						while (keys.hasMoreElements()) {
							String key = (String) keys.nextElement();
							if (properties.containsKey(key)) {
								ConfManager.logger.debug("��Դ�ļ� "
										+ (String) entry.getValue()
										+ "���ܺ�����Դ�ļ�����ͬkey:" + key);
							}

							properties.put(key, bu.getString(key));
						}
					}
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					properties.store(out, "");
					InputStream in = new ByteArrayInputStream(out.toByteArray());
					ResourceBundle finalBundle = new PropertyResourceBundle(in);
					return finalBundle;
				}
			};
			FutureTask ft = new FutureTask(eval);
			f = (Future) cache.putIfAbsent(locale, ft);
			if (f == null) {
				f = ft;
				ft.run();
			}
		}
		try {
			ResourceBundle bundle = (ResourceBundle) f.get();
			return bundle.getString(key);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object getValueByKey(String key) {
		return PoolManager.getInstance().borrowObject(key);
	}

	public static void setValue(String key, Object obj) {
		if (PoolManager.getInstance().borrowObject(key) == null)
			PoolManager.getInstance().addSingle(key, obj);
		else
			PoolManager.getInstance().refreshObject(key, obj);
	}

	public static void setValues(Map map) {
		PoolManager.getInstance().addSingles(map);
	}
}