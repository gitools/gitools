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
package org.gitools.matrix.sort;

import org.gitools.utils.aggregation.IAggregator;
import org.jetbrains.annotations.NotNull;

/**
 * @noinspection ALL
 */
public final class ValueSortCriteria
{

    public enum SortDirection
    {
        ASCENDING("Ascending", 1),
        DESCENDING("Descending", -1);

        private final String title;
        private final int factor;

        private SortDirection(String title, int factor)
        {
            this.title = title;
            this.factor = factor;
        }

        public int getFactor()
        {
            return factor;
        }

        @Override
        public String toString()
        {
            return title;
        }
    }

    private String attributeName;
    private int attributeIndex;
    private IAggregator aggregator;
    private SortDirection direction;

    public ValueSortCriteria(int attributeIndex, IAggregator aggregator, SortDirection direction)
    {

        this(null, attributeIndex, aggregator, direction);
    }

    public ValueSortCriteria(String attributeName, int attributeIndex, IAggregator aggregator, SortDirection direction)
    {

        this.attributeName = attributeName;
        this.attributeIndex = attributeIndex;
        this.direction = direction;
        this.aggregator = aggregator;
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }

    public final int getAttributeIndex()
    {
        return attributeIndex;
    }

    public final void setAttributeIndex(int propIndex)
    {
        this.attributeIndex = propIndex;
    }

    public final SortDirection getDirection()
    {
        return direction;
    }

    public final void setDirection(SortDirection direction)
    {
        this.direction = direction;
    }

    public final IAggregator getAggregator()
    {
        return aggregator;
    }

    public final void setAggregator(IAggregator aggregator)
    {
        this.aggregator = aggregator;
    }

    @NotNull
    @Override
    public String toString()
    {
        return attributeName + ", " + aggregator.toString() + ", " + direction.toString();
    }
}
