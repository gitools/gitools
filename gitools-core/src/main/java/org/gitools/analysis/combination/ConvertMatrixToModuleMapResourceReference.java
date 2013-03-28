package org.gitools.analysis.combination;

import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.ResourceReference;

public class ConvertMatrixToModuleMapResourceReference extends ResourceReference<ModuleMap> {

    public ConvertMatrixToModuleMapResourceReference(IResourceLocator locator) {
        super(locator);
    }

    @Override
    protected ModuleMap onAfterLoad(IResource resource) {

        if (resource != null && resource instanceof IMatrix) {
            return MatrixUtils.matrixToModuleMap((IMatrix) resource);
        }

        return (ModuleMap) resource;
    }

}
