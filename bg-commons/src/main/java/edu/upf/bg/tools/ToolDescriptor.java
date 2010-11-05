package edu.upf.bg.tools;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * It represents the descriptor for a tool
 * and contains all the relevant information for the tool.
 */

@XmlRootElement
public class ToolDescriptor {

	protected String name;
	protected String description;
	protected Class<?> argsClass;
	protected Class<? extends ToolLifeCycle> lifeCycleClass;
	
	public ToolDescriptor() {
	}
	
	public ToolDescriptor(
			String name, 
			String descr, 
			Class<?> argsObject, 
			Class<? extends ToolLifeCycle> toolClass) {
		
		this.name = name;
		this.description = descr;
		this.argsClass = argsObject;
		this.lifeCycleClass = toolClass;
	}

	@XmlElement
	public String getName() {
		return name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public Class<?> getArgumentsClass() {
		return argsClass;
	}
	
	protected void setArgumentsClass(Class<?> argsClass) {
		this.argsClass = argsClass;
	}
	
	@XmlElement
	public Class<? extends ToolLifeCycle> getLifeCycleClass() {
		return lifeCycleClass;
	}
	
	protected void setLifeCycleClass(Class<? extends ToolLifeCycle> lifeCycleClass) {
		this.lifeCycleClass = lifeCycleClass;
	}
}
