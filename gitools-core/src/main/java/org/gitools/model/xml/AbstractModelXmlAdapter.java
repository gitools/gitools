package org.gitools.model.xml;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.AbstractModel;

@Deprecated //FIXME Esta clase tiene muy mala pinta !!!!!!!!!!!
public class AbstractModelXmlAdapter
		extends XmlAdapter<AbstractModelDecoratorElement, List<AbstractModel>> {
	
	@Override
	public AbstractModelDecoratorElement marshal(List<AbstractModel> elems)
			throws Exception {
		return new AbstractModelDecoratorElement(elems);
	}

	@Override
	public List<AbstractModel> unmarshal(AbstractModelDecoratorElement v)
			throws Exception {
		 return v.getList();
	}

}
