package org.gitools.persistence.locators.filters.gz;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.locators.filters.IResourceFilter;
import org.jetbrains.annotations.NotNull;

public class GzResourceFilter implements IResourceFilter
{
    @Override
    public boolean isFiltered(@NotNull String extension)
    {
        return extension.endsWith(".gz");
    }

    @NotNull
    @Override
    public IResourceLocator apply(@NotNull IResourceLocator resourceLocator)
    {
        if (isFiltered(resourceLocator.getExtension())) {
            return new GzResourceLocatorAdapter(resourceLocator);
        }

        return resourceLocator;
    }

    @Override
    public String removeExtension(@NotNull String resourceExtension)
    {
        if (isFiltered(resourceExtension))
        {
            return resourceExtension.substring(0, resourceExtension.length() - 3);
        }

        return resourceExtension;
    }
}
