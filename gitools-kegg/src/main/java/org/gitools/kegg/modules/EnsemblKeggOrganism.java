/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.kegg.modules;

import org.gitools.modules.importer.Organism;
import org.gitools.modules.importer.RefImpl;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.kegg.soap.Definition;

public class EnsemblKeggOrganism extends RefImpl implements Organism {

	private DatasetInfo ensemblDataset;
	private Definition keggDef;

	public EnsemblKeggOrganism(String id, String name, DatasetInfo dataset) {
		super(id, name);
		this.ensemblDataset = dataset;
	}

	public EnsemblKeggOrganism(String id, String name, Definition def) {
		super(id, name);
		this.keggDef = def;
	}

	public DatasetInfo getEnsemblDataset() {
		return ensemblDataset;
	}

	public void setEnsemblDataset(DatasetInfo ensemblDataset) {
		this.ensemblDataset = ensemblDataset;
	}

	public Definition getKeggDef() {
		return keggDef;
	}

	public void setKeggDef(Definition keggDef) {
		this.keggDef = keggDef;
	}

	@Override
	public String getRef() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getRef()).append("[");
		if (ensemblDataset != null)
			sb.append("ENSEMBL");
		if (ensemblDataset != null && keggDef != null)
			sb.append(", ");
		if (keggDef != null)
			sb.append("KEGG");
		sb.append("]");
		return sb.toString();
	}
}
