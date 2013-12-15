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
package org.gitools.analysis.groupcomparison.DimensionGroups;


import org.gitools.analysis.groupcomparison.filters.GroupByLabelPredicate;


public class DimensionGroupAnnotation extends DimensionGroup {

    private final String property;

    public DimensionGroupAnnotation(String name, GroupByLabelPredicate predicate) {
        super(name, predicate, DimensionGroupEnum.Annotation);
        property = String.valueOf(predicate.getGroupIdentifiers().size()) + "\n items";

    }

    @Override
    public String getProperty() {
        return property;
    }
}
