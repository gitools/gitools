package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.model.xml.PersistenceReferenceXmlAdapter;
import org.gitools.persistence.PersistenceContext;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;

public class EnrichmentAnalysisXmlPersistence
		extends AbstractXmlPersistence<EnrichmentAnalysis> {

	public EnrichmentAnalysisXmlPersistence() {
		super(EnrichmentAnalysis.class);

		setPersistenceTitle("enrichment configuration");
	}
	
	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		PersistenceContext context = getPersistenceContext();
		return new XmlAdapter<?, ?>[] {
			new PersistenceReferenceXmlAdapter(context)
		};
	}

	@Override
	protected void beforeRead(File file, IProgressMonitor monitor) throws PersistenceException {
		File baseFile = file.getParentFile();

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);
	}

	@Override
	protected void beforeWrite(File file, EnrichmentAnalysis entity,
			IProgressMonitor monitor) throws PersistenceException {

		File baseFile = file.getParentFile();
		String baseName = PersistenceUtils.getBaseName(file.getName());

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);

		//context.setMimeType(entity.getDataTable(), MimeTypes.DOUBLE_MATRIX);
		context.setFilePath(entity.getDataMatrix(),
				new File(baseFile, baseName + ".data.gz").getAbsolutePath());

		context.setFilePath(entity.getModuleMap(),
				new File(baseFile, baseName + ".modules.gz").getAbsolutePath());

		context.setFilePath(entity.getResultsMatrix(),
				new File(baseFile, baseName + ".results.gz").getAbsolutePath());
	}
}
