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
package com.bluesky.ioc;

/**
 * @author <a href="mailto:hanlu0808@gmail.com">Hanlu</a>
 * 
 */
public interface ComponentFactory {

	/**
	 * <h3>获得Component</h3> 根据name获得相应的Component
	 * 
	 * @param name
	 *            定义在xml配置文件中的Component的标识名称
	 * @return 
	 *         如果name为null或者在Component工厂中没有包含该name的Component，返回null。否则返回相应的Component
	 */
	Object getComponent(String name);

}
