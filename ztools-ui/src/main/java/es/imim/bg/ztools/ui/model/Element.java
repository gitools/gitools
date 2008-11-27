package es.imim.bg.ztools.ui.model;

import java.util.HashMap;
import java.util.Map;

public class Element {
	
	protected String name;
	
	protected String descr;
	
	protected Map<String, String> attributes = 
		new HashMap<String, String>();
	
	public Element() {
	}
	
	public Element(String name) {
		this.name = name;
	}
	
	public Element(String name, String descr) {
		this.name = name;
		this.descr = descr;
	}

	public Element(String name, String descr, Map<String, String> attributes) {
		this.name = name;
		this.descr = descr;
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescr() {
		return descr;
	}
	
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}
}
