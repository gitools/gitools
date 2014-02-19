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
package org.gitools.analysis.groupcomparison.dimensiongroups;

import org.gitools.api.matrix.IMatrixPredicate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DimensionGroup {

    protected String name = "";
    protected DimensionGroupEnum groupType;
    protected IMatrixPredicate predicate;
    protected String property = "";
    protected Integer groupSize;


    @SuppressWarnings("UnusedDeclaration")
    public DimensionGroup() {
        //JAXB Requirement
    }

    public DimensionGroup(String name, IMatrixPredicate predicate, DimensionGroupEnum groupType, String property) {
        this.name = name;
        this.predicate = predicate;
        this.groupType = groupType;
        this.property = property;
        groupSize = null;
    }

    public DimensionGroup(String name, IMatrixPredicate predicate, DimensionGroupEnum groupType, String property, Integer groupSize) {
        this.name = name;
        this.predicate = predicate;
        this.groupType = groupType;
        this.property = property;
        this.groupSize = groupSize;
    }


    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }

    /**
     * If applicable, returns the size of the group or null if
     * group size may vary
     *
     * @return Integer|null
     */

    public Integer getGroupSize() {
        return groupSize;
    }

    public String getProperty() {
        return property;
    }

    public IMatrixPredicate getPredicate() {
        return predicate;
    }

    public DimensionGroupEnum getGroupType() {
        return groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
