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
package com.bluesky.ioc.metadata;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public class ComponentMetadataAttribute extends GlobalAttribute {

	// Component是单例的或者是多态的，取决于singleton或者new
	private String scope;
	protected final String[] scopes = { "singleton", "new" };

	/**
	 * 
	 */
	public ComponentMetadataAttribute() {
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope == null ? scopes[0] : scope;
	}

	/**
	 * @param scope
	 *            the scope to set
	 */
	private void setScope(String scope) {
		if (scope != null && !scope.trim().equals("")) {
			for (String s : scopes) {
				if (s.equals(scope)) {
					this.scope = scope;
					return;
				}
			}
		}
		this.scope = scopes[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.css.sword.esb.server.microkernel.metadata.GlobalAttribute#setAttribute
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setAttribute(String attributeName, String value) {
		if (attributeName.equals(Metadata.ATTRIBUTE_SCOPE)) {
			this.setScope(value);
			return;
		}
		super.setAttribute(attributeName, value);
	}
}
