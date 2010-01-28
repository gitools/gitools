package org.gitools.persistence.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.model.xml.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.persistence.PersistenceContext;

public class HeatmapXmlPersistence
		extends AbstractXmlPersistence<Heatmap> {

	public HeatmapXmlPersistence() {
		super(Heatmap.class);
	}

	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		PersistenceContext context = getPersistenceContext();
		return new XmlAdapter<?, ?>[] {
			new PersistenceReferenceXmlAdapter(context)
		};
	}
}
