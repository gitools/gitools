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

import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.kegg.service.domain.KeggOrganism;
import org.gitools.modules.importer.Organism;
import org.gitools.modules.importer.RefImpl;
import org.jetbrains.annotations.NotNull;

public class EnsemblKeggOrganism extends RefImpl implements Organism
{

    private DatasetInfo ensemblDataset;
    private KeggOrganism keggDef;

    public EnsemblKeggOrganism(String id, String name, DatasetInfo dataset)
    {
        super(id, name);
        this.ensemblDataset = dataset;
    }

    public EnsemblKeggOrganism(String id, String name, KeggOrganism def)
    {
        super(id, name);
        this.keggDef = def;
    }

    public DatasetInfo getEnsemblDataset()
    {
        return ensemblDataset;
    }

    public void setEnsemblDataset(DatasetInfo ensemblDataset)
    {
        this.ensemblDataset = ensemblDataset;
    }

    public KeggOrganism getKeggOrganism()
    {
        return keggDef;
    }

    public void setKeggDef(KeggOrganism keggDef)
    {
        this.keggDef = keggDef;
    }

    @NotNull
    @Override
    public String getRef()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getRef()).append("[");
        if (ensemblDataset != null)
        {
            sb.append("ENSEMBL");
        }
        if (ensemblDataset != null && keggDef != null)
        {
            sb.append(", ");
        }
        if (keggDef != null)
        {
            sb.append("KEGG");
        }
        sb.append("]");
        return sb.toString();
    }
}
