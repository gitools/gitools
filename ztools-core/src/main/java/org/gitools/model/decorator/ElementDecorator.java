package org.gitools.model.decorator;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.model.decorator.impl.PValueElementDecorator;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.xml.adapter.ElementDecoratorXmlAdapter;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ElementDecorator extends AbstractModel {

	private static final long serialVersionUID = -2101303088018509837L;

	@XmlTransient
	protected IElementAdapter adapter;
	
	public ElementDecorator(){
		
	}
	
	public ElementDecorator(IElementAdapter adapter) {
		this.adapter = adapter;
	}
	
	public IElementAdapter getAdapter() {
		return adapter;
	}
	
	public void setAdapter(IElementAdapter adapter){
		this.adapter = adapter;
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
