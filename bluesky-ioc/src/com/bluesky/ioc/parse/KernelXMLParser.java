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
package com.bluesky.ioc.parse;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.bluesky.ioc.exception.KernelXMLParserException;
import com.bluesky.ioc.metadata.CompositeMetadata;
import com.bluesky.ioc.source.KernelSource;
import com.bluesky.logging.Log;
import com.bluesky.logging.LogFactory;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class KernelXMLParser implements XMLParser {

	private static final String SHCAME = "microkernel.xsd";
	private Log log = LogFactory.getLog(KernelXMLParser.class);
	private URL[] validatedXML;
	private Validator validator = null;

	public KernelXMLParser() throws KernelXMLParserException {
		InputStream is = KernelXMLParser.class.getClassLoader()
				.getResourceAsStream(SHCAME);
		if (null == is) {
			throw new KernelXMLParserException(SHCAME + "文件不存在");
		}
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = factory.newSchema(new StreamSource(is));
			validator = schema.newValidator();
			is.close();
		} catch (Exception e) {
			throw new KernelXMLParserException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.css.sword.esb.server.microkernel.parse.XMLParser#parse()
	 */
	public CompositeMetadata parse() throws KernelXMLParserException {
		KernelXMLParserHandler parserHandler = new KernelXMLParserHandler();
		if (validatedXML != null) {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			try {
				SAXParser parser = parserFactory.newSAXParser();
				for (URL url : validatedXML) {
					InputStream is = url.openStream();
					parser.parse(is, parserHandler);
					is.close();
				}
			} catch (Exception e) {
				throw new KernelXMLParserException(e);
			}
		}
		return parserHandler.getCompositeMetadata();
	}

	/**
	 * <h3>加载XML配置文件</h3> 验证XML配置文件是否符合Shcema规范，将符合规范的XML文件缓存到新的XML列表中
	 * 
	 * @param xmlPath
	 *            XML配置文件的URL数组
	 * @throws KernelXMLParserException
	 */
	private void load(URL[] xmlPath) throws KernelXMLParserException {
		Set<URL> urls = new LinkedHashSet<URL>();
		for (URL url : xmlPath) {
			try {
				InputStream is = url.openStream();
				validator.validate(new StreamSource(is));
				urls.add(url);
				is.close();
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error(url.toString() + "文件验证失败");
				}
			}
		}
		this.validatedXML = urls.toArray(new URL[urls.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.parse.XMLParser#load(com.css.sword
	 * .esb.server.microkernel.source.KernelSource)
	 */
	public void load(KernelSource source) throws KernelXMLParserException {
		if (source == null || source.resource() == null) {
			return;
		}
		Object resource = source.resource();
		if (resource instanceof URL[]) {
			this.load((URL[]) resource);
			return;
		}
	}

}
