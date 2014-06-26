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
package org.gitools.analysis.groupcomparison.filters;

import com.google.common.base.Function;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPredicate;
import org.gitools.api.matrix.MatrixDimensionKey;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupByLabelPredicate implements IMatrixPredicate<Double> {

    private MatrixDimensionKey dimensionKey;

    @XmlElementWrapper
    @XmlElement(name = "id")
    private Set<String> groupIdentifiers;

    public GroupByLabelPredicate() {
        //JAXB requirement
    }

    public GroupByLabelPredicate(IMatrixDimension dimension, Set<String> identifiers) {
        this.dimensionKey = dimension.getId();
        this.groupIdentifiers = identifiers;
    }

    public GroupByLabelPredicate(IMatrixDimension dimension, String groupAnnotation, Function<String, String> dimensionFunction) {
        groupIdentifiers = new HashSet<>();
        this.dimensionKey = dimension.getId();
        for (String identifier : dimension) {
            if (Objects.equals(dimensionFunction.apply(identifier), groupAnnotation)) {
                groupIdentifiers.add(identifier);
            }
        }
    }

    public boolean apply(Double value, IMatrixPosition position) {
        return groupIdentifiers.contains(position.get(dimensionKey));
    }

    public Set<String> getGroupIdentifiers() {
        return groupIdentifiers;
    }
}
