/*
 *  Copyright 2010 chris.
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
import java.util.Map.Entry;
import java.util.Set;
import org.gitools.idmapper.MappingContext;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingException;
import org.gitools.idmapper.MappingNode;
import org.gitools.kegg.soap.KEGGPortType;

public class KeggGenesMapper extends AbstractKeggMapper {

	private static final Map<String, String> fileKey = new HashMap<String, String>();
	static {
		fileKey.put("entrez:genes", "ncbi-geneid");
		fileKey.put("uniprot:proteins", "uniprot");
		fileKey.put("pdb:proteins", "pdb");
		fileKey.put("ensembl:genes", "ensembl");
	}

	public KeggGenesMapper(KEGGPortType service, String organismId) {
		super("KeggGenes", service, organismId);
	}

	@Override
	public void initialize(MappingContext context, IProgressMonitor monitor) throws MappingException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		monitor.begin("Getting mapping information from KEGG FTP ...", 1);

		// Get map from the FTP
		try {
			String prefix = fileKey.get(dst.getId());
			if (!src.getId().equals("kegg:genes") || prefix == null)
				throw new MappingException("Unsupported mapping from " + src + " to " + dst);

			if (prefix.equals("ensembl"))
				prefix = prefix + "-" + organismId;

			URL url = new URL("ftp://ftp.genome.jp/pub/kegg/genes/organisms/"
					+ organismId + "/" + organismId + "_" + prefix + ".list");

			monitor.info(url.toString());

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(url.openStream()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\t");
				if (fields.length != 2)
					continue;
				Set<String> b = map.get(fields[0]);
				if (b == null) {
					b = new HashSet<String>();
					map.put(fields[0], b);
				}
				b.add(fields[1].substring(prefix.length()));
			}
		}
		catch (Exception ex) {
			monitor.exception(ex);
		}

		monitor.end();

		Set<Entry<String, Set<String>>> eset = data.getMap().entrySet();
		monitor.begin("Mapping KEGG genes ...", eset.size());

		// Do mapping
		for (Map.Entry<String, Set<String>> e : eset) {
			Set<String> dset = new HashSet<String>();
			for (String sname : e.getValue()) {
				Set<String> dnames = map.get(sname);
				if (dnames != null)
					dset.addAll(dnames);
			}
			data.set(e.getKey(), dset);
		}
		
		return data;
	}

	@Override
	public void finalize(MappingContext context, IProgressMonitor monitor) throws MappingException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}
}
