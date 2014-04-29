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
package org.gitools.api.matrix;

import org.gitools.api.analysis.IAggregator;

public interface IMatrixLayer<T> {

    String getId();

    String getName();

    String getDescription();

    Class<T> getValueClass();

    ValueTranslator<T> getTranslator();

    SortDirection getSortDirection();

    void setSortDirection(SortDirection direction);

    IAggregator getAggregator();

    void setAggregator(IAggregator aggregator);

    /**
     * This method is called when the layer is not in use.
     * <p/>
     * It's a good practice to free all the caching memory usage.
     */
    void detach();

    <T> void setCache(ICacheKey<T> key, T value);

    <T> T getCache(ICacheKey<T> key);

}
