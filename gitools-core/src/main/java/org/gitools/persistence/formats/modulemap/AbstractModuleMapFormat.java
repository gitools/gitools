/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence.formats.modulemap;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.AbstractResourceFormat;

import java.util.Properties;


public abstract class AbstractModuleMapFormat<R extends ModuleMap> extends AbstractResourceFormat<R> {

    public static final String MIN_SIZE = "min_size";
    public static final int DEFAULT_MIN_SIZE = 0;

    public static final String MAX_SIZE = "max_size";
    public static final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE;

    public static final String ITEM_NAMES_FILTER_ENABLED = "item_names_filter_enabled";
    public static final boolean DEFAULT_ITEM_NAMES_FILTER_ENABLED = false;

    public static final String ITEM_NAMES = "item_names";

    private int minSize;
    private int maxSize;
    private boolean itemNamesFilterEnabled;
    private String[] itemNames;

    protected AbstractModuleMapFormat(String extension, String mime, Class<R> resourceClass) {
        super(extension, mime, resourceClass);
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, Properties p, IProgressMonitor progressMonitor) throws PersistenceException {

        this.minSize = defaultValue(p.get(MIN_SIZE), DEFAULT_MIN_SIZE);
        this.maxSize = defaultValue(p.get(MAX_SIZE), DEFAULT_MAX_SIZE);
        this.itemNamesFilterEnabled = defaultValue(p.get(ITEM_NAMES_FILTER_ENABLED), DEFAULT_ITEM_NAMES_FILTER_ENABLED);
        this.itemNames = (String[]) p.get(ITEM_NAMES);

    }

    private static <T> T defaultValue(Object value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return (T) value;
    }

    protected int getMinSize() {
        return minSize;
    }

    protected int getMaxSize() {
        return maxSize;
    }

    protected boolean isItemNamesFilterEnabled() {
        return itemNamesFilterEnabled;
    }

    protected String[] getItemNames() {
        return itemNames;
    }

}
