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

package org.gitools.persistence.text;

import org.gitools.persistence.AbstractResourcePersistence;

import java.util.Properties;


public abstract class ModuleMapPersistence<R> extends AbstractResourcePersistence<R> {

    public static final String MIN_SIZE = "min_size";
    public static final int DEFAULT_MIN_SIZE = 0;

    public static final String MAX_SIZE = "max_size";
    public static final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE;

    public static final String ITEM_NAMES_FILTER_ENABLED = "item_names_filter_enabled";
    public static final boolean DEFAULT_ITEM_NAMES_FILTER_ENABLED = false;

    public static final String ITEM_NAMES = "item_names";

    protected int getMinSize() {
        Properties p = getProperties();
        Integer value = (Integer) p.get(MIN_SIZE);
        return value != null ? value : DEFAULT_MIN_SIZE;
    }

    protected int getMaxSize() {
        Properties p = getProperties();
        Integer value = (Integer) p.get(MAX_SIZE);
        return value != null ? value : DEFAULT_MAX_SIZE;
    }

    protected boolean isItemNamesFilterEnabled() {
        Properties p = getProperties();
        Boolean value = (Boolean) p.get(ITEM_NAMES_FILTER_ENABLED);
        return value != null ? value : DEFAULT_ITEM_NAMES_FILTER_ENABLED;
    }

    protected String[] getItemNames() {
        Properties p = getProperties();
        String[] names = (String[]) p.get(ITEM_NAMES);
        return names != null ? names : null;
    }

}
