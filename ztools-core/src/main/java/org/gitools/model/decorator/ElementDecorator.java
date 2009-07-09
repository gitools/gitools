package org.gitools.model.decorator;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.gitools.model.AbstractModel;
import org.gitools.model.matrix.element.IElementAdapter;
@XmlRootElement
public abstract class ElementDecorator extends AbstractModel {

	private static final long serialVersionUID = -2101303088018509837L;

	protected IElementAdapter adapter;
	
	public ElementDecorator(){
		
	}
	
	public ElementDecorator(IElementAdapter adapter) {
		this.adapter = adapter;
	}
	
	public IElementAdapter getAdapter() {
		return adapter;
	}
	
	public abstract void decorate(
			ElementDecoration decoration, 
			Object element);
	
	
	public Map<String, String> getConfiguration(){
		return null;
	}
	
	public void setConfiguration(Map<String, String> configuration){};
	
	protected int getPropertyIndex(String[] names) {
		int index = -1;
		int nameIndex = 0;
		
		while (index == -1 && nameIndex < names.length) {
			try {
				index = adapter.getPropertyIndex(names[nameIndex++]);
			}
			catch (Exception e) {}
		}
		
		if (index == -1)
			index = 0;
		
		return index;
	}
}
