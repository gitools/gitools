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
package org.gitools.persistence.locators.filters;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractResourceFilter implements IResourceFilter {
    private final String suffix;

    /**
     * Instantiates a new Abstract resource filter.
     *
     * @param suffix the extension that identifies this filter
     */
    protected AbstractResourceFilter(String suffix) {
        this.suffix = "." + suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public boolean isFiltered(String extension) {
        return extension.endsWith(suffix);
    }

    @Override
    public String removeExtension(@NotNull String resourceExtension) {
        if (isFiltered(resourceExtension)) {
            return resourceExtension.substring(0, resourceExtension.length() - suffix.length());
        }

        return resourceExtension;
    }

}
