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
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.gitools.idmapper.MappingContext;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingException;
import org.gitools.idmapper.MappingNode;
import org.gitools.kegg.soap.Definition;
import org.gitools.kegg.soap.KEGGPortType;

public class KeggPathwaysMapper extends AbstractKeggMapper implements AllIds {

	public KeggPathwaysMapper(KEGGPortType service, String organismId) {
		super("KeggPathways", false, true, service, organismId);
	}

	@Override
	public void initialize(MappingContext context, IProgressMonitor monitor) throws MappingException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
		if (!KEGG_PATHWAYS.equals(src.getId()))
			throw new MappingException("Unsupported mapping from " + src + " to " + dst);
		
		if (data.isEmpty()) {
			monitor.begin("Getting KEGG pathways ...", 1);
			try {
				Definition[] pathwaysDefs = service.list_pathways(organismId);
				for (Definition d : pathwaysDefs)
					data.put(d.getEntry_id(), d.getEntry_id());
			}
			catch (RemoteException ex) {
				throw new MappingException(ex);
			}
			monitor.end();
		}

		if (monitor.isCancelled())
			return null;

		Map<String, Set<String>> pathwaysMap = new HashMap<String, Set<String>>();
		Set<String> dstIds = data.getDstIds();
		monitor.begin("Getting KEGG genes ...", dstIds.size());
		try {
			int count = 0;
			for (String dstId : dstIds) {
				monitor.info(dstId);
				if (monitor.isCancelled())
					return null;

				if (count++ > 10)
					break;

				String[] genes = service.get_genes_by_pathway(dstId);
				pathwaysMap.put(dstId, new HashSet<String>(Arrays.asList(genes)));
				monitor.worked(1);
			}
		}
		catch (RemoteException ex) {
			throw new MappingException(ex);
		}
		monitor.end();

		data.clearDstIds();

		monitor.begin("Mapping KEGG pathways to KEGG genes ...", dstIds.size());
		for (String srcId : data.getSrcIds()) {
			monitor.info(srcId);
			if (monitor.isCancelled())
				return null;
			
			Set<String> pathways = data.get(srcId);
			Set<String> genes = new HashSet<String>();
			Iterator<String> it = pathways.iterator();
			while (it.hasNext()) {
				Set<String> pg = pathwaysMap.get(it.next());
				if (pg != null)
					genes.addAll(pg);
			}
			data.set(srcId, genes);
			monitor.worked(1);
		}
		monitor.end();

		return data;
	}

	@Override
	public void finalize(MappingContext context, IProgressMonitor monitor) throws MappingException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
