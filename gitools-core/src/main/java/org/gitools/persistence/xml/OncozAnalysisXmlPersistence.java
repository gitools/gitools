package org.gitools.persistence.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.analysis.htest.oncozet.OncozAnalysis;
import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.FileXmlAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.ModuleMapXmlAdapter;

public class OncozAnalysisXmlPersistence
		extends AbstractXmlPersistence<OncozAnalysis> {

	public OncozAnalysisXmlPersistence() throws JAXBException {
		super(OncozAnalysis.class);
	}
	
	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter[] {
				new FileXmlAdapter(pathResolver),
				new AnnotationMatrixXmlAdapter(pathResolver),
				new MatrixXmlAdapter(pathResolver),
				new ModuleMapXmlAdapter(pathResolver)	};
	}

}
