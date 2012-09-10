package com.yonyouhealth.uaph.framework.comm.conf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;
import com.yonyouhealth.uaph.framework.comm.pool.PoolManager;
import com.yonyouhealth.uaph.framework.comm.util.FileUtils;

public class ConfParser {
	private static final LogWritter logger = LogFactory
			.getLogger(ConfParser.class);
	private Map confTreeNodeMap;
	private final String uhweb_bootstrap = "uhweb.xml";

	public ConfParser() {
		this.confTreeNodeMap = new HashMap();

		// this.sword_bootstrap = "sword.xml";
	}

	public void parser() {
		FileUtils fileUtils = new FileUtils("uhweb.xml");
		InputStream is = fileUtils.getInputStream();
		if (is == null) {
			logger.error("ƽ̨�������ļ�uhweb.xml��ȡ���󣡳�����ֹ���������л�����");

			System.exit(-1);
		}

		Document doc = init(is);
		Element root = getRootElement(doc);

		List properties = root.getChildren("property");
		for (int i = 0; i < properties.size(); ++i) {
			Element property = (Element) properties.get(i);
			String name = property.getAttributeValue("name");
			String value = property.getAttributeValue("value");
			addKey(name, value);
		}

		Element cluster = root.getChild("cluster");
		if (cluster != null) {
			List attributes = cluster.getAttributes();
			Properties props = new Properties();
			for (int i = 0; i < attributes.size(); ++i) {
				Attribute attribute = (Attribute) attributes.get(i);
				String name = attribute.getName();
				String value = attribute.getValue();
				props.setProperty(name, value);
			}
			addKey("cluster", props);
		}

		Element codecache = root.getChild("codecache");
		if (codecache != null) {
			List attributes = codecache.getAttributes();
			Properties props = new Properties();
			for (int i = 0; i < attributes.size(); ++i) {
				Attribute attribute = (Attribute) attributes.get(i);
				String name = attribute.getName();
				String value = attribute.getValue();
				props.setProperty(name, value);
			}
			addKey("codecache", props);
		}

		List startapps = root.getChildren("startapp");
		for (int i = 0; i < startapps.size(); ++i) {
			Element startapp = (Element) startapps.get(i);
			String name = startapp.getAttributeValue("name");
			String value = startapp.getAttributeValue("value");
			addKey(name, value);
		}

		Element scheduler = root.getChild("scheduler");
		if (scheduler != null) {
			List attributes = scheduler.getAttributes();
			Properties props = new Properties();
			for (int i = 0; i < attributes.size(); ++i) {
				Attribute attribute = (Attribute) attributes.get(i);
				String name = attribute.getName();
				String value = attribute.getValue();
				props.setProperty(name, value);
			}
			addKey("scheduler", props);
		}

		List ejbs = root.getChildren("ejb");
		Map map = new HashMap();
		for (int i = 0; i < ejbs.size(); ++i) {
			Element ejb = (Element) ejbs.get(i);
			List attributes = ejb.getAttributes();

			Properties props = new Properties();
			for (int k = 0; k < attributes.size(); ++k) {
				Attribute attribute = (Attribute) attributes.get(k);
				String name = attribute.getName();
				String value = attribute.getValue();
				props.setProperty(name, value);
			}
			String name = props.getProperty("jndi");
			map.put(name, props);
		}
		addKey("ejb", map);
	}

	private Document init(InputStream is) {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(is);
		} catch (Exception ex) {
			logger.error("�����ļ�����ʧ�ܣ�");
		}

		return doc;
	}

	private Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	private ConfTreeNode getConfTreeNode(Element confElement) {
		String name = confElement.getAttributeValue("name");
		String content = confElement.getAttributeValue("value");

		String parent = confElement.getAttributeValue("parent");
		String loader = confElement.getAttributeValue("loader");
		String language = confElement.getAttributeValue("language");
		String applied = confElement.getAttributeValue("applied");

		ConfTreeNode confTreeNode = new ConfTreeNode();

		confTreeNode.setNodeName(name);
		confTreeNode.setNodeContent(content);
		confTreeNode.setLoaderClassName(loader);
		confTreeNode.setLanguage(language);
		confTreeNode.setApplied(applied);
		if (loader == null) {
			confTreeNode.setLeaf(true);
		} else {
			confTreeNode.setLeaf(false);
		}

		if ((parent == null) || (parent.equals("root"))) {
			confTreeNode.setParentNode(null);
		} else {
			confTreeNode.setParentNode((ConfTreeNode) this.confTreeNodeMap
					.get(parent));
		}

		this.confTreeNodeMap.put(name, confTreeNode);

		return confTreeNode;
	}

	private List analyse(ConfTreeNode confTreeNode) {
		String loaderClassName = confTreeNode.getLoaderClassName();
		if ((loaderClassName != null) && (loaderClassName.length() > 0)) {
			IConfLoader confLoader = ConfLoaderFactory.getInstance().getLoader(
					loaderClassName);

			return confLoader.analyse(confTreeNode);
		}
		return new ArrayList();
	}

	private void addKey(String key, Object value) {
		Object obj = PoolManager.getInstance().borrowObject(key);
		if (obj == null) {
			PoolManager.getInstance().addSingle(key, value);
			logger.debug("���" + key + "[" + value + "]");
		} else {
			PoolManager.getInstance().refreshObject(key, value);
			logger.debug("����" + key + "[" + value + "]");
		}
	}

	public static void main(String[] argvs) {
		ConfParser cp = new ConfParser();
		cp.parser();
	}
}