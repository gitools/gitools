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

import org.gitools.analysis.AbstractMethod;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.correlation.CorrelationMethod;
import org.gitools.analysis.correlation.CorrelationResult;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

/**
 * @noinspection ALL
 */
public class SpearmanCorrelationMethod extends AbstractMethod implements CorrelationMethod {

    public static final String ID = "spearman";

    public SpearmanCorrelationMethod() {
        this(new Properties());
    }

    private SpearmanCorrelationMethod(Properties properties) {
        super(ID, "Spearman's rank correlation", "Spearman's rank correlation", CorrelationResult.class, properties);
    }

    @Nullable
    @Override
    public CorrelationResult correlation(double[] x, double[] y, int[] indices, int indicesLength) throws MethodException {
        /*RealMatrix data = new Array2DRowRealMatrix(new double[][] {x, y});

		CorrelationResult result = new CorrelationResult();

		SpearmansCorrelation correlation = new SpearmansCorrelation(data);
		result.setScore(correlation.correlation(x, y));

		PearsonsCorrelation pcor = correlation.getRankCorrelation();
		try {
			RealMatrix pvalues = pcor.getCorrelationPValues();
			result.setPvalue(pvalues.getEntry(0, 1));

			RealMatrix se = pcor.getCorrelationStandardErrors();
			result.setStandardError(se.getEntry(0, 1));
		}
		catch (MathException ex) {
			throw new MethodException(ex);
		}*/

        return null;
    }

}
