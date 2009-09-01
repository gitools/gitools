package org.gitools.model.xml.adapter;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.model.xml.AbstractModelDecoratorElement;

public class AbstractModelXmlAdapter extends
		XmlAdapter<AbstractModelDecoratorElement, List<AbstractModel>> {
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
