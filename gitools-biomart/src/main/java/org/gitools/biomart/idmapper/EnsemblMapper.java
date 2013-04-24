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
package org.gitools.biomart.idmapper;

import org.gitools.biomart.BiomartService;
import org.gitools.biomart.queryhandler.BiomartQueryHandler;
import org.gitools.biomart.restful.model.Attribute;
import org.gitools.biomart.restful.model.Dataset;
import org.gitools.biomart.restful.model.Query;
import org.gitools.core.idmapper.*;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class EnsemblMapper extends AbstractMapper implements AllIds {

    private final BiomartService service;
    private final String dataset;

    public EnsemblMapper(BiomartService service, String dataset) {
        super("Ensembl", false, true);

        this.service = service;
        this.dataset = dataset;
    }

    @NotNull
    @Override
    public MappingData map(MappingContext context, @NotNull MappingData data, @NotNull MappingNode src, @NotNull MappingNode dst, @NotNull IProgressMonitor monitor) throws MappingException {
        String srcInternalName = getInternalName(src.getId());
        String dstInternalName = getInternalName(dst.getId());
        if (srcInternalName == null || dstInternalName == null) {
            throw new MappingException("Unsupported mapping from " + src + " to " + dst);
        }

        monitor.begin("Getting mappings from Ensembl ...", 1);

        final Map<String, Set<String>> map = new HashMap<String, Set<String>>();

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
                        items = new HashSet<String>();
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

    private static final Map<String, String> inameMap = new HashMap<String, String>();

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

    public static String getInternalName(@NotNull String id) {
        String iname = inameMap.get(id);
        if (iname == null && id.startsWith("ensembl:")) {
            return id.substring(8);
        }
        return iname;
    }

    @NotNull
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