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

import java.util.Map;

import com.bluesky.ioc.exception.KernelComponentBuilderException;
import com.bluesky.ioc.exception.KernelComponentDeployerException;
import com.bluesky.ioc.exception.KernelXMLParserException;
import com.bluesky.ioc.metadata.ComponentMetadata;
import com.bluesky.ioc.metadata.CompositeMetadata;
import com.bluesky.ioc.parse.XMLParser;
import com.bluesky.logging.Log;
import com.bluesky.logging.LogFactory;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public final class KernelComponentDeployer implements ComponentDeployer {

	private Log log=LogFactory.getLog(KernelComponentDeployer.class);
	/**
	 * 
	 */
	public KernelComponentDeployer() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.core.ComponentDeployer#deploy(com
	 * .css.sword.esb.server.microkernel.parse.XMLParser,
	 * com.css.sword.esb.server.microkernel.ComponentDepender,
	 * com.css.sword.esb.server.microkernel.core.ComponentBuilder)
	 */
	public void deploy(XMLParser parser, ComponentDepender depender,
			ComponentBuilder builder) throws KernelComponentDeployerException {
		try {
			CompositeMetadata compositeMetadata = parser.parse();
			this.deploy(compositeMetadata, builder);
			compositeMetadata=null;
		} catch (KernelXMLParserException e) {
			throw new KernelComponentDeployerException(e);
		}
	}

	/**
	 * 
	 * @param compositeMetadata
	 * @param builder
	 */
	private void deploy(final CompositeMetadata compositeMetadata,
			ComponentBuilder builder) {
		Map<String, ComponentMetadata> components = compositeMetadata
				.getComponentMetadatas();
		for (ComponentMetadata componentMetadata : components.values()) {
			try {
				builder.build(new ComponentObject(componentMetadata),components);
			} catch (KernelComponentBuilderException e) {
				if(log.isErrorEnabled()){
					log.error("微内核部署Component："+componentMetadata.getName()+"失败", e);
				}
			}
		}
	}
}
