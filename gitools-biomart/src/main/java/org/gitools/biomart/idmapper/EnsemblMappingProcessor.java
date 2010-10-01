/*
 *  Copyright 2010 cperez.
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.idmapper.AbstractMappingProcessor;
import org.gitools.idmapper.IdMappingContext;
import org.gitools.idmapper.IdMappingException;
import org.gitools.idmapper.IdMappingNode;


public class EnsemblMappingProcessor extends AbstractMappingProcessor {

	public static final String BIOMART_SOURCE_ID = "ensembl.biomart.source.id";

	private BiomartSource source;
	private BiomartRestfulService service;
	private MartLocation mart;


	public EnsemblMappingProcessor() {
		super("Ensembl");
	}

	public BiomartSource[] getEnsemblSources() {
		LinkedList<BiomartSource> sources = new LinkedList<BiomartSource>();

		BiomartSource latestVersion = null;
		for (BiomartSource src : BiomartSourceManager.getDefault().getSources()) {
			if (src.getName().matches(".*ensembl.*")) {
				if (src.getName().matches(".*last.*"))
					latestVersion = src;
				else
					sources.add(src);
			}
		}

		if (latestVersion != null)
			sources.add(0, latestVersion);

		return sources.toArray(new BiomartSource[sources.size()]);
	}

	@Override
	public void initialize(IdMappingContext context) throws IdMappingException {
		String sourceId = context.getString(BIOMART_SOURCE_ID);
		if (sourceId == null)
			throw new IdMappingException("Context property " + BIOMART_SOURCE_ID + " not defined.");

		source = getEnsemblSource(sourceId);

		try {
			MartLocation mart = getMartLocation();

			
		}
		catch (Exception ex) {
			throw new IdMappingException(ex);
		}


	}

	private BiomartRestfulService getService() throws BiomartServiceException {
		if (service != null)
			return service;

		return service = BiomartServiceFactory.createRestfulService(source);
	}

	private MartLocation getMartLocation() throws BiomartServiceException, IdMappingException {
		if (mart != null)
			return mart;

		BiomartRestfulService bs = getService();
		List<MartLocation> registry = bs.getRegistry();
		Iterator<MartLocation> it = registry.iterator();

		while (mart == null && it.hasNext()) {
			MartLocation m = it.next();
			if (m.getName().equals("ENSEMBL_MART_ENSEMBL"))
				mart = m;
		}

		if (mart == null)
			throw new IdMappingException("Ensembl Genes database not found in the registry.");

		return mart;
	}

	private BiomartSource getEnsemblSource(String id) {
		for (BiomartSource src : BiomartSourceManager.getDefault().getSources()) {
			if (src.getName().equals(id))
				return src;
		}
		return null;
	}

	@Override
	public Map<String, String> map(IdMappingContext context, Map<String, String> input, IdMappingNode src, IdMappingNode dst) throws IdMappingException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void finalize(IdMappingContext context) throws IdMappingException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
