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
package org.gitools.core.persistence.formats.modulemap;

import org.gitools.core.model.ModuleMap;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence.formats.AbstractResourceFormat;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;


public abstract class AbstractModuleMapFormat<R extends ModuleMap> extends AbstractResourceFormat<R> {

    public static final String MIN_SIZE = "min_size";
    private static final int DEFAULT_MIN_SIZE = 0;

    public static final String MAX_SIZE = "max_size";
    private static final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE;

    public static final String ITEM_NAMES_FILTER_ENABLED = "item_names_filter_enabled";
    private static final boolean DEFAULT_ITEM_NAMES_FILTER_ENABLED = false;

    public static final String ITEM_NAMES = "item_names";

    private int minSize;
    private int maxSize;
    private boolean itemNamesFilterEnabled;
    private String[] itemNames;

    AbstractModuleMapFormat(String extension, Class<R> resourceClass) {
        super(extension, resourceClass);
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, @NotNull Properties p, IProgressMonitor progressMonitor) throws PersistenceException {

        this.minSize = defaultValue(p.get(MIN_SIZE), DEFAULT_MIN_SIZE);
        this.maxSize = defaultValue(p.get(MAX_SIZE), DEFAULT_MAX_SIZE);
        this.itemNamesFilterEnabled = defaultValue(p.get(ITEM_NAMES_FILTER_ENABLED), DEFAULT_ITEM_NAMES_FILTER_ENABLED);
        this.itemNames = (String[]) p.get(ITEM_NAMES);

    }

    @Nullable
    private static <T> T defaultValue(@Nullable Object value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return (T) value;
    }

    int getMinSize() {
        return minSize;
    }

    int getMaxSize() {
        return maxSize;
    }

    boolean isItemNamesFilterEnabled() {
        return itemNamesFilterEnabled;
    }

    String[] getItemNames() {
        return itemNames;
    }

}
