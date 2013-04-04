package org.gitools.persistence.locators.filters.gz;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.locators.filters.AbstractResourceFilter;
import org.jetbrains.annotations.NotNull;

public class GzResourceFilter extends AbstractResourceFilter
{
    public static final String SUFFIX = "gz";

    public GzResourceFilter()
    {
        super(SUFFIX);
    }

    @NotNull
    @Override
    public IResourceLocator apply(@NotNull IResourceLocator resourceLocator)
    {
        if (isFiltered(resourceLocator.getExtension())) {
            return new GzResourceLocatorAdaptor(this, resourceLocator);
        }

        return resourceLocator;
    }


}
