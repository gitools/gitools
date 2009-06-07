package org.gitools.model;

import java.util.HashMap;
import java.util.Map;

public class ToolConfig {
	
	public static final String ZETCALC = "zetcalc";
	public static final String ONCOZET = "oncozet";
	
	protected String name;
	protected Map<String, String> properties = 
		new HashMap<String, String>();
	
	public ToolConfig(String name) {
		this.name = name;
	}
	
	public ToolConfig() {
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public void put(String name, String value) {
		properties.put(name, value);
	}
	
	public String get(String name) {
		return properties.get(name);
	}
}
