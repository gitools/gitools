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
package org.gitools.core.persistence.formats.matrix;

import org.gitools.core.datafilters.DoubleTranslator;
import org.gitools.core.matrix.model.matrix.DoubleBinaryMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence._DEPRECATED.FileSuffixes;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

public class DoubleBinaryMatrixFormat extends AbstractTextMatrixFormat<DoubleBinaryMatrix> {

    public DoubleBinaryMatrixFormat() {
        super(FileSuffixes.DOUBLE_BINARY_MATRIX, DoubleBinaryMatrix.class);
    }

    @NotNull
    @Override
    protected DoubleBinaryMatrix createEntity() {
        return new DoubleBinaryMatrix();
    }

    @Override
    protected DoubleBinaryMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        return read(resourceLocator, new DoubleTranslator(), progressMonitor);
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, DoubleBinaryMatrix resource, IProgressMonitor progressMonitor) throws PersistenceException {
        write(resourceLocator, resource, new DoubleTranslator(), progressMonitor);
    }

}
