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

import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.AbstractResourcePersistence;

import java.util.Map;

public abstract class BaseMatrixPersistence<T extends BaseMatrix> extends AbstractResourcePersistence<T> {

    public static final String POPULATION_LABELS = "population_labels";
    public static final String BACKGROUND_VALUE = "background_value";
    public static final String BINARY_VALUES = "binary_values";
    public static final String VALUE_TRANSLATORS = "value_translators";

    /**
     * Returns the array of labels to consider as background population,
     * or null if no population is specified.
     *
     * @return population labels
     */
    protected String[] getPopulationLabels() {
        return (String[]) getProperties().get(POPULATION_LABELS);
    }

    /**
     * The value to use for population rows not having data in the file
     *
     * @return background value
     */
    protected Double getBackgroundValue() {
        if (getProperties().containsKey(BACKGROUND_VALUE))
            return (Double) getProperties().get(BACKGROUND_VALUE);
        else
            return 0.0;
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
    protected ValueTranslator getValueTranslator(int i) {
        if (getProperties().containsKey(VALUE_TRANSLATORS)) {
            Map<Integer, ValueTranslator> valueTranslatorMap;
            valueTranslatorMap = (Map<Integer, ValueTranslator>) getProperties().get(VALUE_TRANSLATORS);
            if (valueTranslatorMap.containsKey(i))
                return valueTranslatorMap.get(i);
            else
                return null;
        } else
            return new DoubleTranslator();
    }

}
