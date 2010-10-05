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

package org.gitools.kegg.modules;

import org.gitools.kegg.idmapper.KeggGenesMapper;
import org.gitools.kegg.idmapper.KeggPathwaysMapper;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.ServiceException;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.Attribute;
import org.gitools.biomart.restful.model.AttributeCollection;
import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.AttributeGroup;
import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.Dataset;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.restful.model.Query;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.biomart.utils.tablewriter.SequentialTableWriter;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingEngine;
import org.gitools.kegg.soap.Definition;
import org.gitools.kegg.soap.KEGGLocator;
import org.gitools.kegg.soap.KEGGPortType;
import org.gitools.model.ModuleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnsemblKeggModulesImporter implements ModulesImporter {

	private static Logger logger = LoggerFactory.getLogger(EnsemblKeggModulesImporter.class);

	// KEGG

	public static final EnsemblKeggModuleCategory[] KEGG_MODULE_CATEGORIES = new EnsemblKeggModuleCategory[] {
		new EnsemblKeggModuleCategory("KEGG", "kegg:pathways", "KEGG Pathways")
	};

	private static final EnsemblKeggFeatureCategory[] KEGG_FEATURES = new EnsemblKeggFeatureCategory[] {
		new EnsemblKeggFeatureCategory("Genes", "kegg:genes", "KEGG Genes"),
		new EnsemblKeggFeatureCategory("Genes", "entrez:genes", "Entrez Genes"),
		new EnsemblKeggFeatureCategory("Protein", "pdb:proteins", "PDB"),
		new EnsemblKeggFeatureCategory("Protein", "uniprot:proteins", "UniProt")
	};

	// Gene Ontology

	public static final EnsemblKeggModuleCategory[] GO_MODULE_CATEGORIES = new EnsemblKeggModuleCategory[] {
		new EnsemblKeggModuleCategory("Ensembl", "go:bp", "GO Biological Processes"),
		new EnsemblKeggModuleCategory("Ensembl", "go:mf", "GO Molecular functions"),
		new EnsemblKeggModuleCategory("Ensembl", "go:cl", "GO Cellular locations")
	};

	private static final EnsemblKeggFeatureCategory[] COMMON_FEATURES = new EnsemblKeggFeatureCategory[] {
		new EnsemblKeggFeatureCategory("Genes", "kegg:genes", "KEGG Genes"),
		new EnsemblKeggFeatureCategory("Genes", "entrez:genes", "Entrez Genes"),
		new EnsemblKeggFeatureCategory("Genes", "ensembl:genes", "Ensembl Genes"),
		new EnsemblKeggFeatureCategory("Genes", "ensembl:transcripts", "Ensembl Transcripts"),
		new EnsemblKeggFeatureCategory("Genes", "entrez:genes", "Entrez Genes"),
		new EnsemblKeggFeatureCategory("Protein", "pdb:proteins", "PDB"),
		new EnsemblKeggFeatureCategory("Protein", "uniprot:proteins", "UniProt"),
		new EnsemblKeggFeatureCategory("Protein", "ensembl:proteins", "Ensembl Proteins")
	};

	private static final Map<String, EnsemblKeggFeatureCategory> COMMON_FEATURES_MAP = new HashMap<String, EnsemblKeggFeatureCategory>();
	static {
		for (EnsemblKeggFeatureCategory f : COMMON_FEATURES)
			COMMON_FEATURES_MAP.put(f.getId(), f);
	}

	// selection state

	private EnsemblKeggVersion version;

	private EnsemblKeggOrganism organism;

	private EnsemblKeggModuleCategory modCategory;

	private EnsemblKeggFeatureCategory featCategory;

	private boolean keggEnabled;

	private boolean goEnabled;

	// internal state

	private BiomartRestfulService biomartService;

	private MartLocation martLocation;

	private KEGGPortType keggService;

	private Organism[] cachedOrganisms;

	private ModuleCategory[] moduleCategories;

	private FeatureCategory[] cachedFeatures;

	public EnsemblKeggModulesImporter(boolean keggEnabled, boolean goEnabled) {
		this.keggEnabled = keggEnabled;
		this.goEnabled = goEnabled;
		
		List<ModuleCategory> mc = new ArrayList<ModuleCategory>();

		if (keggEnabled)
			mc.addAll(Arrays.asList(KEGG_MODULE_CATEGORIES));

		if (goEnabled)
			mc.addAll(Arrays.asList(GO_MODULE_CATEGORIES));

		moduleCategories = mc.toArray(new ModuleCategory[mc.size()]);
	}

	private BiomartRestfulService getBiomartService() throws BiomartServiceException, ModulesImporterException {
		if (biomartService != null)
			return biomartService;

		if (version == null)
			throw new ModulesImporterException("Ensembl biomart source not defined.");
		
		return biomartService =
				BiomartServiceFactory.createRestfulService(version.getSource());
	}

	private MartLocation getMart() throws BiomartServiceException, ModulesImporterException {
		BiomartRestfulService bs = getBiomartService();
		List<MartLocation> registry = bs.getRegistry();
		Iterator<MartLocation> it = registry.iterator();

		martLocation = null;
		while (martLocation == null && it.hasNext()) {
			MartLocation m = it.next();
			if (m.getName().equals("ENSEMBL_MART_ENSEMBL"))
				martLocation = m;
		}

		return martLocation;
	}

	private KEGGPortType getKeggService() throws ServiceException {
		if (keggService != null)
			return keggService;

		KEGGLocator locator = new KEGGLocator();
		return keggService = locator.getKEGGPort();
	}

	@Override
	public Version[] getVersions() {
		LinkedList<Version> sources = new LinkedList<Version>();

		Version latestVersion = null;
		for (BiomartSource src : BiomartSourceManager.getDefault().getSources()) {
			if (src.getName().matches(".*ensembl.*")) {
				if (src.getName().matches(".*last.*"))
					latestVersion = new EnsemblKeggVersion(src);
				else
					sources.add(new EnsemblKeggVersion(src));
			}
		}

		if (latestVersion != null)
			sources.add(0, latestVersion);

		return sources.toArray(new Version[sources.size()]);
	}

	@Override
	public Organism[] getOrganisms() throws ModulesImporterException {
		if (cachedOrganisms != null)
			return cachedOrganisms;
		
		List<EnsemblKeggOrganism> orgs = new ArrayList<EnsemblKeggOrganism>();
		Map<String, EnsemblKeggOrganism> orgsMap = new HashMap<String, EnsemblKeggOrganism>();

		try {
			// Ensembl organisms

			BiomartRestfulService bs = getBiomartService();
			MartLocation mart = getMart();

			List<DatasetInfo> datasets = bs.getDatasets(mart);
			for (DatasetInfo ds : datasets) {
				String id = ds.getDisplayName().replaceAll(" genes [(].*[)]", "").toLowerCase();
				EnsemblKeggOrganism o = orgsMap.get(id);
				if (o == null) {
					o = new EnsemblKeggOrganism(id, id, ds);
					orgs.add(o);
					orgsMap.put(id, o);
				}
				else
					o.setEnsemblDataset(ds);
			}

			// KEGG organisms

			if (keggEnabled) {
				KEGGPortType serv = getKeggService();
				
				Definition[] orgDefs = serv.list_organisms();

				for (Definition def : orgDefs) {
					String id = def.getDefinition().replaceAll(" [(].*[)]", "").toLowerCase();
					EnsemblKeggOrganism o = orgsMap.get(id);
					if (o == null) {
						o = new EnsemblKeggOrganism(id, id, def);
						orgs.add(o);
						orgsMap.put(id, o);
					}
					else
						o.setKeggDef(def);
				}
			}
		}
		catch (Exception ex) {
			throw new ModulesImporterException(ex);
		}

		return cachedOrganisms = orgs.toArray(new EnsemblKeggOrganism[orgs.size()]);
	}

	@Override
	public ModuleCategory[] getModuleCategories() {
		return moduleCategories;
	}

	@Override
	public FeatureCategory[] getFeatureCategories() throws ModulesImporterException {
		if (cachedFeatures != null)
			return cachedFeatures;

		if (modCategory == null)
			throw new ModulesImporterException("Module category not defined.");

		List<EnsemblKeggFeatureCategory> feats = new ArrayList<EnsemblKeggFeatureCategory>();

		if (keggEnabled && organism.getKeggDef() != null)
			feats.addAll(Arrays.asList(KEGG_FEATURES));

		feats.addAll(Arrays.asList(new EnsemblKeggFeatureCategory[] {
			new EnsemblKeggFeatureCategory("Genes", "ensembl:genes", "Ensembl Genes"),
			new EnsemblKeggFeatureCategory("Genes", "ensembl:transcripts", "Ensembl Transcripts")
		}));
		
		try {
			BiomartRestfulService bs = getBiomartService();

			MartLocation mart = getMart();
			DatasetInfo dataset = organism.getEnsemblDataset();

			List<AttributePage> attrs = bs.getAttributes(mart, dataset);

			Map<String, String> idmap = new HashMap<String, String>();
			idmap.put("ensembl_gene_id", "ensembl:genes");
			idmap.put("ensembl_transcript_id", "ensembl:transcripts");
			idmap.put("pdb", "pdb:proteins");
			idmap.put("entrezgene", "entrez:genes");
			idmap.put("uniprot_swissprot_accession", "uniprot:proteins");
			//ids.put("unigene", "unigene:genes");

			Set<String> idremove = new HashSet<String>(Arrays.asList(new String[] {
				"clone_based_ensembl_gene_name", "clone_based_ensembl_transcript_name",
				"clone_based_vega_gene_name", "clone_based_vega_transcript_name",
				"ox_ENS_LRG_transcript__dm_dbprimary_acc_1074", "ottt", "ottg",
				"shares_cds_with_enst", "shares_cds_with_ottt", "shares_cds_and_utr_with_ottt",
				"HGNC_mb001", "uniprot_sptrembl", "wikigene_description",
				"dbass3_id", "dbass3_name", "dbass5_id", "dbass5_name"
			}));

			for (AttributePage p : attrs)
				for (AttributeGroup g : p.getAttributeGroups())
					for (AttributeCollection c : g.getAttributeCollections()) {
						boolean isMicroarray = c.getInternalName().equals("microarray");
						if (c.getInternalName().equals("xrefs") || isMicroarray) {

							logger.debug("Collection: " + c.getDisplayName() + " [" + c.getInternalName() + "]");

							for (AttributeDescription a : c.getAttributeDescriptions()) {
								String dname = a.getDisplayName();
								if (a.isHidden() || dname == null || dname.isEmpty())
									continue;
								
								String iname = a.getInternalName();
								if (idremove.contains(iname))
									continue;

								String id = idmap.get(iname);
								if (id == null)
									id = "ensembl:" + iname;

								EnsemblKeggFeatureCategory feat = COMMON_FEATURES_MAP.get(id);
								if (feat == null) {
									String section = isMicroarray ? "Microarrays" : "Others";
									feat = new EnsemblKeggFeatureCategory(section, id, dname);
								}
								feats.add(feat);

								logger.debug("\t" + dname + " [" + iname + "] --> " + feat.getId());
							}
						}
					}
		}
		catch (Exception ex) {
			throw new ModulesImporterException(ex);
		}

		// remove duplicated features
		Set<String> featIds = new HashSet<String>();
		Iterator<EnsemblKeggFeatureCategory> featIt = feats.iterator();
		while (featIt.hasNext()) {
			EnsemblKeggFeatureCategory feat = featIt.next();
			if (featIds.contains(feat.getId()))
				featIt.remove();
			else
				featIds.add(feat.getId());
		}

		return cachedFeatures = feats.toArray(new EnsemblKeggFeatureCategory[feats.size()]);
	}

	// Importer state

	@Override
	public ModuleCategory getModuleCategory() {
		return modCategory;
	}

	@Override
	public void setModuleCategory(ModuleCategory category) {
		this.modCategory = (EnsemblKeggModuleCategory) category;
		organism = null;
		featCategory = null;
		cachedOrganisms = null;
		cachedFeatures = null;
	}

	@Override
	public Version getVersion() {
		return version;
	}

	@Override
	public void setVersion(Version version) {
		this.version = (EnsemblKeggVersion) version;
		biomartService = null;
		cachedOrganisms = null;
		cachedFeatures = null;
	}

	@Override
	public Organism getOrganism() {
		return organism;
	}

	@Override
	public void setOrganism(Organism organismId) {
		this.organism = (EnsemblKeggOrganism) organismId;
		featCategory = null;
		cachedFeatures = null;
	}

	@Override
	public FeatureCategory getFeatureCategory() {
		return featCategory;
	}

	@Override
	public void setFeatCategory(FeatureCategory featCategory) {
		this.featCategory = (EnsemblKeggFeatureCategory) featCategory;
	}

	// Import

	public ModuleMap importMap(IProgressMonitor monitor) throws ModulesImporterException {
		ModuleMap mmap = null;

		MappingEngine mapping = new MappingEngine();

		if (organism.getKeggDef() != null) {

			String keggorg = organism.getKeggDef().getEntry_id();

			KEGGPortType ks = null;
			try {
				ks = getKeggService();
			}
			catch (Exception ex) {
				throw new ModulesImporterException(ex);
			}

			mapping.addMapper("kegg:pathways", "kegg:genes",
					new KeggPathwaysMapper(ks, keggorg));

			mapping.addMapper("kegg:genes", "ensembl:genes",
					new KeggGenesMapper(ks, keggorg));

			mapping.addMapper("kegg:genes", "pdb:proteins",
					new KeggGenesMapper(ks, keggorg));

			mapping.addMapper("kegg:genes", "uniprot:proteins",
					new KeggGenesMapper(ks, keggorg));

			mapping.addMapper("kegg:genes", "entrez:genes",
					new KeggGenesMapper(ks, keggorg));
		}

		try {
			MappingData data = mapping.run(
					modCategory.getId(),
					featCategory.getId(),
					monitor);

			if (!monitor.isCancelled()) {
				if (!modCategory.getId().equals(data.getSrcNode().getId())
						|| !featCategory.getId().equals(data.getDstNode().getId()))
					monitor.exception(new ModulesImporterException("There was an unexpected mapping error."));

				mmap = new ModuleMap(data.getMap());
			}
		}
		catch (Exception ex) {
			monitor.exception(new ModulesImporterException(ex));
		}

		return mmap;
	}
}
