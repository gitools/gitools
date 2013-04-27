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
package org.gitools.core.analysis.correlation;

import org.gitools.core.analysis.correlation.methods.PearsonCorrelationMethod;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.model.Analysis;
import org.gitools.core.persistence.ResourceReference;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Properties;

@XmlRootElement
public class CorrelationAnalysis extends Analysis {

    private String method;

    private Properties methodProperties;

    @Nullable
    private Double replaceNanValue;

    private boolean transposeData;

    private int attributeIndex;

    private ResourceReference<IMatrix> data;

    private ResourceReference<IMatrix> results;

    public CorrelationAnalysis() {
        this.method = PearsonCorrelationMethod.ID;
        this.methodProperties = new Properties();
        this.replaceNanValue = null;
        this.transposeData = false;
        this.attributeIndex = 0;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Properties getMethodProperties() {
        return methodProperties;
    }

    public void setMethodProperties(Properties methodProperties) {
        this.methodProperties = methodProperties;
    }

    public boolean isReplaceNanValues() {
        return replaceNanValue != null && !Double.isNaN(replaceNanValue);
    }

    @Nullable
    public Double getReplaceNanValue() {
        return replaceNanValue;
    }

    public void setReplaceNanValue(Double value) {
        this.replaceNanValue = value;
    }

    public boolean isTransposeData() {
        return transposeData;
    }

    public void setTransposeData(boolean transposeData) {
        this.transposeData = transposeData;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(int attributeIndex) {
        this.attributeIndex = attributeIndex;
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