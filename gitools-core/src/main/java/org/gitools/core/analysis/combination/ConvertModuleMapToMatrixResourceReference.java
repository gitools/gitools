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
package org.gitools.core.analysis.combination;

import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.persistence.IResource;
import org.gitools.core.persistence.IResourceFormat;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.utils.MatrixUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConvertModuleMapToMatrixResourceReference extends ResourceReference<IMatrix> {

    public ConvertModuleMapToMatrixResourceReference(IResourceLocator locator, IResourceFormat resourceFormat) {
        super(locator, resourceFormat);
    }

    @NotNull
    @Override
    protected IMatrix onAfterLoad(@Nullable IResource resource) {

        if (resource instanceof IModuleMap) {
            return MatrixUtils.moduleMapToMatrix((IModuleMap) resource);
        }

        return (IMatrix) resource;
    }

}
