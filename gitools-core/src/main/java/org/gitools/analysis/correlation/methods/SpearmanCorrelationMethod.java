/*
 *  Copyright 2010 cperez.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.analysis.correlation.methods;

import java.util.Properties;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.SpearmansCorrelation;
import org.gitools.analysis.AbstractMethod;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.correlation.CorrelationMethod;
import org.gitools.analysis.correlation.CorrelationResult;


public class SpearmanCorrelationMethod extends AbstractMethod implements CorrelationMethod {

	public static final String ID = "spearman";

	public SpearmanCorrelationMethod() {
		this(new Properties());
	}

	public SpearmanCorrelationMethod(Properties properties) {
		super(ID,
				"Spearman's rank correlation",
				"Spearman's rank correlation",
				CorrelationResult.class, properties);
	}

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
