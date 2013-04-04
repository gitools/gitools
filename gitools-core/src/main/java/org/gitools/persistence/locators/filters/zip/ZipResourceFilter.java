package org.gitools.persistence.locators.filters.zip;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.locators.filters.AbstractResourceFilter;
import org.jetbrains.annotations.NotNull;

public class ZipResourceFilter extends AbstractResourceFilter
{
    public static final String SUFFIX = "zip";

    public ZipResourceFilter()
    {
        super(SUFFIX);
    }

    @NotNull
    @Override
    public IResourceLocator apply(IResourceLocator resourceLocator)
    {
        if (isFiltered(resourceLocator.getExtension())) {

            String entryName = removeExtension(resourceLocator.getName());

            return new ZipResourceLocatorAdaptor(entryName, this, resourceLocator);
        }

        return resourceLocator;
    }
}
