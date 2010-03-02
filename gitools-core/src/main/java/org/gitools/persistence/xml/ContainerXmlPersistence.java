package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.Container;
import org.gitools.model.xml.FileXmlAdapter;
import org.gitools.persistence.PersistenceException;

public class ContainerXmlPersistence
		extends AbstractXmlPersistence<Container> {

	private File baseFile;

	public ContainerXmlPersistence() {	
		super(Container.class);
	}
	
	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter[] {
				new FileXmlAdapter(baseFile) };
	}

	@Override
	protected void beforeRead(File file, IProgressMonitor monitor) throws PersistenceException {
		baseFile = file.getParentFile();
	}

	@Override
	protected void beforeWrite(File file, Container entity, IProgressMonitor monitor) throws PersistenceException {
		baseFile = file.getParentFile();
	}
}
