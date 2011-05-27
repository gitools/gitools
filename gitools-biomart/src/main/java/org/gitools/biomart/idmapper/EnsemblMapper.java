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

package org.gitools.biomart.idmapper;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gitools.biomart.BiomartService;
import org.gitools.biomart.restful.model.Attribute;
import org.gitools.biomart.restful.model.Dataset;
import org.gitools.biomart.restful.model.Query;
import org.gitools.biomart.queryhandler.BiomartQueryHandler;
import org.gitools.idmapper.AbstractMapper;
import org.gitools.idmapper.MappingContext;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingException;
import org.gitools.idmapper.MappingNode;


public class EnsemblMapper extends AbstractMapper implements AllIds {

	private BiomartService service;
	private String dataset;

	public EnsemblMapper(BiomartService service, String dataset) {
		super("Ensembl", false, true);

		this.service = service;
		this.dataset = dataset;
	}

	@Override
	public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
		String srcInternalName = getInternalName(src.getId());
		String dstInternalName = getInternalName(dst.getId());
		if (srcInternalName == null || dstInternalName == null)
			throw new MappingException("Unsupported mapping from " + src + " to " + dst);

		monitor.begin("Getting mappings from Ensembl ...", 1);

		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		Query q = createQuery(dataset, srcInternalName, dstInternalName);
		try {
			service.queryModule(q, new BiomartQueryHandler() {
				@Override public void begin() throws Exception { }
				@Override public void end() { }

				@Override public void line(String[] rowFields) throws Exception {
					String srcf = rowFields[0];
					String dstf = rowFields[1];
					Set<String> items = map.get(srcf);
					if (items == null) {
						items = new HashSet<String>();
						map.put(srcf, items);
					}
					items.add(dstf);
				}
			}, monitor);
		}
		catch (Exception ex) {
			throw new MappingException(ex);
		}

		monitor.end();

		monitor.begin("Mapping Ensembl IDs...", 1);

		if (data.isEmpty())
			data.identity(map.keySet());

		data.map(map);

		monitor.end();

		return data;
	}

	private static final Map<String, String> inameMap = new HashMap<String, String>();
	static {
		inameMap.put(ENSEMBL_GENES, "ensembl_gene_id");
		inameMap.put(ENSEMBL_TRANSCRIPTS, "ensembl_transcript_id");
		inameMap.put(ENSEMBL_PROTEINS, "ensembl_peptide_id");

		inameMap.put(PDB, "pdb");
		inameMap.put(NCBI_REFSEQ, "embl");
		inameMap.put(NCBI_GENES, "entrezgene");
		inameMap.put(NCBI_UNIGENE, "unigene");
		inameMap.put(UNIPROT, "uniprot_swissprot_accession");

		inameMap.put(GO_BP, "go_biological_process_id");
		inameMap.put(GO_MF, "go_molecular_function_id");
		inameMap.put(GO_CL, "go_cellular_component_id");
		inameMap.put(GO_ID, "go_id");
	}

	public static String getInternalName(String id) {
		String iname = inameMap.get(id);
		if (iname == null && id.startsWith("ensembl:"))
			return id;
		return iname;
	}

	public static Query createQuery(String dataset, String srcInternalName, String dstInternalName) {
		Query q = new Query();
		q.setVirtualSchemaName("default");
		q.setUniqueRows(1);
		Dataset ds = new Dataset();
		ds.setName(dataset);
		List<Attribute> attrs = ds.getAttribute();
		Attribute srcAttr = new Attribute();
		srcAttr.setName(srcInternalName);
		attrs.add(srcAttr);
		Attribute dstAttr = new Attribute();
		dstAttr.setName(dstInternalName);
		attrs.add(dstAttr);

		q.getDatasets().add(ds);

		return q;
	}
}