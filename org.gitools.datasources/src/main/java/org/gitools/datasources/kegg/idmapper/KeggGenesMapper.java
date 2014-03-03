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
package org.gitools.datasources.kegg.idmapper;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.datasources.idmapper.MappingContext;
import org.gitools.datasources.idmapper.MappingData;
import org.gitools.datasources.idmapper.MappingException;
import org.gitools.datasources.idmapper.MappingNode;
import org.gitools.datasources.kegg.service.KeggService;
import org.gitools.datasources.kegg.service.domain.IdConversion;

import java.util.*;

public class KeggGenesMapper extends AbstractKeggMapper implements AllIds {

    public static final String NCBI_DB = "ncbi-geneid";
    public static final String UNIPROT_DB = "uniprot";
    public static final String PDB_DB = "pdb";
    public static final String ENSEMBL_DB = "ensembl";

    private static final Map<String, String> fileKey = new HashMap<>();

    static {
        fileKey.put(NCBI_GENES, NCBI_DB);
        fileKey.put(UNIPROT, UNIPROT_DB);
        fileKey.put(PDB, PDB_DB);
        fileKey.put(ENSEMBL_GENES, ENSEMBL_DB);
    }

    public KeggGenesMapper(KeggService service, String organismId) {
        super("KeggGenes", false, true, service, organismId);
    }


    @Override
    public MappingData map(MappingContext context, MappingData data, MappingNode src, MappingNode dst, IProgressMonitor monitor) throws MappingException {
        Map<String, Set<String>> map = new HashMap<>();

        monitor.begin("Getting mapping information from KEGG ...", 1);

        // TODO Filter out items not in data if data is not empty

        // Get map from the API
        try {
            String prefix = fileKey.get(dst.getId());
            if (!KEGG_GENES.equals(src.getId()) || prefix == null) {
                throw new MappingException("Unsupported mapping from " + src + " to " + dst);
            }

            if (prefix.equals(ENSEMBL_DB)) {
                prefix = prefix + "-" + organismId;
            }

            List<IdConversion> relations = service.getConvert(organismId, prefix);

            int plen = prefix.length() + 1;
            for (IdConversion rel : relations) {
                String srcId = rel.getSourceId();
                String dstId = rel.getTargetId();
                Set<String> b = map.get(srcId);
                if (b == null) {
                    b = new HashSet<>();
                    map.put(srcId, b);
                }
                b.add(dstId.substring(plen));
            }
        } catch (Exception ex) {
            throw new MappingException(ex);
        }

        monitor.begin("Mapping KEGG genes ...", 1);

        if (data.isEmpty()) {
            data.identity(map.keySet());
        }

        data.map(map);

        return data;
    }
}
