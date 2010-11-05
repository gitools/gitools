package org.gitools.model.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.model.HeatmapHeader;

@Deprecated
@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractModelDecoratorElement {
	@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	private int order[];

	@XmlElementWrapper(name = "headerDecoratorType")
	@XmlElement(name = "headerDecorator")
	private List<HeatmapHeader> headerDecorators = new ArrayList<HeatmapHeader>();

	@XmlElementWrapper(name = "elementDecoratorType")
	@XmlElement(name = "elementDecorator")
	//@XmlJavaTypeAdapter(ElementDecoratorXmlAdapter.class)
	private List<ElementDecorator> elementDecorators = new ArrayList<ElementDecorator>();

	public AbstractModelDecoratorElement() {

	}

	public AbstractModelDecoratorElement(List<AbstractModel> elems) {
		int size = elems.size();
		order = new int[size];
		int nHeaders = 0; 		
		int nElems = 0; 		
		int i = 0;
		
		for (AbstractModel elem : elems) {
			if (elem instanceof HeatmapHeader) {
				headerDecorators.add((HeatmapHeader) elem);
				order[i] = nHeaders;
				nHeaders++;
			} else if (elem instanceof ElementDecorator) {
				elementDecorators.add((ElementDecorator) elem);
				order[i] = size + nElems;
				nElems++;
			}
			i++;
		}
	}

	public List<AbstractModel> getList() {
		List<AbstractModel> decorators = new ArrayList<AbstractModel>();
		int size = order.length;
		for (int elem : order) {
			if (elem >= size){
				decorators.add(elementDecorators.get(elem - size));
			}else{
				decorators.add(headerDecorators.get(elem));
			}
		}
		return decorators;
	}

}
