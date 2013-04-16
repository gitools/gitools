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
package org.gitools.persistence.formats.matrix;

import org.gitools.matrix.model.matrix.DoubleMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

public class DoubleMatrixFormat extends AbstractTextMatrixFormat<DoubleMatrix>
{

    public DoubleMatrixFormat()
    {
        super(FileSuffixes.DOUBLE_MATRIX, DoubleMatrix.class);
    }

    DoubleMatrixFormat(String extension, int skipLines, Integer... skipColumns)
    {
        super(extension, DoubleMatrix.class, skipLines, skipColumns);
    }

    @NotNull
    @Override
    protected DoubleMatrix createEntity()
    {
        return new DoubleMatrix();
    }

    @Override
    protected DoubleMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException
    {
        return read(resourceLocator, getValueTranslator(), progressMonitor);
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, DoubleMatrix doubleMatrix, IProgressMonitor progressMonitor) throws PersistenceException
    {
        write(resourceLocator, doubleMatrix, getValueTranslator(), progressMonitor);
    }
}
