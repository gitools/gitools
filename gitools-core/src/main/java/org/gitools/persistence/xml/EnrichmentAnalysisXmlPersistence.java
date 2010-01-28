package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.model.xml.adapter.PersistenceReferenceXmlAdapter;
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

		/*return new XmlAdapter[] {
				new DoubleMatrixXmlAdapter(context),
				new ModuleMapXmlAdapter(context),
				new ObjectMatrixXmlAdapter(context) };*/
	}

	@Override
	protected void afterRead(File file, EnrichmentAnalysis entity,
			IProgressMonitor monitor) throws PersistenceException {

		if (!isRecursivePersistence())
			return;

		
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
		
		/*if (!isRecursivePersistence())
			return;

		PersistenceContext context = getPersistenceContext();

		File baseFile = file.getParentFile();
		String baseName = PersistenceUtils.getBaseName(file.getName());

		// Modules
		File modFile = new File(baseFile, baseName + ".modules.gz");
		ModuleMapTextIndicesPersistence modPersist =
				new ModuleMapTextIndicesPersistence();
		modPersist.write(modFile, entity.getModuleMap(), monitor);

		String modPath = PersistenceUtils.getRelativePath(
				baseFile.getAbsolutePath(), modFile.getAbsolutePath());
		context.put(entity.getModuleMap(), modPath);

		// Data
		File dataFile = new File(baseFile, baseName + ".data.gz");
		PersistenceManager.getDefault().store(dataFile, entity.getDataTable(), monitor);

		String mimeType = MimeTypeManager.getDefault().fromClass(entity.getDataTable().getClass());
		String dataPath = PersistenceUtils.getRelativePath(
				baseFile.getAbsolutePath(), dataFile.getAbsolutePath());
		//MatrixXmlElement matrixXml = new MatrixXmlElement(mimeType, dataPath);
		context.put(entity.getDataTable(), matrixXml);

		/*DoubleMatrixTextPersistence dataPersist = new DoubleMatrixTextPersistence();
		dataPersist.write(dataFile, entity.getDataTable(), monitor);*/


		// Results
		/*File resFile = new File(baseFile, baseName + ".results.gz");
		ObjectMatrixTextPersistence resPersist = new ObjectMatrixTextPersistence();
		resPersist.write(resFile, entity.getResultsMatrix(), monitor);

		String resPath = PersistenceUtils.getRelativePath(
				baseFile.getAbsolutePath(), resFile.getAbsolutePath());
		context.put(entity.getResultsMatrix(), resPath);*/
	}
}
