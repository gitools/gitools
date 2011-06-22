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

public class KeggGenesMapper extends AbstractKeggMapper implements AllIds {

	public static final String FTP_HOST = "ftp.genome.jp";
	public static final String FTP_PATH = "/pub/kegg/genes/organisms";

	public static final String FTP_BASE_URL = "ftp://" + FTP_HOST + FTP_PATH + "/";

	public static final String NCBI_FTP_FILE_PREFIX = "ncbi-geneid";
	public static final String UNIPROT_FTP_FILE_PREFIX = "uniprot";
	public static final String PDB_FTP_FILE_PREFIX = "pdb";
	public static final String ENSEMBL_FTP_FILE_PREFIX = "ensembl";

	private static final Map<String, String> fileKey = new HashMap<String, String>();
	static {
		fileKey.put(NCBI_GENES, NCBI_FTP_FILE_PREFIX);
		fileKey.put(UNIPROT, UNIPROT_FTP_FILE_PREFIX);
		fileKey.put(PDB, PDB_FTP_FILE_PREFIX);
		fileKey.put(ENSEMBL_GENES, ENSEMBL_FTP_FILE_PREFIX);
	}

	public KeggGenesMapper(KEGGPortType service, String organismId) {
		super("KeggGenes", false, true, service, organismId);
	}

	@Override
	public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		monitor.begin("Getting mapping information from KEGG FTP ...", 1);

		// TODO Filter out items not in data if data is not empty
		
		// Get map from the FTP
		try {
			String prefix = fileKey.get(dst.getId());
			if (!KEGG_GENES.equals(src.getId()) || prefix == null)
				throw new MappingException("Unsupported mapping from " + src + " to " + dst);

			if (prefix.equals(ENSEMBL_FTP_FILE_PREFIX))
				prefix = prefix + "-" + organismId;

			URL url = new URL(FTP_BASE_URL
					+ organismId + "/" + organismId + "_" + prefix + ".list");

			monitor.info(url.toString());

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(url.openStream()));

			int plen = prefix.length() + 1;
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\t");
				String sf = fields[0];
				String df = fields[1];
				if (fields.length != 2)
					continue;
				Set<String> b = map.get(sf);
				if (b == null) {
					b = new HashSet<String>();
					map.put(sf, b);
				}
				b.add(df.substring(plen));
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
