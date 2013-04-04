package org.gitools.persistence.locators.filters;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractResourceFilter implements IResourceFilter
{
    private String suffix;

    /**
     * Instantiates a new Abstract resource filter.
     *
     * @param suffix the extension that identifies this filter
     */
    protected AbstractResourceFilter(String suffix)
    {
        this.suffix = "." + suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public boolean isFiltered(String extension)
    {
        return extension.endsWith(suffix);
    }

    @Override
    public String removeExtension(@NotNull String resourceExtension)
    {
        if (isFiltered(resourceExtension))
        {
            return resourceExtension.substring(0, resourceExtension.length() - suffix.length());
        }

        return resourceExtension;
    }

}
