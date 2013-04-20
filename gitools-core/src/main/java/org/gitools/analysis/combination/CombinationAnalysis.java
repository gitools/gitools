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
package org.gitools.analysis.combination;

import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.ResourceReference;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CombinationAnalysis extends Analysis {

    private String sizeAttrName;

    private String pvalueAttrName;

    private boolean transposeData;

    private ResourceReference<ModuleMap> groupsMap;

    private ResourceReference<IMatrix> data;

    private ResourceReference<IMatrix> results;

    public CombinationAnalysis() {
        this.transposeData = false;
    }

    public String getSizeAttrName() {
        return sizeAttrName;
    }

    public void setSizeAttrName(String sizeAttrName) {
        this.sizeAttrName = sizeAttrName;
    }

    public String getPvalueAttrName() {
        return pvalueAttrName;
    }

    public void setPvalueAttrName(String pvalueAttrName) {
        this.pvalueAttrName = pvalueAttrName;
    }

    public boolean isTransposeData() {
        return transposeData;
    }

    public void setTransposeData(boolean transposeData) {
        this.transposeData = transposeData;
    }

    public ResourceReference<ModuleMap> getGroupsMap() {
        return groupsMap;
    }

    public void setGroupsMap(ResourceReference<ModuleMap> groupsMap) {
        this.groupsMap = groupsMap;
    }

    public ResourceReference<IMatrix> getData() {
        return data;
    }

    public void setData(ResourceReference<IMatrix> data) {
        this.data = data;
    }

    public ResourceReference<IMatrix> getResults() {
        return results;
    }

    public void setResults(ResourceReference<IMatrix> results) {
        this.results = results;
    }
}
