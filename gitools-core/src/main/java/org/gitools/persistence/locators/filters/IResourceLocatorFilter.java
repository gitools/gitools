package org.gitools.persistence.locators.filters;

import org.gitools.persistence.IResourceLocator;

public interface IResourceLocatorFilter {

    boolean isFilterable(IResourceLocator resourceLocator);

    boolean isFiltered(IResourceLocator resourceLocator);

    IResourceLocator getUnfilteredLocator(IResourceLocator resourceLocator);

    IResourceLocator getFilteredLocator(IResourceLocator resourceLocator);
}
