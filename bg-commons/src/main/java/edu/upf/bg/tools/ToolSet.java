package edu.upf.bg.tools;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.upf.bg.tools.ToolDescriptor;

@XmlRootElement(name="tools")
public class ToolSet {
	
	protected List<ToolDescriptor> toolDescriptors =
		new ArrayList<ToolDescriptor>();
	
	public ToolSet() {
	}
	
	@XmlElement(name="tool")
	public List<ToolDescriptor> getToolDescriptors() {
		return toolDescriptors;
	}
	
	public void setToolDescriptors(List<ToolDescriptor> toolDescriptors) {
		this.toolDescriptors = toolDescriptors;
	}
	
	public void add(ToolSet toolSet) {
		toolDescriptors.addAll(toolSet.getToolDescriptors());
	}
	
	public void add(ToolDescriptor desc) {
		toolDescriptors.add(desc);
	}
}
