/*
 * #%L
 * gitools-kegg
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.kegg.modules;

import org.gitools.biomart.BiomartService;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.idmapper.EnsemblMapper;
import org.gitools.biomart.queryhandler.BiomartQueryHandler;
import org.gitools.biomart.restful.model.*;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.core.idmapper.MappingData;
import org.gitools.core.idmapper.MappingEngine;
import org.gitools.kegg.idmapper.AllIds;
import org.gitools.kegg.idmapper.KeggGenesMapper;
import org.gitools.kegg.idmapper.KeggPathwaysMapper;
import org.gitools.kegg.service.KeggService;
import org.gitools.kegg.service.domain.KeggOrganism;
import org.gitools.kegg.service.domain.KeggPathway;
import org.gitools.core.model.ModuleMap;
import org.gitools.core.modules.importer.*;
import org.gitools.obo.OBOEvent;
import org.gitools.obo.OBOEventTypes;
import org.gitools.obo.OBOStreamReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EnsemblKeggModulesImporter implements ModulesImporter, AllIds, OBOEventTypes {

    private static final Logger logger = LoggerFactory.getLogger(EnsemblKeggModulesImporter.class);

    // KEGG

    private static final EnsemblKeggModuleCategory[] KEGG_MODULE_CATEGORIES = new EnsemblKeggModuleCategory[]{new EnsemblKeggModuleCategory("KEGG", KEGG_PATHWAYS, "KEGG Pathways")};

    // Gene Ontology

    private static final EnsemblKeggModuleCategory[] GO_MODULE_CATEGORIES = new EnsemblKeggModuleCategory[]{new EnsemblKeggModuleCategory("Ensembl", GO_BP, "GO Biological Processes"), new EnsemblKeggModuleCategory("Ensembl", GO_MF, "GO Molecular functions"), new EnsemblKeggModuleCategory("Ensembl", GO_CL, "GO Cellular locations")};

    private static final Map<String, String> goDescId = new HashMap<String, String>();

    static {
        goDescId.put(GO_BP, "name_1006");
        goDescId.put(GO_MF, "go_molecular_function__dm_name_1006");
        goDescId.put(GO_CL, "go_cellular_component__dm_name_1006");
        goDescId.put(GO_ID, "name_1006");
    }

    // Features

    private static final EnsemblKeggFeatureCategory[] COMMON_FEATURES = new EnsemblKeggFeatureCategory[]{new EnsemblKeggFeatureCategory("Genes", KEGG_GENES, "KEGG Genes"), new EnsemblKeggFeatureCategory("Genes", NCBI_GENES, "NCBI Genes"), new EnsemblKeggFeatureCategory("Protein", PDB, "PDB"), new EnsemblKeggFeatureCategory("Protein", UNIPROT, "UniProt"), new EnsemblKeggFeatureCategory("Genes", ENSEMBL_GENES, "Ensembl Genes"), new EnsemblKeggFeatureCategory("Genes", ENSEMBL_TRANSCRIPTS, "Ensembl Transcripts"), new EnsemblKeggFeatureCategory("Protein", ENSEMBL_PROTEINS, "Ensembl Proteins")};

    private static final Map<String, EnsemblKeggFeatureCategory> featMap = new HashMap<String, EnsemblKeggFeatureCategory>();

    static {
        for (EnsemblKeggFeatureCategory f : COMMON_FEATURES)
            featMap.put(f.getId(), f);
    }

    // selection state

    private EnsemblKeggVersion version;

    @Nullable
    private EnsemblKeggOrganism organism;

    private EnsemblKeggModuleCategory modCategory;

    @Nullable
    private EnsemblKeggFeatureCategory featCategory;

    private final boolean keggEnabled;

    private final boolean goEnabled;

    // internal state

    @Nullable
    private BiomartService biomartService;

    @Nullable
    private MartLocation martLocation;

    private KeggService keggService;

    @Nullable
    private Organism[] cachedOrganisms;

    private final ModuleCategory[] moduleCategories;

    @Nullable
    private FeatureCategory[] cachedFeatures;

    private Map<String, EnsemblKeggFeatureCategory> keggFeatMap;
    private List<EnsemblKeggFeatureCategory> ensemblFeats;

    public EnsemblKeggModulesImporter(boolean keggEnabled, boolean goEnabled) {
        this.keggEnabled = keggEnabled;
        this.goEnabled = goEnabled;

        List<ModuleCategory> mc = new ArrayList<ModuleCategory>();

        if (keggEnabled) {
            mc.addAll(Arrays.asList(KEGG_MODULE_CATEGORIES));
        }

        if (goEnabled) {
            mc.addAll(Arrays.asList(GO_MODULE_CATEGORIES));
        }

        moduleCategories = mc.toArray(new ModuleCategory[mc.size()]);
    }

    @Nullable
    private BiomartService getBiomartService() throws BiomartServiceException, ModulesImporterException {
        if (biomartService != null) {
            return biomartService;
        }

        if (version == null) {
            throw new ModulesImporterException("Ensembl biomart source not defined.");
        }

        return biomartService = BiomartServiceFactory.createService(version.getSource());
    }

    @Nullable
    private MartLocation getMart() throws BiomartServiceException, ModulesImporterException {
        BiomartService bs = getBiomartService();
        List<MartLocation> registry = bs.getRegistry();
        Iterator<MartLocation> it = registry.iterator();

        martLocation = null;
        while (martLocation == null && it.hasNext()) {
            MartLocation m = it.next();
            if (m.getName().equals("ENSEMBL_MART_ENSEMBL")) {
                martLocation = m;
            }
        }

        return martLocation;
    }

    @NotNull
    @Override
    public Version[] getVersions() {
        LinkedList<Version> sources = new LinkedList<Version>();

        Version latestVersion = null;
        for (BiomartSource src : BiomartSourceManager.getDefault().getSources()) {
            if (src.getName().matches(".*ensembl.*")) {
                if (src.getName().matches(".*last.*")) {
                    latestVersion = new EnsemblKeggVersion(src);
                } else {
                    sources.add(new EnsemblKeggVersion(src));
                }
            }
        }

        if (latestVersion != null) {
            sources.add(0, latestVersion);
        }

        return sources.toArray(new Version[sources.size()]);
    }

    @Nullable
    @Override
    public Organism[] getOrganisms() throws ModulesImporterException {
        if (cachedOrganisms != null) {
            return cachedOrganisms;
        }

        List<EnsemblKeggOrganism> orgs = new ArrayList<EnsemblKeggOrganism>();
        Map<String, EnsemblKeggOrganism> orgsMap = new HashMap<String, EnsemblKeggOrganism>();

        try {
            // Ensembl organisms

            BiomartService bs = getBiomartService();
            MartLocation mart = getMart();

            List<DatasetInfo> datasets = bs.getDatasets(mart);
            for (DatasetInfo ds : datasets) {
                String id = ds.getDisplayName().replaceAll(" genes [(].*[)]", "").toLowerCase();
                EnsemblKeggOrganism o = orgsMap.get(id);
                if (o == null) {
                    o = new EnsemblKeggOrganism(id, id, ds);
                    orgs.add(o);
                    orgsMap.put(id, o);
                } else {
                    o.setEnsemblDataset(ds);
                }
            }

            // KEGG organisms

            if (keggEnabled) {
                KeggService serv = getKeggService();

                List<KeggOrganism> orgDefs = serv.getOrganisms();

                for (KeggOrganism def : orgDefs) {
                    String id = def.getDescription().replaceAll(" [(].*[)]", "").toLowerCase();
                    EnsemblKeggOrganism o = orgsMap.get(id);
                    if (o == null) {
                        o = new EnsemblKeggOrganism(id, id, def);
                        orgs.add(o);
                        orgsMap.put(id, o);
                    } else {
                        o.setKeggDef(def);
                    }
                }

                // Remove organism of Ensembl not in KEGG
                Iterator<EnsemblKeggOrganism> it = orgs.iterator();
                while (it.hasNext()) {
                    EnsemblKeggOrganism o = it.next();
                    if (o.getKeggOrganism() == null) {
                        it.remove();
                        orgsMap.remove(o.getId());
                    }
                }
            }
        } catch (Exception ex) {
            throw new ModulesImporterException(ex);
        }

        return cachedOrganisms = orgs.toArray(new EnsemblKeggOrganism[orgs.size()]);
    }

    @Override
    public ModuleCategory[] getModuleCategories() {
        return moduleCategories;
    }

    @Nullable
    @Override
    public FeatureCategory[] getFeatureCategories() throws ModulesImporterException {
        if (cachedFeatures != null) {
            return cachedFeatures;
        }

        if (modCategory == null) {
            throw new ModulesImporterException("Module category not defined.");
        }

        List<EnsemblKeggFeatureCategory> feats = new ArrayList<EnsemblKeggFeatureCategory>();

		/* if this importer is activated for KEGG and the selected organism exists in KEGG*/
        if (keggEnabled && organism.getKeggOrganism() != null) {

            keggFeatMap = new HashMap<String, EnsemblKeggFeatureCategory>();
            keggFeatMap.put(KEGG_GENES, featMap.get(KEGG_GENES));
            feats.add(featMap.get(KEGG_GENES));

            String orgId = organism.getKeggOrganism().getId();
            Map<String, String> idMap = new HashMap<String, String>();
            idMap.put(KeggGenesMapper.ENSEMBL_DB + "-" + orgId, ENSEMBL_GENES);
            idMap.put(KeggGenesMapper.NCBI_DB, NCBI_GENES);
            idMap.put(KeggGenesMapper.UNIPROT_DB, UNIPROT);
            idMap.put(KeggGenesMapper.PDB_DB, PDB);

            // PDB and ENSMBL are removed from REST API
            // String[] idList = new String[] { ENSEMBL_GENES, NCBI_GENES, UNIPROT, PDB};
            String[] idList = new String[]{NCBI_GENES, UNIPROT};

            for (String id : idList) {
                EnsemblKeggFeatureCategory f = featMap.get(id);
                feats.add(f);
                keggFeatMap.put(id, f);
            }
        }

		/* if the selected organism has a dataset in ensembl */
        if (organism.getEnsemblDataset() != null) {
            ensemblFeats = new ArrayList<EnsemblKeggFeatureCategory>();

            ensemblFeats.addAll(Arrays.asList(new EnsemblKeggFeatureCategory[]{featMap.get(ENSEMBL_GENES), featMap.get(ENSEMBL_TRANSCRIPTS), featMap.get(ENSEMBL_PROTEINS)}));

            List<AttributePage> attrs = null;
            try {
                BiomartService bs = getBiomartService();

                MartLocation mart = getMart();
                DatasetInfo dataset = organism.getEnsemblDataset();

                attrs = bs.getAttributes(mart, dataset);
            } catch (BiomartServiceException ex) {
                throw new ModulesImporterException(ex);
            }

            Map<String, String> idmap = new HashMap<String, String>();
            idmap.put("ensembl_gene_id", ENSEMBL_GENES);
            idmap.put("ensembl_transcript_id", ENSEMBL_TRANSCRIPTS);
            idmap.put("pdb", PDB);
            idmap.put("entrezgene", NCBI_GENES);
            idmap.put("uniprot_swissprot_accession", UNIPROT);
            //idmap.put("unigene", UNIGENE);

            Set<String> idremove = new HashSet<String>(Arrays.asList(new String[]{"clone_based_ensembl_gene_name", "clone_based_ensembl_transcript_name", "clone_based_vega_gene_name", "clone_based_vega_transcript_name", "ox_ENS_LRG_transcript__dm_dbprimary_acc_1074", "ottt", "ottg", "shares_cds_with_enst", "shares_cds_with_ottt", "shares_cds_and_utr_with_ottt", "HGNC_mb001", "uniprot_sptrembl", "wikigene_description", "dbass3_id", "dbass3_name", "dbass5_id", "dbass5_name"}));

            for (AttributePage p : attrs)
                for (AttributeGroup g : p.getAttributeGroups())
                    for (AttributeCollection c : g.getAttributeCollections()) {
                        boolean isXref = c.getInternalName().equals("xrefs");
                        boolean isMicroarray = c.getInternalName().equals("microarray");
                        if (isXref || isMicroarray) {

                            logger.debug("Collection: " + c.getDisplayName() + " [" + c.getInternalName() + "]");

                            for (AttributeDescription a : c.getAttributeDescriptions()) {
                                String dname = a.getDisplayName();
                                if (a.isHidden() || dname == null || dname.isEmpty()) {
                                    continue;
                                }

                                String iname = a.getInternalName();
                                if (idremove.contains(iname)) {
                                    continue;
                                }

                                String id = idmap.get(iname);
                                if (id == null) {
                                    id = "ensembl:" + iname;
                                }

                                EnsemblKeggFeatureCategory feat = featMap.get(id);
                                if (feat == null) {
                                    String section = isMicroarray ? "Microarrays" : "Others";
                                    feat = new EnsemblKeggFeatureCategory(section, id, dname);
                                }
                                ensemblFeats.add(feat);

                                logger.debug("\t" + dname + " [" + iname + "] --> " + feat.getId());
                            }
                        }
                    }

            feats.addAll(ensemblFeats);
        }

        // remove duplicated features
        Set<String> featIds = new HashSet<String>();
        Iterator<EnsemblKeggFeatureCategory> featIt = feats.iterator();
        while (featIt.hasNext()) {
            EnsemblKeggFeatureCategory feat = featIt.next();
            if (featIds.contains(feat.getId())) {
                featIt.remove();
            } else {
                featIds.add(feat.getId());
            }
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

    @Nullable
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

    @Nullable
    @Override
    public FeatureCategory getFeatureCategory() {
        return featCategory;
    }

    @Override
    public void setFeatCategory(FeatureCategory featCategory) {
        this.featCategory = (EnsemblKeggFeatureCategory) featCategory;
    }

    // Import

    @Nullable
    public ModuleMap importMap(@NotNull IProgressMonitor monitor) throws ModulesImporterException {
        ModuleMap mmap = null;

        MappingEngine mapping = new MappingEngine();

        // Build KEGG network
        if (organism.getKeggOrganism() != null) {

            monitor.info("KEGG");

            String keggorg = organism.getKeggOrganism().getId();

            KeggService ks = null;
            try {
                ks = getKeggService();
            } catch (Exception ex) {
                throw new ModulesImporterException(ex);
            }

            mapping.addMapper(KEGG_PATHWAYS, KEGG_GENES, new KeggPathwaysMapper(ks, keggorg));

            for (Map.Entry<String, EnsemblKeggFeatureCategory> e : keggFeatMap.entrySet())
                mapping.addMapper(KEGG_GENES, e.getValue().getId(), new KeggGenesMapper(ks, keggorg));
        }

        // Build Ensembl network
        Integer numericRelease = null;
        String release = version.getSource().getRelease();
        if (release != null && !release.isEmpty()) {
            numericRelease = Integer.parseInt(release);
        }

        if (organism.getEnsemblDataset() != null) {

            monitor.info("Ensembl");

            BiomartService bs = null;
            try {
                bs = getBiomartService();
            } catch (BiomartServiceException ex) {
                throw new ModulesImporterException(ex);
            }

            String dsName = organism.getEnsemblDataset().getName();

            // Get KEGG and Ensembl networks link

            String linkId = null;

            if (keggEnabled && organism.getKeggOrganism() != null && !keggFeatMap.containsKey(ENSEMBL_GENES)) {

                List<String> idList = Arrays.asList(new String[]{ENSEMBL_GENES, NCBI_GENES, PDB, UNIPROT});
                Iterator<String> it = idList.iterator();
                while (linkId == null && it.hasNext()) {
                    String id = it.next();
                    if (keggFeatMap.containsKey(id)) {
                        linkId = id;
                    }
                }
                if (linkId != null) {
                    mapping.addMapper(linkId, ENSEMBL_GENES, new EnsemblMapper(bs, dsName));
                }
            } else {
                linkId = ENSEMBL_GENES;
            }

            Set<String> sinks = new HashSet<String>();

            for (FeatureCategory f : ensemblFeats) {
                String id = f.getRef();
                if (!id.equals(linkId)) {
                    sinks.add(id);
                }
            }

            for (String id : sinks)
                mapping.addMapper(linkId, id, new EnsemblMapper(bs, dsName));

            if (goEnabled) {
                if (numericRelease != null && numericRelease >= 62) {
                    for (String id : sinks)
                        mapping.addMapper(GO_ID, id, new EnsemblMapper(bs, dsName));

                    mapping.addMapper(GO_ID, linkId, new EnsemblMapper(bs, dsName));

                } else {
                    for (String id : sinks) {
                        mapping.addMapper(GO_BP, id, new EnsemblMapper(bs, dsName));
                        mapping.addMapper(GO_MF, id, new EnsemblMapper(bs, dsName));
                        mapping.addMapper(GO_CL, id, new EnsemblMapper(bs, dsName));
                    }

                    mapping.addMapper(GO_BP, linkId, new EnsemblMapper(bs, dsName));
                    mapping.addMapper(GO_MF, linkId, new EnsemblMapper(bs, dsName));
                    mapping.addMapper(GO_CL, linkId, new EnsemblMapper(bs, dsName));
                }
            }
        }

        try {
            String src = modCategory.getId();

            Map<String, String> modDesc = null;
            String[] ids = null;
            MappingData data = null;

            if (isCategory(src, KEGG_MODULE_CATEGORIES)) {
                modDesc = retrieveKeggsModules(src, monitor.subtask());
            } else if (isCategory(src, GO_MODULE_CATEGORIES)) {
                if (numericRelease != null && numericRelease >= 62) {
                    modDesc = retrieveGoModulesEns62(src, monitor.subtask());
                    src = GO_ID;
                } else {
                    modDesc = retrieveGoModules(src, monitor.subtask());
                }
            } else {
                throw new ModulesImporterException("Unknown category id: " + src);
            }

            ids = modDesc.keySet().toArray(new String[modDesc.size()]);
            data = mapping.run(ids, src, featCategory.getId(), monitor);

            if (!monitor.isCancelled()) {
                if (!src.equals(data.getSrcNode().getId()) || !featCategory.getId().equals(data.getDstNode().getId())) {
                    monitor.exception(new ModulesImporterException("There was an unexpected mapping error."));
                }

                Map<String, Set<String>> tree = null;

                boolean plainGo = goEnabled && (modCategory.getId().equals(GO_BP) || modCategory.getId().equals(GO_MF) || modCategory.getId().equals(GO_CL));

                if (plainGo) {
                    tree = retrieveGoTree(monitor);
                    data = plainGoData(data, tree, monitor);
                } else {
                    tree = new HashMap<String, Set<String>>();
                }

                mmap = new ModuleMap(data.getMap(), modDesc, tree);

                //if (plainGo)
                //mmap = mmap.plain();
            }
        } catch (Exception ex) {
            throw new ModulesImporterException(ex);
        }

        return mmap;
    }


    @NotNull
    private Map<String, String> retrieveKeggsModules(final String src, @NotNull IProgressMonitor monitor) throws ModulesImporterException {
        final Map<String, String> desc = new HashMap<String, String>();
        monitor.begin("Getting KEGG pathways ...", 1);
        try {
            String keggorg = organism.getKeggOrganism().getId();

            KeggService ks = getKeggService();

            List<KeggPathway> pathwaysDefs = ks.getPathways(keggorg);
            for (KeggPathway d : pathwaysDefs)
                desc.put(d.getId(), d.getDescription());
        } catch (Exception ex) {
            throw new ModulesImporterException(ex);
        }
        monitor.end();
        return desc;
    }

    @NotNull
    private Map<String, String> retrieveGoModulesEns62(@NotNull final String src, @NotNull IProgressMonitor monitor) throws ModulesImporterException {
        final Map<String, String> desc = new HashMap<String, String>();

        monitor.begin("Getting Gene Ontology names ...", 1);
        BiomartService bs = null;
        try {
            bs = getBiomartService();

            Query q = EnsemblMapper.createQuery(organism.getEnsemblDataset().getName(), EnsemblMapper.getInternalName(GO_ID), goDescId.get(GO_ID));

            Attribute a = new Attribute();
            a.setName("namespace_1003");
            q.getDatasets().get(0).getAttribute().add(a);

            bs.queryTable(q, new BiomartQueryHandler() {
                @Override
                public void begin() throws Exception {
                }

                @Override
                public void line(@NotNull String[] fields) throws Exception {
                    if (fields.length == 3) {
                        if ((src.equals(GO_CL) && fields[2].equals("cellular_component")) || (src.equals(GO_MF) && fields[2].equals("molecular_function")) || (src.equals(GO_BP) && fields[2].equals("biological_process"))) {
                            desc.put(fields[0], fields[1]);
                        }
                    }
                }

                @Override
                public void end() {
                }
            }, true, "", monitor.subtask());
        } catch (BiomartServiceException ex) {
            throw new ModulesImporterException(ex);
        }
        monitor.end();

        return desc;
    }

    @NotNull
    private Map<String, String> retrieveGoModules(@NotNull final String src, @NotNull IProgressMonitor monitor) throws ModulesImporterException {
        final Map<String, String> desc = new HashMap<String, String>();

        monitor.begin("Getting Gene Ontology names ...", 1);
        BiomartService bs = null;
        try {
            bs = getBiomartService();

            Query q = EnsemblMapper.createQuery(organism.getEnsemblDataset().getName(), EnsemblMapper.getInternalName(src), goDescId.get(src));

            bs.queryTable(q, new BiomartQueryHandler() {
                @Override
                public void begin() throws Exception {
                }

                @Override
                public void line(@NotNull String[] fields) throws Exception {
                    if (fields.length == 2) {
                        desc.put(fields[0], fields[1]);
                    }
                }

                @Override
                public void end() {
                }
            }, true, "", monitor.subtask());

        } catch (BiomartServiceException ex) {
            throw new ModulesImporterException(ex);
        }
        monitor.end();

        return desc;
    }

    private boolean isCategory(String src, @NotNull EnsemblKeggModuleCategory[] categories) {
        for (EnsemblKeggModuleCategory c : categories)
            if (c.getId().equals(src)) {
                return true;
            }

        return false;
    }

    @NotNull
    private Map<String, Set<String>> retrieveGoTree(@NotNull IProgressMonitor monitor) throws IOException {
        monitor.begin("Reading Gene Ontology graph ...", 1);

        URL url = new URL("ftp://ftp.geneontology.org/pub/go/ontology/gene_ontology.obo");
        OBOStreamReader oboReader = new OBOStreamReader(url);

        Map<String, Set<String>> tree = new HashMap<String, Set<String>>();

        OBOEvent evt = oboReader.nextEvent();
        while (evt != null) {
            while (evt != null && evt.getType() != STANZA_START)
                evt = oboReader.nextEvent();

            if (evt != null && evt.getStanzaName().equalsIgnoreCase("term")) {
                evt = oboReader.nextEvent();

                String id = null;
                Set<String> isA = new HashSet<String>();
                boolean obsolete = false;

                while (evt != null && evt.getType() != STANZA_START) {

                    if (evt != null && evt.getType() == TAG_START) {
                        String tagName = evt.getTagName();
                        if (tagName.equalsIgnoreCase("id")) {
                            id = evt.getTagContents();
                        } else if (tagName.equalsIgnoreCase("is_a")) {
                            isA.add(evt.getTagContents());
                        } else if (tagName.equalsIgnoreCase("is_obsolete")) {
                            obsolete = evt.getTagContents().equalsIgnoreCase("true");
                        }

                        evt = oboReader.nextEvent();
                    }

                    while (evt != null &&
                            evt.getType() != STANZA_START &&
                            evt.getType() != TAG_START)
                        evt = oboReader.nextEvent();
                }

                if (id != null && !obsolete) {
                    for (String parent : isA) {
                        Set<String> set = tree.get(parent);
                        if (set == null) {
                            set = new HashSet<String>();
                            tree.put(parent, set);
                        }
                        set.add(id);
                    }
                }
            } else if (evt != null) {
                evt = oboReader.nextEvent();
            }
        }

        monitor.end();

		/*System.out.println();
        for (String key : tree.keySet()) {
		System.out.print(key + " --> ");
		for (String v : tree.get(key))
		System.out.print(v + ", ");
		System.out.println();
		}*/

        return tree;
    }

    @NotNull
    private MappingData plainGoData(@NotNull MappingData data, @NotNull Map<String, Set<String>> tree, @NotNull IProgressMonitor monitor) throws IOException {
        monitor.begin("Plaining Gene Ontology hierarchy ...", 1);

		/*Map<String, Set<String>> m = data.getMap();
        for (String key : m.keySet()) {
			System.out.print(key + " --> ");
			for (String v : m.get(key))
				System.out.print(v + ", ");
			System.out.println();
		}*/

        MappingData plainData = new MappingData(data.getSrcNode().getId(), data.getDstNode().getId());

        for (String id : data.getSrcIds()) {
            Set<String> dstIds = new HashSet<String>(data.get(id));

            plainGoTerm(id, new HashSet<String>(), dstIds, data.getMap(), tree);

            plainData.set(id, dstIds);
        }

        monitor.end();

        return plainData;
    }

    private void plainGoTerm(String id, @NotNull Set<String> path, @NotNull Set<String> dstIds, @NotNull Map<String, Set<String>> map, @NotNull Map<String, Set<String>> childrenTree) {

        if (path.contains(id)) {
            throw new RuntimeException("Circular reference in path " + path + " for term " + id);
        }

        Set<String> childrenList = childrenTree.get(id);
        if (childrenList == null) {
            return;
        }

        for (String child : childrenList) {
            Set<String> ids = map.get(child);
            if (ids == null) {
                continue;
            }

            dstIds.addAll(ids);
            Set<String> childPath = new HashSet<String>(path);
            childPath.add(id);
            plainGoTerm(child, childPath, dstIds, map, childrenTree);
        }
    }

    KeggService getKeggService() {

        if (keggService == null) {
            keggService = new KeggService();
        }

        return keggService;
    }
}
