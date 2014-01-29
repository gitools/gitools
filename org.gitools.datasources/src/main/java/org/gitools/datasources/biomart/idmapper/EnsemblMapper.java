/*
 * #%L
 * gitools-biomart
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
package org.gitools.datasources.biomart.idmapper;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.datasources.biomart.BiomartService;
import org.gitools.datasources.biomart.queryhandler.BiomartQueryHandler;
import org.gitools.datasources.biomart.restful.model.Attribute;
import org.gitools.datasources.biomart.restful.model.Dataset;
import org.gitools.datasources.biomart.restful.model.Query;
import org.gitools.datasources.idmapper.AbstractMapper;
import org.gitools.datasources.idmapper.MappingContext;
import org.gitools.datasources.idmapper.MappingData;
import org.gitools.datasources.idmapper.MappingException;
import org.gitools.datasources.idmapper.MappingNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class EnsemblMapper extends AbstractMapper implements AllIds {

    private final BiomartService service;
    private final String dataset;

    public EnsemblMapper(BiomartService service, String dataset) {
        super("Ensembl", false, true);

        this.service = service;
        this.dataset = dataset;
    }


    @Override
    public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
        String srcInternalName = getInternalName(src.getId());
        String dstInternalName = getInternalName(dst.getId());
        if (srcInternalName == null || dstInternalName == null) {
            throw new MappingException("Unsupported mapping from " + src + " to " + dst);
        }

        monitor.begin("Getting mappings from Ensembl ...", 1);

        final Map<String, Set<String>> map = new HashMap<>();

        Query q = createQuery(dataset, srcInternalName, dstInternalName);
        try {
            service.queryModule(q, new BiomartQueryHandler() {
                @Override
                public void begin() throws Exception {
                }

                @Override
                public void end() {
                }

                @Override
                public void line(String[] rowFields) throws Exception {
                    String srcf = rowFields[0];
                    String dstf = rowFields[1];
                    Set<String> items = map.get(srcf);
                    if (items == null) {
                        items = new HashSet<>();
                        map.put(srcf, items);
                    }
                    items.add(dstf);
                }
            }, monitor);
        } catch (Exception ex) {
            throw new MappingException(ex);
        }

        monitor.end();

        monitor.begin("Mapping Ensembl IDs...", 1);

        if (data.isEmpty()) {
            data.identity(map.keySet());
        }

        data.map(map);

        monitor.end();

        return data;
    }

    private static final Map<String, String> inameMap = new HashMap<>();

    static {
        inameMap.put(ENSEMBL_GENES, "ensembl_gene_id");
        inameMap.put(ENSEMBL_TRANSCRIPTS, "ensembl_transcript_id");
        inameMap.put(ENSEMBL_PROTEINS, "ensembl_peptide_id");

        inameMap.put(PDB, "pdb");
        inameMap.put(NCBI_REFSEQ, "embl");
        inameMap.put(NCBI_GENES, "entrezgene");
        inameMap.put(NCBI_UNIGENE, "unigene");
        inameMap.put(UNIPROT, "uniprot_swissprot_accession");

        inameMap.put(GO_BP, "go_biological_process_id");
        inameMap.put(GO_MF, "go_molecular_function_id");
        inameMap.put(GO_CL, "go_cellular_component_id");
        inameMap.put(GO_ID, "go_id");
    }

    public static String getInternalName(String id) {
        String iname = inameMap.get(id);
        if (iname == null && id.startsWith("ensembl:")) {
            return id.substring(8);
        }
        return iname;
    }


    public static Query createQuery(String dataset, String srcInternalName, String dstInternalName) {
        Query q = new Query();
        q.setVirtualSchemaName("default");
        q.setUniqueRows(1);
        Dataset ds = new Dataset();
        ds.setName(dataset);
        List<Attribute> attrs = ds.getAttribute();
        Attribute srcAttr = new Attribute();
        srcAttr.setName(srcInternalName);
        attrs.add(srcAttr);
        Attribute dstAttr = new Attribute();
        dstAttr.setName(dstInternalName);
        attrs.add(dstAttr);

        q.getDatasets().add(ds);

        return q;
    }
}