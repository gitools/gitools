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
package org.gitools.persistence.formats.matrix;

import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.matrix.BaseMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.AbstractResourceFormat;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    private Map<Integer, ValueTranslator> valueTranslatorMap;


    AbstractMatrixFormat(String extension, Class<T> resourceClass) {
        super(extension, resourceClass);
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, @NotNull Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {

        // Save properties
        this.properties = properties;

        // Population labels
        this.populationLabels = (String[]) properties.get(POPULATION_LABELS);

        // Background value
        if (properties.containsKey(BACKGROUND_VALUE)) {
            this.backgroundValue = (Double) properties.get(BACKGROUND_VALUE);
        } else {
            this.backgroundValue = 0.0;
        }

        // Value translator map
        if (properties.containsKey(VALUE_TRANSLATORS)) {
            valueTranslatorMap = (Map<Integer, ValueTranslator>) properties.get(VALUE_TRANSLATORS);
        } else {
            valueTranslatorMap = null;
        }
    }

    Properties getProperties() {
        return properties;
    }

    /**
     * Returns the array of labels to consider as background population,
     * or null if no population is specified.
     *
     * @return population labels
     */
    String[] getPopulationLabels() {
        return populationLabels;
    }

    /**
     * The value to use for population rows not having data in the file
     *
     * @return background value
     */
    Double getBackgroundValue() {
        return this.backgroundValue;
    }

    /**
     * Returns the String <-> Double translator to use.
     * <p/>
     * the class calling this method assumes there is only ONE ValueTranslator
     *
     * @return value translator
     */
    @Nullable
    ValueTranslator getValueTranslator() {
        return getValueTranslator(0);
    }

    /**
     * Returns the String <-> Double translator to use for
     * value index i.
     *
     * @return value translator
     */
    @Nullable
    @Deprecated
    ValueTranslator getValueTranslator(int i) {
        if (valueTranslatorMap == null) {
            return new DoubleTranslator();
        }

        if (valueTranslatorMap.containsKey(i)) {
            return valueTranslatorMap.get(i);
        } else {
            return null;
        }
    }

}
