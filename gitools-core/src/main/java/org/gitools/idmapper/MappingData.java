/*
 * #%L
 * gitools-core
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
package org.gitools.idmapper;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MappingData
{

    private final MappingNode srcNode;
    private MappingNode dstNode;

    private final Set<String> dstIds;
    private final Map<String, Set<String>> map;

    public MappingData(String srcId, String dstId)
    {
        this.srcNode = new StringMappingNode(srcId);
        this.dstNode = new StringMappingNode(dstId);

        dstIds = new HashSet<String>();
        map = new HashMap<String, Set<String>>();
    }

    public MappingNode getSrcNode()
    {
        return srcNode;
    }

    public MappingNode getDstNode()
    {
        return dstNode;
    }

    public void setDstNode(MappingNode dstNode)
    {
        this.dstNode = dstNode;
    }

    @NotNull
    public Set<String> getSrcIds()
    {
        return map.keySet();
    }

    public Set<String> getDstIds()
    {
        return dstIds;
    }

    public Set<String> get(String srcId)
    {
        Set<String> d = map.get(srcId);
        if (d == null)
        {
            d = new HashSet<String>();
        }
        return d;
    }

    public void put(String srcId, String dstId)
    {
        Set<String> ids = map.get(srcId);
        if (ids == null)
        {
            ids = new HashSet<String>();
            map.put(srcId, ids);
        }
        dstIds.add(dstId);
        ids.add(dstId);
    }

    void clearDstIds()
    {
        dstIds.clear();
    }

    public void set(String srcId, Set<String> dids)
    {
        map.put(srcId, dids);
        dstIds.addAll(dids);
    }

    public Map<String, Set<String>> getMap()
    {
        return map;
    }

    public void map(@NotNull Map<String, Set<String>> dstmap)
    {
        clearDstIds();

        for (Map.Entry<String, Set<String>> e : map.entrySet())
        {
            Set<String> dset = new HashSet<String>();
            for (String sname : e.getValue())
            {
                Set<String> dnames = dstmap.get(sname);
                if (dnames != null)
                {
                    dset.addAll(dnames);
                }
            }
            set(e.getKey(), dset);
        }
    }

    void clear()
    {
        dstIds.clear();
        map.clear();
    }

    public boolean isEmpty()
    {
        return map.keySet().isEmpty();
    }

    public void identity(@NotNull Set<String> set)
    {
        clear();

        for (String id : set)
            put(id, id);
    }

    public void removeEmptyKeys()
    {
        List<String> rm = new ArrayList<String>();
        for (Map.Entry<String, Set<String>> e : map.entrySet())
            if (e.getValue().isEmpty())
            {
                rm.add(e.getKey());
            }

        for (String k : rm)
            map.remove(k);
    }

    @NotNull
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(map.size()).append(" keys, ");
        sb.append(dstIds.size()).append(" distinct values. ");
        sb.append(map.toString());
        return sb.toString();
    }
}
