package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.persistence.PersistenceContext;
import org.gitools.persistence.PersistenceEntityContext;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.PersistenceUtils;

public class EnrichmentAnalysisXmlPersistence
		extends AbstractXmlPersistence<EnrichmentAnalysis> {

	public EnrichmentAnalysisXmlPersistence() {
		super(EnrichmentAnalysis.class);

		setPersistenceTitle("enrichment analysis");
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
		String baseName = PersistenceUtils.getFileName(file.getName());

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);

		PersistenceManager pm = getPersistenceManager();

		String dataExt = pm.getExtensionFromEntity(entity.getData().getClass());
		context.setEntityContext(entity.getData(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-data." + dataExt + ".gz").getAbsolutePath(), false));

		context.setEntityContext(entity.getModuleMap(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-modules.ixm.gz").getAbsolutePath(), false));

		String resultsExt = pm.getExtensionFromEntity(entity.getResults().getClass());
		context.setEntityContext(entity.getResults(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-results." + resultsExt + ".gz").getAbsolutePath()));
	}
}
