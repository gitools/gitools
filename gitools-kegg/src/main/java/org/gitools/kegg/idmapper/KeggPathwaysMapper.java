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
package org.gitools.kegg.idmapper;

import org.gitools.idmapper.MappingContext;
import org.gitools.idmapper.MappingData;
import org.gitools.idmapper.MappingException;
import org.gitools.idmapper.MappingNode;
import org.gitools.kegg.service.KeggService;
import org.gitools.kegg.service.domain.KeggPathway;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KeggPathwaysMapper extends AbstractKeggMapper implements AllIds
{

    public KeggPathwaysMapper(KeggService service, String organismId)
    {
        super("KeggPathways", false, true, service, organismId);
    }

    @Nullable
    @Override
    public MappingData map(MappingContext context, @NotNull MappingData data, @NotNull MappingNode src, MappingNode dst, @NotNull IProgressMonitor monitor) throws MappingException
    {
        if (!KEGG_PATHWAYS.equals(src.getId()))
        {
            throw new MappingException("Unsupported mapping from " + src + " to " + dst);
        }

        if (data.isEmpty())
        {
            monitor.begin("Getting KEGG pathways ...", 1);
            try
            {
                List<KeggPathway> pathwaysDefs = service.getPathways(organismId);
                for (KeggPathway d : pathwaysDefs)
                    data.put(d.getId(), d.getId());
            } catch (Exception ex)
            {
                throw new MappingException(ex);
            }
            monitor.end();
        }

        if (monitor.isCancelled())
        {
            return null;
        }

        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> dstIds = data.getDstIds();
        monitor.begin("Getting KEGG genes ...", dstIds.size());
        try
        {
            //int count = 0;
            for (String dstId : dstIds)
            {
                monitor.info(dstId);
                if (monitor.isCancelled())
                {
                    return null;
                }

                //if (count++ > 10) //FIXME
                //	break;

                List<String> genes = service.getGenesByPathway(dstId);
                map.put(dstId, new HashSet<String>(genes));
                monitor.worked(1);
            }
        } catch (Exception ex)
        {
            throw new MappingException(ex);
        }
        monitor.end();


        monitor.begin("Mapping KEGG pathways to KEGG genes ...", dstIds.size());

        data.map(map);

        monitor.end();

        return data;
    }

}
