package org.gitools.analysis.combination;

import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.ResourceReference;

public class ConvertModuleMapToMatrixResourceReference extends ResourceReference<IMatrix> {

    public ConvertModuleMapToMatrixResourceReference(IResourceLocator locator) {
        super(locator);
    }

    @Override
    protected IMatrix onAfterLoad(IResource resource) {

        if (resource != null && resource instanceof ModuleMap) {
            return MatrixUtils.moduleMapToMatrix((ModuleMap) resource);
        }

        return (IMatrix) resource;
    }

}
