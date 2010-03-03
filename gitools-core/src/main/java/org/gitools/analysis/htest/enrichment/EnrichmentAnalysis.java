package org.gitools.analysis.htest.enrichment;


import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.analysis.htest.HtestAnalysis;

import org.gitools.model.ModuleMap;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnrichmentAnalysis extends HtestAnalysis implements Serializable {

	private static final long serialVersionUID = -7476200948081644842L;
	
	/** Discard data rows without any module annotation */
	protected boolean discardNonMappedRows;

	/** Modules */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected ModuleMap moduleMap;

	/** Minimum module size */
	protected int minModuleSize;

	/** Maximum module size */
	protected int maxModuleSize;
	
	public EnrichmentAnalysis() {
	}

	public boolean isDiscardNonMappedRows() {
		return discardNonMappedRows;
	}

	public void setDiscardNonMappedRows(boolean discardNonMappedRows) {
		this.discardNonMappedRows = discardNonMappedRows;
	}

	public ModuleMap getModuleMap() {
		return moduleMap;
	}

	public void setModuleMap(ModuleMap moduleMap) {
		this.moduleMap = moduleMap;
	}

	public int getMinModuleSize() {
		return minModuleSize;
	}

	public void setMinModuleSize(int minModuleSize) {
		this.minModuleSize = minModuleSize;
	}

	public int getMaxModuleSize() {
		return maxModuleSize;
	}

	public void setMaxModuleSize(int maxModuleSize) {
		this.maxModuleSize = maxModuleSize;
	}
}
