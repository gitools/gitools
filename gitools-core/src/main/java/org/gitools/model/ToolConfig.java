package org.gitools.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.model.xml.adapter.ConfigurationXmlAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolConfig {

	//FIXME This shouldn't be here
	public static final String ZETCALC = "zetcalc";
	public static final String ONCOZET = "oncozet";
	
	protected String name;

	@XmlJavaTypeAdapter(ConfigurationXmlAdapter.class)
	protected Map<String, String> configuration = new HashMap<String, String>();
	
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
	
	public Map<String, String> getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}
	
	public void put(String name, String value) {
		configuration.put(name, value);
	}
	
	public String get(String name) {
		return configuration.get(name);
	}
}
