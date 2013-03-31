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

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.idmapper.MappingContext;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingException;
import org.gitools.idmapper.MappingNode;
import org.gitools.kegg.service.domain.KeggPathway;
import org.gitools.kegg.service.KeggService;

import java.util.*;

public class KeggPathwaysMapper extends AbstractKeggMapper implements AllIds {

	public KeggPathwaysMapper(KeggService service, String organismId) {
		super("KeggPathways", false, true, service, organismId);
	}

	@Override
	public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
		if (!KEGG_PATHWAYS.equals(src.getId()))
			throw new MappingException("Unsupported mapping from " + src + " to " + dst);
		
		if (data.isEmpty()) {
			monitor.begin("Getting KEGG pathways ...", 1);
			try {
				List<KeggPathway> pathwaysDefs = service.getPathways(organismId);
				for (KeggPathway d : pathwaysDefs)
					data.put(d.getId(), d.getId());
			}
			catch (Exception ex) {
				throw new MappingException(ex);
			}
			monitor.end();
		}

		if (monitor.isCancelled())
			return null;

		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		Set<String> dstIds = data.getDstIds();
		monitor.begin("Getting KEGG genes ...", dstIds.size());
		try {
			//int count = 0;
			for (String dstId : dstIds) {
				monitor.info(dstId);
				if (monitor.isCancelled())
					return null;

				//if (count++ > 10) //FIXME
				//	break;

				List<String> genes = service.getGenesByPathway(dstId);
				map.put(dstId, new HashSet<String>(genes));
				monitor.worked(1);
			}
		}
		catch (Exception ex) {
			throw new MappingException(ex);
		}
		monitor.end();



		monitor.begin("Mapping KEGG pathways to KEGG genes ...", dstIds.size());

		data.map(map);

		monitor.end();

		return data;
	}

}
