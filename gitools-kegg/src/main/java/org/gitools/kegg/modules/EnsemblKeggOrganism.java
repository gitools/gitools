package org.gitools.kegg.modules;

import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;

public class EnsemblKeggOrganism extends RefImpl implements Organism {

	private MartLocation mart;
	private DatasetInfo dataset;
	private String keggId;

	public EnsemblKeggOrganism(String id, String name, MartLocation mart, DatasetInfo dataset) {
		super(id, name);
		this.mart = mart;
		this.dataset = dataset;
	}

	public MartLocation getMart() {
		return mart;
	}

	public DatasetInfo getDataset() {
		return dataset;
	}

	public String getKeggId() {
		return keggId;
	}

	public void setKeggId(String keggId) {
		this.keggId = keggId;
	}

	@Override
	public String getRef() {
		return mart.getName() + ":" + dataset.getName() + ":" + super.getRef();
	}
}
