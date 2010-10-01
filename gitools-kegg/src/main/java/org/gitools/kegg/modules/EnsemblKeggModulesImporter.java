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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.gitools.kegg.soap.Definition;
import org.gitools.kegg.soap.KEGGLocator;
import org.gitools.kegg.soap.KEGGPortType;
import org.gitools.model.ModuleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnsemblKeggModulesImporter implements ModulesImporter {

	private static Logger logger = LoggerFactory.getLogger(EnsemblKeggModulesImporter.class);

	// KEGG

	public static final String KEGG_DB = "kegg";
	public static final String KEGG_PATHWAYS_ID = "kegg_pathways";
	public static final String KEGG_GENE_ID = "kegg_gene";

	public static ModuleCategory[] KEGG_MODULE_CATEGORIES = new EnsemblKeggModuleCategory[] {
		new EnsemblKeggModuleCategory(KEGG_DB, KEGG_PATHWAYS_ID, "KEGG Pathways")
	};

	public static EnsemblKeggFeatureCategory[] KEGG_FEATURE_CATEGORIES = new EnsemblKeggFeatureCategory[] {
		new EnsemblKeggFeatureCategory(KEGG_DB, KEGG_GENE_ID, "KEGG Genes", "KEGG")
	};

	// Gene Ontology

	public static final String ENSEMBL_DB = "ensembl";

	public static final String GO_BP_ID = "go_bp";
	public static final String GO_MF_ID = "go_mf";
	public static final String GO_CL_ID = "go_cl";

	public static ModuleCategory[] GO_MODULE_CATEGORIES = new EnsemblKeggModuleCategory[] {
		new EnsemblKeggModuleCategory(ENSEMBL_DB, GO_BP_ID, "GO Biological Processes"),
		new EnsemblKeggModuleCategory(ENSEMBL_DB, GO_MF_ID, "GO Molecular functions"),
		new EnsemblKeggModuleCategory(ENSEMBL_DB, GO_BP_ID, "GO Cellular locations")
	};

	// selection state

	private EnsemblKeggModuleCategory modCategory;

	private EnsemblKeggVersion version;

	private EnsemblKeggOrganism organism;

	private EnsemblKeggFeatureCategory featCategory;

	// internal state

	ModuleCategory[] moduleCategories;

	private BiomartRestfulService biomartService;

	private KEGGPortType keggService;

	private Organism[] cachedOrganisms;

	private FeatureCategory[] cachedFeatures;

	public EnsemblKeggModulesImporter(ModuleCategory[] moduleCategories) {
		this.moduleCategories = moduleCategories;
	}

	private BiomartRestfulService getBiomartService() throws BiomartServiceException {
		if (biomartService != null)
			return biomartService;

		if (version == null)
			throw new RuntimeException("Ensembl biomart source not defined.");
		
		return biomartService =
				BiomartServiceFactory.createRestfulService(version.getSource());
	}

	private KEGGPortType getKeggService() throws ServiceException {
		if (keggService != null)
			return keggService;

		KEGGLocator locator = new KEGGLocator();
		return keggService = locator.getKEGGPort();
	}

	@Override
	public ModuleCategory[] getModuleCategories() {
		return moduleCategories;
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

		try {
			BiomartRestfulService bs = getBiomartService();

			List<MartLocation> registry = bs.getRegistry();
			Iterator<MartLocation> it = registry.iterator();

			MartLocation mart = null;
			while (mart == null && it.hasNext()) {
				MartLocation m = it.next();
				if (m.getName().equals("ENSEMBL_MART_ENSEMBL"))
					mart = m;
			}

			if (mart == null)
				throw new RuntimeException("Ensembl Genes database not found in the registry.");

			List<DatasetInfo> datasets = bs.getDatasets(mart);
			for (DatasetInfo ds : datasets) {
				String name = ds.getDisplayName().replaceAll(" genes [(].*[)]", "");
				orgs.add(new EnsemblKeggOrganism(ds.getName(), name, mart, ds));
			}

			// KEGG organism match
			if (modCategory.getDb().equals(KEGG_DB)) {
				KEGGPortType serv = getKeggService();

				Map<String, Definition> orgMap = new HashMap<String, Definition>();
				Definition[] orgDefs = serv.list_organisms();
				for (Definition def : orgDefs)
					orgMap.put(def.getDefinition()
						.replaceAll(" [(].*[)]", "").toLowerCase(), def);

				Iterator<EnsemblKeggOrganism> itOrg = orgs.iterator();
				while (itOrg.hasNext()) {
					EnsemblKeggOrganism org = itOrg.next();
					Definition def = orgMap.get(org.getName().toLowerCase());
					if (def != null)
						org.setKeggId(def.getEntry_id());
					else {
						itOrg.remove();
						logger.warn("Ensembl organism '" + org.getName() + "' not found in KEGG");
					}
				}
			}
		}
		catch (Exception ex) {
			throw new ModulesImporterException(ex);
		}

		return cachedOrganisms = orgs.toArray(new EnsemblKeggOrganism[orgs.size()]);
	}

	@Override
	public FeatureCategory[] getFeatureCategories() throws ModulesImporterException {
		if (cachedFeatures != null)
			return cachedFeatures;

		if (modCategory == null)
			throw new RuntimeException("Module category not defined.");

		List<EnsemblKeggFeatureCategory> feats = new ArrayList<EnsemblKeggFeatureCategory>();

		if (modCategory.getDb().equals(KEGG_DB))
			feats.addAll(Arrays.asList(KEGG_FEATURE_CATEGORIES));

		try {
			BiomartRestfulService bs = getBiomartService();

			MartLocation mart = organism.getMart();
			DatasetInfo dataset = organism.getDataset();

			List<AttributePage> attrs = bs.getAttributes(mart, dataset);

			Map<String, String> ids = new HashMap<String, String>();
			ids.put("ensembl_gene_id", "Ensembl");
			ids.put("ensembl_transcript_id", "Ensembl");
			ids.put("ensembl_peptide_id", "Ensembl");
			ids.put("ucsc", "Genes");
			ids.put("pdb", "Proteins");
			ids.put("embl", "Genes");
			ids.put("entrezgene", "Genes");
			ids.put("hgnc_symbol", "Genes");
			ids.put("refseq_dna", "Genes");
			ids.put("uniprot_swissprot_accession", "Proteins");
			ids.put("protein_id", "Proteins");
			ids.put("refseq_peptide", "Proteins");
			ids.put("unigene", "Genes");

			for (AttributePage p : attrs)
				for (AttributeGroup g : p.getAttributeGroups())
					for (AttributeCollection c : g.getAttributeCollections())
						for (AttributeDescription a : c.getAttributeDescriptions()) {
							String section = ids.get(a.getInternalName());
							if (section != null)
								feats.add(new EnsemblKeggFeatureCategory(
										ENSEMBL_DB, a.getInternalName(), a.getDisplayName(), section));
						}

		}
		catch (Exception ex) {
			throw new ModulesImporterException(ex);
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
		ModuleMap mmap = new ModuleMap();

		try {
			if (featCategory.getDb().equals(ENSEMBL_DB)) {
				Map<String, String> map1 = new HashMap<String, String>();
				
				Query query = new Query();
				Dataset ds = new Dataset();
				ds.setInterface(organism.getDataset().getInterface());
				ds.setName(organism.getDataset().getName());
				query.getDatasets().add(ds);

				Attribute attr = new Attribute();
				attr.setName(featCategory.getId());
				ds.getAttribute().add(attr);
				if (!featCategory.getId().equals("entrezgene")) {
					attr = new Attribute();
					attr.setName("entrezgene");
					ds.getAttribute().add(attr);
				}

				biomartService.queryModule(query, new SequentialTableWriter() {
					@Override public void open() throws Exception { }

					@Override public void close() { }

					@Override public void write(String[] rowFields) throws Exception {
						
					}
				}, monitor);
			}
		}
		catch (Exception ex) {
			throw new ModulesImporterException(ex);
		}

		return mmap;
	}
}
