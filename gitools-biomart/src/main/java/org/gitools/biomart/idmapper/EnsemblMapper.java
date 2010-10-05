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

import edu.upf.bg.progressmonitor.IProgressMonitor;
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
import org.gitools.idmapper.AbstractMapper;
import org.gitools.idmapper.MappingContext;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingException;
import org.gitools.idmapper.MappingNode;


public class EnsemblMapper extends AbstractMapper {

	public static final String BIOMART_SOURCE_ID = "ensembl.biomart.source.id";

	private BiomartSource source;
	private BiomartRestfulService service;
	private MartLocation mart;


	public EnsemblMapper(String organismDataset) {
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
	public void initialize(MappingContext context, IProgressMonitor monitor) throws MappingException {
		String sourceId = context.getString(BIOMART_SOURCE_ID);
		if (sourceId == null)
			throw new MappingException("Context property " + BIOMART_SOURCE_ID + " not defined.");

		source = getEnsemblSource(sourceId);

		try {
			MartLocation mart = getMartLocation();

			
		}
		catch (Exception ex) {
			throw new MappingException(ex);
		}


	}

	private BiomartRestfulService getService() throws BiomartServiceException {
		if (service != null)
			return service;

		return service = BiomartServiceFactory.createRestfulService(source);
	}

	private MartLocation getMartLocation() throws BiomartServiceException, MappingException {
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
			throw new MappingException("Ensembl Genes database not found in the registry.");

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
	public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void finalize(MappingContext context, IProgressMonitor monitor) throws MappingException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
/*biomartService.queryModule(query, new SequentialTableWriter() {
					@Override public void open() throws Exception { }

					@Override public void close() { }

					@Override public void write(String[] rowFields) throws Exception {

					}
				}, monitor);*/