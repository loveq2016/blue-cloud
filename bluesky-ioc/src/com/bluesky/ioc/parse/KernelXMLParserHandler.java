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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bluesky.ioc.metadata.ComponentMetadata;
import com.bluesky.ioc.metadata.CompositeMetadata;
import com.bluesky.ioc.metadata.ConstructorMetadata;
import com.bluesky.ioc.metadata.EntryMetadata;
import com.bluesky.ioc.metadata.Metadata;
import com.bluesky.ioc.metadata.PropertyMetadata;
import com.bluesky.ioc.metadata.ServiceMetadata;
import com.bluesky.ioc.metadata.ServicesMetadata;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class KernelXMLParserHandler extends DefaultHandler {
	private CompositeMetadata compositeMetadata;
	private ComponentMetadata currentComponentElement;
	private ServiceMetadata currentServiceElement;
	private ConstructorMetadata currentConstructorElement;
	private ServicesMetadata currentServicesElement;
	private PropertyMetadata currentPropertyElement;
	private EntryMetadata currentEntryElement;

	/**
	 * 
	 */
	public KernelXMLParserHandler() {
		compositeMetadata = new CompositeMetadata();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public final void endElement(String uri, String localName, String qName)
			throws SAXException {
		// component节点
		if (qName.equals(Metadata.ELEMENT_COMPONENT)) {
			compositeMetadata.addComponentMetadata(currentComponentElement);
			currentComponentElement = null;
		} else if (qName.equals(Metadata.ELEMENT_CONSTRUCTOR)) {// constructor节点
			currentComponentElement.setConstructor(currentConstructorElement);
			currentConstructorElement = null;
		} else if (qName.equals(Metadata.ELEMENT_SERVICE)) { // service 节点
			if (null != currentConstructorElement) {
				currentConstructorElement
						.addServiceMetadata(currentServiceElement);
			} else if (null != currentServicesElement) {
				currentServicesElement
						.addServiceMetadata(currentServiceElement);
			} else {
				currentComponentElement
						.addServiceMetadata(currentServiceElement);
			}
			currentServiceElement = null;
		} else if (qName.equals(Metadata.ELEMENT_SERVICES)) { // services 节点
			currentComponentElement.addServicesMetadata(currentServicesElement);
			currentServicesElement = null;
		} else if (qName.equals(Metadata.ELEMENT_PROPERTY)) { // property 节点
			if (currentEntryElement != null) {
				currentEntryElement.addPropertyMetadata(currentPropertyElement);
			} else {
				currentServiceElement
						.addPropertyMetadata(currentPropertyElement);
			}
			currentPropertyElement = null;
		} else if (qName.equals(Metadata.ELEMENT_ENTRY)) { // entry 节点
			currentServiceElement.addEntryMetadata(currentEntryElement);
			currentEntryElement = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public final void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// component节点
		if (qName.equals(Metadata.ELEMENT_COMPONENT)) {
			currentComponentElement = new ComponentMetadata();
			currentComponentElement.setAttributes(attributes);
		} else if (qName.equals(Metadata.ELEMENT_CONSTRUCTOR)) {// constructor节点
			currentConstructorElement = new ConstructorMetadata();
			currentConstructorElement.setAttributes(attributes);
		} else if (qName.equals(Metadata.ELEMENT_SERVICE)) { // service 节点
			currentServiceElement = new ServiceMetadata();
			currentServiceElement.setAttributes(attributes);
		} else if (qName.equals(Metadata.ELEMENT_SERVICES)) { // services 节点
			currentServicesElement = new ServicesMetadata();
			currentServicesElement.setAttributes(attributes);
		} else if (qName.equals(Metadata.ELEMENT_PROPERTY)) { // property 节点
			currentPropertyElement = new PropertyMetadata();
			currentPropertyElement.setAttributes(attributes);
		} else if (qName.equals(Metadata.ELEMENT_ENTRY)) { // entry 节点
			currentEntryElement = new EntryMetadata();
		}
	}

	/**
	 * 
	 * @return 如果没有进行解析，返回新的CompositeMetadata。否则返回解析完毕的元数据信息CompositeMetadata
	 */
	protected CompositeMetadata getCompositeMetadata() {
		return this.compositeMetadata;
	}

}
