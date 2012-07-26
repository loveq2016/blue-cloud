/**
 * 
 */
package com.bluecloud.mvc.external;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leo
 * 
 */
public final class FragmentEventRegister {

	Map<String, FragmentEvent> events;

	public FragmentEventRegister() {
		events = new HashMap<String, FragmentEvent>();
	}

	/**
	 * 
	 * @param event
	 */
	public void add(FragmentEvent event) {
		events.put(event.toString(), event);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public FragmentEvent find(String name) {
		return events.get(name);
	}

}
