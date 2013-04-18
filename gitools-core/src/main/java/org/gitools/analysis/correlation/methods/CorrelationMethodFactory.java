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
package org.gitools.analysis.correlation.methods;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.correlation.CorrelationMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;


public class CorrelationMethodFactory {

    @NotNull
    public static CorrelationMethod createMethod(String methodId, Properties methodProperties) throws AnalysisException {
        if (PearsonCorrelationMethod.ID.equalsIgnoreCase(methodId)) {
            return new PearsonCorrelationMethod(methodProperties);
        } else if (SpearmanCorrelationMethod.ID.equalsIgnoreCase(methodId)) {
            return new PearsonCorrelationMethod(methodProperties);
        } else {
            throw new AnalysisException("Unknown correlation method: " + methodId);
        }
    }
}
