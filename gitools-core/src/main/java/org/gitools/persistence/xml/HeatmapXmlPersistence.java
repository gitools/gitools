package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.persistence.PersistenceContext;
import org.gitools.persistence.PersistenceEntityContext;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;

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

	@Override
	protected void beforeWrite(File file, Heatmap entity, IProgressMonitor monitor) throws PersistenceException {

		file = file.getAbsoluteFile();
		File baseFile = file.getParentFile();
		String baseName = PersistenceUtils.getFileName(file.getName());

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);

		//context.setMimeType(entity.getDataTable(), MimeTypes.DOUBLE_MATRIX);
		context.setEntityContext(entity.getMatrixView().getContents(), new PersistenceEntityContext(
				new File(baseFile, baseName + ".data.gz").getAbsolutePath()));

		context.setEntityContext(entity.getRowHeader().getAnnotations(), new PersistenceEntityContext(
				new File(baseFile, baseName + ".row.annotations.gz").getAbsolutePath()));

		context.setEntityContext(entity.getColumnHeader().getAnnotations(), new PersistenceEntityContext(
				new File(baseFile, baseName + ".column.annotations.gz").getAbsolutePath()));
	}


}