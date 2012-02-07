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

package org.gitools.kegg.idmapper;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.gitools.idmapper.MappingContext;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingException;
import org.gitools.idmapper.MappingNode;
import org.gitools.kegg.soap.KEGGPortType;
import org.gitools.kegg.soap.LinkDBRelation;

public class KeggGenesMapper extends AbstractKeggMapper implements AllIds {

	public static final String NCBI_DB = "ncbi-geneid";
	public static final String UNIPROT_DB = "uniprot";
	public static final String PDB_DB = "pdb";
	public static final String ENSEMBL_DB = "ensembl";

	private static final Map<String, String> fileKey = new HashMap<String, String>();
	static {
		fileKey.put(NCBI_GENES, NCBI_DB);
		fileKey.put(UNIPROT, UNIPROT_DB);
		fileKey.put(PDB, PDB_DB);
		fileKey.put(ENSEMBL_GENES, ENSEMBL_DB);
	}

	public KeggGenesMapper(KEGGPortType service, String organismId) {
		super("KeggGenes", false, true, service, organismId);
	}

	@Override
	public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		monitor.begin("Getting mapping information from KEGG ...", 1);

		// TODO Filter out items not in data if data is not empty
		
		// Get map from the API
		try {
			String prefix = fileKey.get(dst.getId());
			if (!KEGG_GENES.equals(src.getId()) || prefix == null)
				throw new MappingException("Unsupported mapping from " + src + " to " + dst);

			if (prefix.equals(ENSEMBL_DB))
				prefix = prefix + "-" + organismId;

			int numGenes = service.get_number_of_genes_by_organism(organismId);
			LinkDBRelation[] relations =
					service.get_linkdb_by_entry(organismId, prefix, 0, numGenes);

			int plen = prefix.length() + 1;
			for (LinkDBRelation rel : relations) {
				String srcId = rel.getEntry_id1();
				String dstId = rel.getEntry_id2();
				Set<String> b = map.get(srcId);
				if (b == null) {
					b = new HashSet<String>();
					map.put(srcId, b);
				}
				b.add(dstId.substring(plen));
			}
		}
		catch (Exception ex) {
			throw new MappingException(ex);
		}

		monitor.end();

		monitor.begin("Mapping KEGG genes ...", 1);

		if (data.isEmpty())
			data.identity(map.keySet());
		
		data.map(map);

		monitor.end();
		
		return data;
	}
}
