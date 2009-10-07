package org.gitools.persistence.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.analysis.EnrichmentAnalysis;
import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.FileObjectXmlAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.ModuleMapXmlAdapter;

public class EnrichmentAnalysisXmlPersistence
		extends AbstractXmlPersistence<EnrichmentAnalysis> {

	public EnrichmentAnalysisXmlPersistence() throws JAXBException {
		super(EnrichmentAnalysis.class);
	}
	
	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter[] {
				new FileObjectXmlAdapter(fileObjectResolver),
				new AnnotationMatrixXmlAdapter(fileObjectResolver),
				new MatrixXmlAdapter(fileObjectResolver),
				new ModuleMapXmlAdapter(fileObjectResolver)	};
	}

}
