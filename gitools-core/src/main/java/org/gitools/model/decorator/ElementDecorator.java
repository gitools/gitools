package org.gitools.model.decorator;

import edu.upf.bg.colorscale.IColorScale;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.model.AbstractModel;
import org.gitools.matrix.model.element.IElementAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ElementDecorator extends AbstractModel {

	private static final long serialVersionUID = -2101303088018509837L;

	@XmlTransient
	protected IElementAdapter adapter;
	
	public ElementDecorator() {
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

	public abstract IColorScale getScale();

	//FIXME use JAXB to save state
	public Map<String, String> getConfiguration() {
		return null;
	}
	
	public abstract void setConfiguration(Map<String, String> configuration);
	
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
