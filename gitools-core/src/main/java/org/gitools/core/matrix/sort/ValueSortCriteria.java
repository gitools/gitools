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
package org.gitools.core.matrix.sort;

import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.SortDirection;
import org.gitools.utils.aggregation.IAggregator;
import org.jetbrains.annotations.NotNull;

public final class ValueSortCriteria {

    private IMatrixLayer layer;
    private IAggregator aggregator;
    private SortDirection direction;

    public ValueSortCriteria(IMatrixLayer layer, IAggregator aggregator, SortDirection direction) {

        this.layer = layer;
        this.direction = direction;
        this.aggregator = aggregator;
    }

    public IMatrixLayer getLayer() {
        return layer;
    }

    public void setLayer(IMatrixLayer layer) {
        this.layer = layer;
    }

    public final SortDirection getDirection() {
        return direction;
    }

    public final void setDirection(SortDirection direction) {
        this.direction = direction;
    }

    public final IAggregator getAggregator() {
        return aggregator;
    }

    public final void setAggregator(IAggregator aggregator) {
        this.aggregator = aggregator;
    }

    @NotNull
    @Override
    public String toString() {
        return layer.getId() + ", " + aggregator.toString() + ", " + direction.toString();
    }
}
