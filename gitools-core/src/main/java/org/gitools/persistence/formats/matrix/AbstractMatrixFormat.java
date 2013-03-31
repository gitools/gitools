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

package org.gitools.persistence.formats.matrix;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.AbstractResourceFormat;

import java.util.Map;
import java.util.Properties;

public abstract class AbstractMatrixFormat<T extends BaseMatrix> extends AbstractResourceFormat<T> {

    public static final String POPULATION_LABELS = "population_labels";
    public static final String BACKGROUND_VALUE = "background_value";
    public static final String BINARY_VALUES = "binary_values";
    public static final String VALUE_TRANSLATORS = "value_translators";

    private Properties properties;
    private String[] populationLabels;
    private Double backgroundValue;
    private Map<Integer, ValueTranslator> valueTranslatorMap;


    public AbstractMatrixFormat(String extension, String mime, Class<T> resourceClass) {
        super(extension, mime, resourceClass);
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {

        // Save properties
        this.properties = properties;

        // Population labels
        this.populationLabels = (String[]) properties.get(POPULATION_LABELS);

        // Background value
        if (properties.containsKey(BACKGROUND_VALUE))
            this.backgroundValue = (Double) properties.get(BACKGROUND_VALUE);
        else
            this.backgroundValue = 0.0;

        // Value translator map
        if (properties.containsKey(VALUE_TRANSLATORS)) {
            valueTranslatorMap = (Map<Integer, ValueTranslator>) properties.get(VALUE_TRANSLATORS);
        } else {
            valueTranslatorMap = null;
        }
    }

    protected Properties getProperties() {
        return properties;
    }

    /**
     * Returns the array of labels to consider as background population,
     * or null if no population is specified.
     *
     * @return population labels
     */
    protected String[] getPopulationLabels() {
        return populationLabels;
    }

    /**
     * The value to use for population rows not having data in the file
     *
     * @return background value
     */
    protected Double getBackgroundValue() {
        return this.backgroundValue;
    }

    /**
     * Returns the String <-> Double translator to use.
     * <p/>
     * the class calling this method assumes there is only ONE ValueTranslator
     *
     * @return value translator
     */
    protected ValueTranslator getValueTranslator() {
        return getValueTranslator(0);
    }

    /**
     * Returns the String <-> Double translator to use for
     * value index i.
     *
     * @return value translator
     */
    @Deprecated
    protected ValueTranslator getValueTranslator(int i) {
        if (valueTranslatorMap == null) {
            return new DoubleTranslator();
        }

        if (valueTranslatorMap.containsKey(i))
            return valueTranslatorMap.get(i);
        else
            return null;
    }

}
