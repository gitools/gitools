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
package org.gitools.datasources.idmapper;

import org.gitools.api.analysis.IProgressMonitor;


public abstract class AbstractMapper implements Mapper {

    private final String name;
    private final boolean bidirectional;
    private final boolean generator;

    protected AbstractMapper(String name, boolean bidirectional, boolean generator) {
        this.name = name;
        this.bidirectional = bidirectional;
        this.generator = generator;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBidirectional() {
        return bidirectional;
    }

    @Override
    public boolean isGenerator() {
        return generator;
    }

    @Override
    public void initialize(MappingContext context, IProgressMonitor monitor) throws MappingException {
    }

    @Override
    public void finalize(MappingContext context, IProgressMonitor monitor) throws MappingException {
    }

    @Override
    public String toString() {
        return name;
    }
}
