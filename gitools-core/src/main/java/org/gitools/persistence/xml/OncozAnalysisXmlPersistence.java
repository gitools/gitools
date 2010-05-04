package org.gitools.persistence.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.persistence.PersistenceContext;

public class OncozAnalysisXmlPersistence
		extends AbstractXmlPersistence<OncodriveAnalysis> {

	public OncozAnalysisXmlPersistence() {
		super(OncodriveAnalysis.class);
	}
	
	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		PersistenceContext context = getPersistenceContext();
		return new XmlAdapter<?, ?>[] {
			new PersistenceReferenceXmlAdapter(context)
		};
	}

}
