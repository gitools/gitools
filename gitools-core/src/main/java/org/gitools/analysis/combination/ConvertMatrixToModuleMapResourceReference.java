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
package org.gitools.analysis.combination;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.persistence.ResourceReference;

public class ConvertMatrixToModuleMapResourceReference extends ResourceReference<IModuleMap> {

    public ConvertMatrixToModuleMapResourceReference(IResourceLocator locator, IResourceFormat resourceFormat) {
        super(locator, resourceFormat);
    }

    @Override
    protected IModuleMap onAfterLoad(IResource resource) {

        if (resource instanceof IMatrix) {
            return MatrixUtils.matrixToModuleMap((IMatrix) resource);
        }

        return (IModuleMap) resource;
    }

}
